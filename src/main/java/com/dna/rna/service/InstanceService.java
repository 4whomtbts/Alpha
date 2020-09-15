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
import com.dna.rna.service.util.InstanceNetworkAllocator;
import com.dna.rna.service.util.InstanceResourceAllocator;
import com.dna.rna.service.util.SshExecutor;
import com.dna.rna.service.util.SshResult;
import com.jcraft.jsch.JSchException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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

    private final ReentrantLock lock = new ReentrantLock();
    private final Object serverResourceLock = new Object();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteInstance(long instanceId) throws IOException, JSchException {
        Instance instance = instanceRepository.findById(instanceId).orElseThrow();
        Server serverOfInstance = instance.getServer();
        lock.lock();
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
        System.out.println("현재 쓰레드 = " + Thread.currentThread().getId());
        serverOfInstance.getServerResource().setGpus(changedServerGpus);
        SshExecutor.deleteInstance(serverOfInstance, instance);
        lock.unlock();
        serverRepository.save(serverOfInstance);
        instanceRepository.delete(instance);
    }

    @Transactional
    public void createInstance(final InstanceDto.Post instanceDto, LocalDateTime expiredAt) throws Exception {
        List<ServerPortDto.Creation> externalPorts = instanceDto.getExternalPorts();
        List<ServerPortDto.Creation> internalPorts = instanceDto.getInternalPorts();
        int requestedGPU = instanceDto.getNumberOfGpuToUse();
        boolean useResourceExclusively = instanceDto.isUseGpuExclusively();
        String instanceName = instanceDto.getInstanceName();
        User owner = instanceDto.getOwner();
        ContainerImage containerImage =
                containerImageRepository.findById(instanceDto.getContainerImageId())
                                        .orElseThrow(() ->
                                            DCloudException.ofInternalServerError("사용자가 선택한 containerImage"
                                                  + instanceDto.getContainerImageId()+"] 가 존재하지 않습니다"));

        externalPorts.add(new ServerPortDto.Creation("ssh", true, 22));
        internalPorts.add(new ServerPortDto.Creation("xrdp", false, 3389));

            List<Server> servers = serverRepository.findAll();
            List<Server> notExcludedServers = new ArrayList<>();
            for (int i=0; i < servers.size(); i++) {
                Server currServer = servers.get(i);
                if (!currServer.isExcluded()) {
                    notExcludedServers.add(currServer);
                }
            }

        InstanceResourceAllocator instanceResourceAllocator = new InstanceResourceAllocator();
        final InstanceResourceAllocator.AllocationResult result =
                instanceResourceAllocator.allocateGPU(notExcludedServers, requestedGPU, useResourceExclusively);

        Server selectedServer = notExcludedServers.get(result.getIndex());
        selectedServer.getServerResource().setGpus(result.getGpus());
        serverRepository.saveAll(notExcludedServers);
        InstanceNetworkAllocator instanceNetworkAllocator = new InstanceNetworkAllocator();
        List<ServerPort> allocatedExternalPortList = serverPortRepository.fetchFreeExternalPortOfServer(selectedServer);
        List<ServerPort> allocatedInternalPortList = serverPortRepository.fetchFreeInternalPortOfServer(selectedServer);
        List<ServerPort> selectedPortList = new ArrayList<>();
        Set<Integer> allocatedExternalPortMap = new HashSet<>();
        Set<Integer> allocatedInternalPortMap = new HashSet<>();
        for (ServerPort serverPort : allocatedExternalPortList) {
            allocatedExternalPortMap.add(serverPort.getFrom());
        }
        for (ServerPort serverPort : allocatedInternalPortList) {
            allocatedInternalPortMap.add(serverPort.getFrom());
        }

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
            logger.error("매우심각 : 인스턴스에 필요한 서버 port 개수가 부족합니다. / 서버 = {} / 요청개수 = {}",
                    selectedServer.getInternalIP(), externalPorts.size());
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

        if (accSelected != externalPorts.size()) {
            logger.error("매우심각 : 인스턴스에 필요한 서버내부노출 port 개수가 부족합니다. / 서버 = {} / 요청개수 = {}",
                    selectedServer.getInternalIP(), internalPorts.size());
        }

        SshExecutor sshExecutor = new SshExecutor();
        String containerUUID = UUID.randomUUID().toString();
        SshResult<InstanceCreationDto> instanceCreationSshResult =
                sshExecutor.createNewInstance(selectedServer, owner, selectedPortList,
                                              new ServerResource(result.getNewInstanceGpus()), containerUUID, instanceDto.getSudoerId());
        if (instanceCreationSshResult.getError() != null) {
            // 인스턴스 생성 후 장애가 발생했다고 가정하고 삭제함.
            SshExecutor.deleteContainer(selectedServer, containerUUID);
            throw DCloudException.ofInternalServerError(
                    "인스턴스 생성 중 오류가 발생했습니다. 실패 사유는 해당 인스턴스에서 인스턴스 로그 메뉴에서 확인하거나" +
                            "관리자에게 문의해주세요.",
                    "인스턴스 생성 실패 = " + instanceCreationSshResult.getError().getCode() + ", "
                            + instanceCreationSshResult.getError().getError());
        }

        InstanceCreationDto instanceCreationResult = instanceCreationSshResult.getResult();

        SshResult<String> copyResult =
                SshExecutor.copyInitShellScriptToInstance(selectedServer, instanceCreationResult.getInstanceHash());

        if (copyResult.getError() != null) {
            throw DCloudException.ofInternalServerError(
                    "인스턴스 생성 중 오류가 발생했습니다. 실패 사유는 해당 인스턴스에서 인스턴스 로그 메뉴에서 확인하거나" +
                            "관리자에게 문의해주세요.",
                    "인스턴스 생성 실패 = " + instanceCreationSshResult.getError().getCode() + ", "
                            + instanceCreationSshResult.getError().getError());
        }

        final Instance instance =
                new Instance(instanceName, instanceCreationResult.getInstanceContainerId(), instanceCreationResult.getInstanceHash(),
                        owner, containerImage, selectedServer, new ServerResource(), null, expiredAt);
        instance.getAllocatedResources().setGpus(result.getNewInstanceGpus());
        instanceRepository.save(instance);

        for (int i=0; i < selectedPortList.size(); i++) {
            selectedPortList.get(i).setInstance(instance);
        }

        // 초기화 프로세스는 일반적으로 시간이 오래 걸리므로
        // 쓰레드로 돌린 후, 나중에 결과를 확인하도록 함.
        executor.submit(() -> {
            try {
                SshResult<String> instanceInitSshResult =
                sshExecutor.executeInstanceInit(
                        selectedServer, instanceCreationResult.getInstanceContainerId(),
                        instanceDto.getSudoerId(), instanceDto.getSudoerPwd());
                if (instanceInitSshResult.getError() != null) {
                    instance.setError(true);
                    throw DCloudException.ofInternalServerError(
                            "인스턴스 생성 중 오류가 발생했습니다. 실패 사유는 해당 인스턴스에서 인스턴스 로그 메뉴에서 확인하거나" +
                                    "관리자에게 문의해주세요.",
                            "인스턴스 생성 실패 = " + instanceCreationSshResult.getError().getCode() + ", "
                                    + instanceCreationSshResult.getError().getError());
                }
                instanceRepository.save(instance);
            } catch (JSchException | IOException e) {
                instance.setError(true);
                throw DCloudException.ofInternalServerError(
                        "인스턴스 생성 중 오류가 발생했습니다. 실패 사유는 해당 인스턴스에서 인스턴스 로그 메뉴에서 확인하거나" +
                                "관리자에게 문의해주세요.",
                        "인스턴스 생성 실패 = " + instanceCreationSshResult.getError().getCode() + ", "
                                + instanceCreationSshResult.getError().getError(),
                        e);
            } finally {
                // 초기화에 실패, 성공 여부를 DB에 저장해서 사용자가 인스턴스
                // 목록을 확인했을 때 성공인지 실패인지 확인할 수 있게 하기 위함.
                instance.setInitialized(true);
                instanceRepository.save(instance);
            }
        });

        serverPortRepository.saveAll(selectedPortList);
        instance.setInstancePorts(selectedPortList);
    }

    @Transactional
    public void startInstance(final long instanceId, final String loginId) throws IOException, JSchException {

        User user = userRepository.findUserByLoginId(loginId);

        Instance instance = instanceRepository.findById(instanceId).orElseThrow(() -> {
            logger.warn("심각: 사용자 [{}] 가 존재하지 않는 인스턴스 [{}] 를 시작하려 시도함",
                         loginId, instanceId);
            return DCloudException.ofIllegalArgumentException(
                    "["+instanceId+"]는 존재하지 않는 유저입니다!");
        });

        if (instance.getOwner() != user) {
            throw DCloudException.ofIllegalArgumentException(
                    "유저 ["+user.getLoginId()+"]가 시작하려는 인스턴스 ID ["+instance.getInstanceId()+"], " +
                    "인스턴스 해쉬 ["+instance.getInstanceHash()+"]의 조작권한이 없습니다.");
        }

        Server server = instance.getServer();
        if (server == null) {
            logger.warn("매우심각: 인스턴스 데이터가 심각하게 훼손되었습니다. 인스턴스 ID : [{}] 의 서버가 " +
                            "존재하지 않습니다.", instanceId);
            throw DCloudException.ofIllegalArgumentException(
                    "데이터베이스에 심각한 오류가 발생했습니다. 서버 관리자에게 문의하세요.");
        }

        String instanceHash = instance.getInstanceHash();
        if (instanceHash == null) {
            logger.warn("매우심각: 인스턴스 데이터가 심각하게 훼손되었습니다. 인스턴스 ID : [{}] 의 인스턴스 해쉬가 " +
                    "존재하지 않습니다.", instanceId);
            throw DCloudException.ofIllegalArgumentException(
                    "데이터베이스에 심각한 오류가 발생했습니다. 서버 관리자에게 문의하세요.");
        }
        server.startInstance(instance.getInstanceHash());
        SshExecutor.copyRemoteAccessScriptToInstance(server, instance.getInstanceHash());
        server.restartRemoteAccessServices(instance.getInstanceHash());
    }

}


