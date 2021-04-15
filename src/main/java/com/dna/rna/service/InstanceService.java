package com.dna.rna.service;

import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.containerImage.ContainerImage;
import com.dna.rna.domain.containerImage.ContainerImageRepository;
import com.dna.rna.domain.instance.Instance;
import com.dna.rna.domain.instance.InstanceRepository;
import com.dna.rna.domain.instanceGpu.InstanceGpu;
import com.dna.rna.domain.instanceGpu.InstanceGpuRepository;
import com.dna.rna.domain.server.Server;
import com.dna.rna.domain.server.ServerRepository;
import com.dna.rna.domain.serverPort.ServerPort;
import com.dna.rna.domain.serverPort.ServerPortRepository;
import com.dna.rna.domain.user.User;
import com.dna.rna.domain.user.UserRepository;
import com.dna.rna.dto.InstanceCreationDto;
import com.dna.rna.dto.InstanceDto;
import com.dna.rna.dto.ServerPortDto;
import com.dna.rna.exception.DCloudException;
import com.dna.rna.service.util.DCloudError;
import com.dna.rna.service.util.InstanceResourceAllocator;
import com.dna.rna.service.util.SshExecutor;
import com.dna.rna.service.util.SshResult;
import com.jcraft.jsch.JSchException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class InstanceService {

    private static final Logger logger = LoggerFactory.getLogger(InstanceService.class);

    private final UserRepository userRepository;
    private final InstanceRepository instanceRepository;
    private final ServerRepository serverRepository;
    private final ServerPortRepository serverPortRepository;
    private final ContainerImageRepository containerImageRepository;
    private final InstanceGpuRepository instanceGpuRepository;
    private final SshExecutor sshExecutor;

    private final ReentrantLock lock = new ReentrantLock();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteInstance(long instanceId) throws IOException, JSchException {
        System.out.println("현재 쓰레드 = " + Thread.currentThread().getId());
        Instance instance = instanceRepository.findById(instanceId)
                .orElseThrow(() ->
                        new NullPointerException(String.format("인스턴스 %s 가 존재하지 않습니다", instanceId)));
        Server serverOfInstance = instance.getServer();
        lock.lock();

        if (serverOfInstance != null) {
            instanceGpuRepository.deleteAll(instance.getGpuList());
            serverPortRepository.removeServerPortByInstance(instance);
            sshExecutor.deleteInstance(serverOfInstance, instance);
        }
        lock.unlock();
        instanceRepository.delete(instance);
    }

    @Transactional
    public DCloudError createInstance(String newInstanceUUID, final InstanceDto.Post instanceDto, LocalDateTime expiredAt) throws Exception {
        DCloudError error = null;
        User owner = instanceDto.getOwner();
        Instance newInstance = Instance.skeletonInstance(newInstanceUUID, owner);
        // 중간에 에러로 리턴되는 경우 계속 초기화 작업이 진행중이라고 뜨는 것을 막기 위함
        newInstance.setInitialized(true);
        final Instance savedNewInstance = instanceRepository.save(newInstance);

        int requestedGPU = instanceDto.getNumberOfGpuToUse();
        boolean useResourceExclusively = instanceDto.isUseGpuExclusively();
        ContainerImage containerImage =
                containerImageRepository.findById(instanceDto.getContainerImageId()).orElse(null);
        if (containerImage == null) {
            savedNewInstance.setError(true);
            String errorMessage = String.format("사용자가 선택한 containerImageId :[%s] 가 존재하지 않습니다", instanceDto.getContainerImageId());
            error = new DCloudError(errorMessage, 0);
            logger.error(errorMessage);
            savedNewInstance.writeInstanceLog(errorMessage);
            return error;
        }

        List<Server> servers = serverRepository.findAll();
        List<Server> notExcludedServers = new ArrayList<>();
        servers.stream().filter(x -> !x.isExcluded()).forEach(notExcludedServers::add);
        if (notExcludedServers.size() == 0) {
            savedNewInstance.setError(true);
            String errorMessage =
                    String.format("[%s] 모든 서버가 사용제외 상태로 되어있습니다", newInstanceUUID);
            error = new DCloudError(errorMessage, 0);
            logger.error(errorMessage);
            savedNewInstance.writeInstanceLog(errorMessage);
            return error;
        }

        InstanceResourceAllocator instanceResourceAllocator = new InstanceResourceAllocator();
        InstanceResourceAllocator.AllocResult allocationResult;

        List<InstanceGpu> createdInstanceGpus = new ArrayList<>();
        try {
            allocationResult = instanceResourceAllocator.allocGpu(notExcludedServers, requestedGPU, useResourceExclusively);
            allocationResult.getGpuList().forEach(gpu -> {
                createdInstanceGpus.add(new InstanceGpu(newInstance, gpu, useResourceExclusively));
            });
            instanceGpuRepository.saveAll(createdInstanceGpus);
        } catch (Exception e) {
            savedNewInstance.setError(true);
            String errorMessage =
                    String.format("[%s] 배타적으로 사용할 수 있는 여분 GPU를 가진 서버가 없습니다", newInstanceUUID);
            error = new DCloudError(errorMessage, 0);
            logger.error(errorMessage);
            savedNewInstance.writeInstanceLog(errorMessage);
            return error;
        }

        List<ServerPortDto.Creation> externalPorts = instanceDto.getExternalPorts();
        List<ServerPortDto.Creation> internalPorts = instanceDto.getInternalPorts();

        logger.info(String.format("[%s] 사용할 컨테이너 이미지 : [%s]", newInstanceUUID, containerImage));
        externalPorts.add(new ServerPortDto.Creation("ssh", true, 22));
        externalPorts.add(new ServerPortDto.Creation("jupyter", true, 8888));
        internalPorts.add(new ServerPortDto.Creation("xrdp", true, 3389));

        Server selectedServer = allocationResult.getServer();
        serverRepository.saveAll(notExcludedServers);
        List<ServerPort> allocatedExternalPortList = serverPortRepository.fetchFreeExternalPortOfServer(selectedServer);
        List<ServerPort> allocatedInternalPortList = serverPortRepository.fetchFreeInternalPortOfServer(selectedServer);
        List<ServerPort> selectedPortList = new ArrayList<>();
        Set<Integer> allocatedExternalPortMap = new HashSet<>();
        Set<Integer> allocatedInternalPortMap = new HashSet<>();
        allocatedExternalPortList.forEach(x -> allocatedExternalPortMap.add(x.getFrom()));
        allocatedInternalPortList.forEach(x -> allocatedInternalPortMap.add(x.getFrom()));

        int minExternalPort = selectedServer.getMinExternalPort();
        int accSelected = 0;
        for (int i = minExternalPort; i < minExternalPort + Server.PORT_RANGE; i++) {
            if (!allocatedExternalPortMap.contains(i)) {
                ServerPortDto.Creation creationDto = externalPorts.get(accSelected);
                ServerPort serverPort = new ServerPort(creationDto.getTag(), true, i, creationDto.getTo(), null);
                selectedPortList.add(serverPort);
                accSelected++;
            }
            if (accSelected == externalPorts.size()) {
                break;
            }
        }

        if (accSelected != externalPorts.size()) {
            String errorMessage = String.format("[%s] 심각 : 인스턴스에 필요한 서버 port 개수가 부족합니다 : 서버 = [%s], 요청개수 = [%s]",
                    newInstanceUUID, selectedServer.getInternalIP(), externalPorts.size());
            savedNewInstance.setError(true);
            error = new DCloudError(errorMessage, 0);
            logger.error(errorMessage);
            savedNewInstance.writeInstanceLog(errorMessage);
            return error;
        }

        int minInternalPort = selectedServer.getMinInternalPort();
        int accInternalPortSelected = 0;
        for (int i = minInternalPort; i < minInternalPort + Server.PORT_RANGE; i++) {
            if (!allocatedInternalPortMap.contains(i)) {
                ServerPortDto.Creation creationDto = internalPorts.get(accInternalPortSelected);
                ServerPort serverPort = new ServerPort(creationDto.getTag(), false, i, creationDto.getTo(), null);
                selectedPortList.add(serverPort);
                accInternalPortSelected++;
            }
            if (accInternalPortSelected == internalPorts.size()) {
                break;
            }
        }

        SshResult<InstanceCreationDto> instanceCreationSshResult;
        List<Integer> allocatedGpuIndices = new ArrayList<>();
        for (InstanceGpu instanceGpu : createdInstanceGpus) {
            allocatedGpuIndices.add(instanceGpu.getGpu().getSlotIndex());
        }

        try {
            instanceCreationSshResult = sshExecutor.createNewInstance(selectedServer, owner, selectedPortList,
                    allocatedGpuIndices, newInstanceUUID, instanceDto.getSudoerId());
        } catch (Exception e) {
            String errorMessage =
                    String.format("[%s] 심각 : 원격 서버에 인스턴스 생성 시도중 예외가 발생했습니다 : \n " +
                                    "서버 = [%s], exception = [%s], stackTrace = [%s]",
                    newInstanceUUID, selectedServer.getInternalIP(), ExceptionUtils.getMessage(e), ExceptionUtils.getStackTrace(e));            savedNewInstance.setError(true);
            error = new DCloudError(errorMessage, 0);
            logger.error(errorMessage);
            savedNewInstance.writeInstanceLog(errorMessage);
            return error;
        }

        if (instanceCreationSshResult.getError() != null) {
            String errorMessage = String.format(
                    "[%s] 심각 : 원격 서버 인스턴스 생성에 실패했습니다 :\n 서버 = [%s], error = [%s]",
                    newInstanceUUID, selectedServer.getInternalIP(), instanceCreationSshResult.getError());
            savedNewInstance.setError(true);
            error = new DCloudError(errorMessage, 0);
            logger.error(errorMessage);
            savedNewInstance.writeInstanceLog(errorMessage);
            return error;
        }

        InstanceCreationDto instanceCreationResult = instanceCreationSshResult.getResult();

        savedNewInstance.setInstanceContainerId(instanceCreationResult.getInstanceContainerId());
        savedNewInstance.setInstanceHash(instanceCreationResult.getInstanceHash());
        savedNewInstance.setOwner(owner);
        savedNewInstance.setContainerImage(containerImage);
        savedNewInstance.setServer(selectedServer);
        savedNewInstance.setAllocatedResources(new ServerResource());
        savedNewInstance.setExpiredAt(expiredAt);
        savedNewInstance.getAllocatedResources().setGpus(allocatedGpuIndices);
        instanceRepository.save(savedNewInstance);

        selectedPortList.forEach(x -> x.setInstance(savedNewInstance));

        // 더 이상 에러로 리턴이 없으며 쓰레드 시작전에 초기화 진행중으로 토글
        newInstance.setInitialized(true);
        // 초기화 프로세스는 일반적으로 시간이 오래 걸리므로
        // 쓰레드로 돌린 후, 나중에 결과를 확인하도록 함.

        serverPortRepository.saveAll(selectedPortList);
        savedNewInstance.setInstancePorts(selectedPortList);
        return null;
    }

    @Transactional
    public void startInstance(final long instanceId, final String loginId) throws IOException, JSchException {

        User user = userRepository.findUserByLoginId(loginId);

        Instance instance = instanceRepository.findById(instanceId).orElseThrow(() -> {
            String message = String.format("심각: 사용자 [%s] 가 존재하지 않는 인스턴스 [%s] 를 시작하려 시도함",
                    loginId, instanceId);
            logger.warn(message);
            return DCloudException.ofIllegalArgumentException(
                    "["+instanceId+"]는 존재하지 않는 유저입니다!");
        });

        if (instance.getOwner() != user) {
            String message = "유저 ["+user.getLoginId()+"]가 시작하려는 인스턴스 ID ["+instance.getInstanceId()+"], " +
                            "인스턴스 해쉬 ["+instance.getInstanceHash()+"]의 조작권한이 없습니다.";
            instance.writeInstanceLog(message);
            throw DCloudException.ofIllegalArgumentException(message);
        }

        Server server = instance.getServer();
        if (server == null) {
            String message = String.format("심각: 인스턴스 데이터가 심각하게 훼손되었습니다 : instanceId : [%s], instanceUUID : [%s] 의 서버가 " +
                    "존재하지 않습니다.", instance.getInstanceId(), instance.getInstanceContainerId());
            logger.warn(message);
            instance.writeInstanceLog(message);
            throw DCloudException.ofIllegalArgumentException(
                    "데이터베이스에 심각한 오류가 발생했습니다. 서버 관리자에게 문의하세요.");
        }

        String instanceHash = instance.getInstanceHash();
        if (instanceHash == null) {
            String mesage = String.format("심각: 인스턴스 데이터가 심각하게 훼손되었습니다 : instanceId : [%s], instanceUUID : [%s] 의 서버가" +
                    "존재하지 않습니다.", instance.getInstanceId(), instance.getInstanceContainerId());
            logger.warn(mesage);
            instance.writeInstanceLog(mesage);
            throw DCloudException.ofIllegalArgumentException(
                    "데이터베이스에 심각한 오류가 발생했습니다. 서버 관리자에게 문의하세요.");
        }
        sshExecutor.startInstance(instance.getInstanceHash(), instance.getServer().getSshPort());
        //sshExecutor.copyRemoteAccessScriptToInstance(instance.getServer().getSshPort(), instance.getInstanceHash());
        //server.restartRemoteAccessServices(instance.getInstanceHash());
    }

}


