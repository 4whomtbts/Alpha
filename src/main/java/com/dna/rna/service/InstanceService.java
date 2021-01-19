package com.dna.rna.service;

import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.containerImage.ContainerImage;
import com.dna.rna.domain.containerImage.ContainerImageRepository;
import com.dna.rna.domain.instance.Instance;
import com.dna.rna.domain.instance.InstanceRepository;
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
import com.dna.rna.service.util.*;
import com.jcraft.jsch.JSchException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class InstanceService {

    private static final Logger logger = LoggerFactory.getLogger(InstanceService.class);
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    private final UserRepository userRepository;
    private final InstanceRepository instanceRepository;
    private final ServerRepository serverRepository;
    private final ServerPortRepository serverPortRepository;
    private final ContainerImageRepository containerImageRepository;
    private final SshExecutor sshExecutor;

    private final ReentrantLock lock = new ReentrantLock();
    private final Object serverResourceLock = new Object();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteInstance(long instanceId) throws IOException, JSchException {
        System.out.println("현재 쓰레드 = " + Thread.currentThread().getId());
        Instance instance = instanceRepository.findById(instanceId).orElseThrow();
        Server serverOfInstance = instance.getServer();
        lock.lock();

        if (serverOfInstance != null) {
            if (serverOfInstance.getServerResource() != null) {
                List<Integer> serverGpus = serverOfInstance.getServerResource().getGpus();
                List<Integer> instanceGpus = instance.getAllocatedResources().getGpus();
                List<Integer> changedServerGpus = new ArrayList<>();
                for (int i=0; i < serverGpus.size(); i++) {
                    int currGpuStat = serverGpus.get(i);
                    int currInstanceStat = instanceGpus.get(i);

                    if ((currInstanceStat == ServerResource.EXCLUSIVELY_ALLOC)
                            && (currGpuStat == ServerResource.EXCLUSIVELY_ALLOC)) {
                        changedServerGpus.add(ServerResource.UN_ALLOC);
                    } else if (currInstanceStat == ServerResource.ALLOC) {
                        changedServerGpus.add(currGpuStat-1);
                    } else if (currInstanceStat == ServerResource.UN_ALLOC) {
                        changedServerGpus.add(currGpuStat);
                    } else {
                        logger.error("매우심각 : 인스턴스 [{}] 를 삭제하려는 도중 " +
                                        "서버 [{}] 의 gpu 상태가 [{}] 이고, 인스턴스의 gpu 상태가 [{}]로 비정상적입니다.",
                                instance.getInstanceId(), serverOfInstance.getInternalIP(), currGpuStat, currInstanceStat);
                    }
                }
                serverOfInstance.getServerResource().setGpus(changedServerGpus);
            }
            serverRepository.save(serverOfInstance);
            sshExecutor.deleteInstance(serverOfInstance, instance);
        } else {
            instanceRepository.delete(instance);
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
        InstanceResourceAllocator.AllocationResult allocationResult;

        try {
            allocationResult= instanceResourceAllocator.allocateGPU(notExcludedServers, requestedGPU, useResourceExclusively);
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
        internalPorts.add(new ServerPortDto.Creation("xrdp", false, 3389));


        Server selectedServer = notExcludedServers.get(allocationResult.getIndex());
        selectedServer.getServerResource().setGpus(allocationResult.getGpus());
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

        try {
            instanceCreationSshResult = sshExecutor.createNewInstance(selectedServer, owner, selectedPortList,
                    new ServerResource(allocationResult.getNewInstanceGpus()), newInstanceUUID, instanceDto.getSudoerId());
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

        SshResult<String> copyResult;
        try {
            copyResult =
                    sshExecutor.copyInitShellScriptToInstance(selectedServer.getSshPort(), instanceCreationResult.getInstanceHash());
        } catch (Exception e) {
            String errorMessage = String.format(
                    "[%s] 심각 : 원격 서버 인스턴스에 초기화 스크립트 복사에 실패했습니다 : 서버 = [%s], exception = [%s], stackTrace = [%s]",
                    newInstanceUUID, selectedServer.getInternalIP(), ExceptionUtils.getMessage(e), ExceptionUtils.getStackTrace(e));
            savedNewInstance.setError(true);
            error = new DCloudError(errorMessage, 0);
            logger.error(errorMessage);
            savedNewInstance.writeInstanceLog(errorMessage);
            return error;
        }

        if (copyResult.getError() != null) {
            String errorMessage = String.format("[%s] 초기화 쉘 스크립트 복사에 실패했습니다 : [%s]",
                    newInstanceUUID, copyResult.getError());
            savedNewInstance.setError(true);
            error = new DCloudError(errorMessage, 0);
            logger.error(errorMessage);
            savedNewInstance.writeInstanceLog(errorMessage);
            return error;
        }

        savedNewInstance.setInstanceContainerId(instanceCreationResult.getInstanceContainerId());
        savedNewInstance.setInstanceHash(instanceCreationResult.getInstanceHash());
        savedNewInstance.setOwner(owner);
        savedNewInstance.setContainerImage(containerImage);
        savedNewInstance.setServer(selectedServer);
        savedNewInstance.setAllocatedResources(new ServerResource());
        savedNewInstance.setExpiredAt(expiredAt);
        savedNewInstance.getAllocatedResources().setGpus(allocationResult.getNewInstanceGpus());
        instanceRepository.save(savedNewInstance);

        selectedPortList.forEach(x -> x.setInstance(savedNewInstance));

        // 더 이상 에러로 리턴이 없으며 쓰레드 시작전에 초기화 진행중으로 토글
        newInstance.setInitialized(false);
        // 초기화 프로세스는 일반적으로 시간이 오래 걸리므로
        // 쓰레드로 돌린 후, 나중에 결과를 확인하도록 함.
        final SshResult constInstanceCreationSshResult = instanceCreationSshResult;
        executor.submit(() -> {
            DCloudError initError = null;
            File logFile = new File("./" + newInstanceUUID);
            try {
                SshResult<String> instanceInitSshResult =
                sshExecutor.executeInstanceInit(
                        selectedServer.getSshPort(), instanceCreationResult.getInstanceContainerId(),
                        instanceDto.getSudoerId(), instanceDto.getSudoerPwd());

                if (instanceInitSshResult.getError() != null) {
                    savedNewInstance.setError(true);
                    String instanceInitErrorMessage =
                            String.format("[%s] 인스턴스 초기화 중 오류 발생 [%s]",
                                    newInstanceUUID, constInstanceCreationSshResult.getError());
                    initError = new DCloudError(instanceInitErrorMessage, 0);
                    savedNewInstance.writeInstanceLog(initError.toString());
                    logger.error(instanceInitErrorMessage);
                } else {
                    String finalMessage = instanceInitSshResult.getResult() + "\n\n\n" +
                            "*******************************************************\n\n" +
                                String.format("Dcloud 인스턴스 생성이 완료 되었습니다!\n " +
                                        "관리코드 [%s]\n " +
                                        "생성일자 [%s]",
                                        newInstanceUUID, new Timestamp(new Date().getTime()))
                            + "\n\n********************************************************\n\n";
                    logger.info("[{}] 인스턴스가 성공적으로 생성 및 초기화 되었습니다", newInstanceUUID);
                    savedNewInstance.writeInstanceLog(finalMessage);
                    instanceRepository.save(savedNewInstance);
                }
            } catch (JSchException | IOException e) {
                savedNewInstance.setError(true);
                String instanceInitErrorMessage =
                        String.format("[%s] 인스턴스 초기화 중 오류 발생 [%s]",
                                newInstanceUUID, constInstanceCreationSshResult.getError());
                initError = new DCloudError(instanceInitErrorMessage, 0);
                savedNewInstance.writeInstanceLog(initError.toString());
                logger.error(instanceInitErrorMessage);

            } finally {
                // 초기화에 실패, 성공 여부를 DB에 저장해서 사용자가 인스턴스
                // 목록을 확인했을 때 성공인지 실패인지 확인할 수 있게 하기 위함.
                savedNewInstance.setInitialized(true);
                savedNewInstance.writeInstanceLog(
                        String.format("인스턴스 [%s]의 초기화가 종료 되었습니다", newInstanceUUID));
                instanceRepository.save(savedNewInstance);
            }
        });

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
        server.startInstance(instance.getInstanceHash());
        sshExecutor.copyRemoteAccessScriptToInstance(instance.getServer().getSshPort(), instance.getInstanceHash());
        server.restartRemoteAccessServices(instance.getInstanceHash());
    }

}


