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
        Instance instance = instanceRepository.findById(instanceId)
                .orElseThrow(() ->
                        new NullPointerException(String.format("???????????? %s ??? ???????????? ????????????", instanceId)));
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
        if (owner.getTicketCount() < 1) {
            throw DCloudException.ofIllegalArgumentException(
                    "????????? ??? ?????? ????????? ????????????", String.format("?????? [%s] ??? ????????? ????????? ????????????", owner));
        }
        owner.setTicketCount(owner.getTicketCount() - 1);

        Instance newInstance = Instance.skeletonInstance(newInstanceUUID, owner, instanceDto.getInstanceName());
        // ????????? ????????? ???????????? ?????? ?????? ????????? ????????? ?????????????????? ?????? ?????? ?????? ??????
        newInstance.setInitialized(true);
        final Instance savedNewInstance = instanceRepository.save(newInstance);

        int requestedGPU = instanceDto.getNumberOfGpuToUse();
        boolean useResourceExclusively = instanceDto.isUseGpuExclusively();
        ContainerImage containerImage =
                containerImageRepository.findById(instanceDto.getContainerImageId()).orElse(null);
        if (containerImage == null) {
            savedNewInstance.setError(true);
            String errorMessage = String.format("???????????? ????????? containerImageId :[%s] ??? ???????????? ????????????", instanceDto.getContainerImageId());
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
                    String.format("[%s] ?????? ????????? ???????????? ????????? ??????????????????", newInstanceUUID);
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
                    String.format("[%s] ??????????????? ????????? ??? ?????? ?????? GPU??? ?????? ????????? ????????????", newInstanceUUID);
            error = new DCloudError(errorMessage, 0);
            logger.error(errorMessage);
            savedNewInstance.writeInstanceLog(errorMessage);
            return error;
        }

        List<ServerPortDto.Creation> externalPorts = instanceDto.getExternalPorts();
        List<ServerPortDto.Creation> internalPorts = instanceDto.getInternalPorts();

        logger.info(String.format("[%s] ????????? ???????????? ????????? : [%s]", newInstanceUUID, containerImage));
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
            String errorMessage = String.format("[%s] ?????? : ??????????????? ????????? ?????? port ????????? ??????????????? : ?????? = [%s], ???????????? = [%s]",
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
                    String.format("[%s] ?????? : ?????? ????????? ???????????? ?????? ????????? ????????? ?????????????????? : \n " +
                                    "?????? = [%s], exception = [%s], stackTrace = [%s]",
                    newInstanceUUID, selectedServer.getInternalIP(), ExceptionUtils.getMessage(e), ExceptionUtils.getStackTrace(e));            savedNewInstance.setError(true);
            error = new DCloudError(errorMessage, 0);
            logger.error(errorMessage);
            savedNewInstance.writeInstanceLog(errorMessage);
            return error;
        }

        if (instanceCreationSshResult.getError() != null) {
            String errorMessage = String.format(
                    "[%s] ?????? : ?????? ?????? ???????????? ????????? ?????????????????? :\n ?????? = [%s], error = [%s]",
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

        // ??? ?????? ????????? ????????? ????????? ????????? ???????????? ????????? ??????????????? ??????
        newInstance.setInitialized(true);
        // ????????? ??????????????? ??????????????? ????????? ?????? ????????????
        // ???????????? ?????? ???, ????????? ????????? ??????????????? ???.

        serverPortRepository.saveAll(selectedPortList);
        savedNewInstance.setInstancePorts(selectedPortList);
        return null;
    }

    @Transactional
    public void startInstance(final long instanceId, final String loginId) throws IOException, JSchException {

        User user = userRepository.findUserByLoginId(loginId);

        Instance instance = instanceRepository.findById(instanceId).orElseThrow(() -> {
            String message = String.format("??????: ????????? [%s] ??? ???????????? ?????? ???????????? [%s] ??? ???????????? ?????????",
                    loginId, instanceId);
            logger.warn(message);
            return DCloudException.ofIllegalArgumentException(
                    "["+instanceId+"]??? ???????????? ?????? ???????????????!");
        });

        if (instance.getOwner() != user) {
            String message = "?????? ["+user.getLoginId()+"]??? ??????????????? ???????????? ID ["+instance.getInstanceId()+"], " +
                            "???????????? ?????? ["+instance.getInstanceHash()+"]??? ??????????????? ????????????.";
            instance.writeInstanceLog(message);
            throw DCloudException.ofIllegalArgumentException(message);
        }

        Server server = instance.getServer();
        if (server == null) {
            String message = String.format("??????: ???????????? ???????????? ???????????? ????????????????????? : instanceId : [%s], instanceUUID : [%s] ??? ????????? " +
                    "???????????? ????????????.", instance.getInstanceId(), instance.getInstanceContainerId());
            logger.warn(message);
            instance.writeInstanceLog(message);
            throw DCloudException.ofIllegalArgumentException(
                    "????????????????????? ????????? ????????? ??????????????????. ?????? ??????????????? ???????????????.");
        }

        String instanceHash = instance.getInstanceHash();
        if (instanceHash == null) {
            String mesage = String.format("??????: ???????????? ???????????? ???????????? ????????????????????? : instanceId : [%s], instanceUUID : [%s] ??? ?????????" +
                    "???????????? ????????????.", instance.getInstanceId(), instance.getInstanceContainerId());
            logger.warn(mesage);
            instance.writeInstanceLog(mesage);
            throw DCloudException.ofIllegalArgumentException(
                    "????????????????????? ????????? ????????? ??????????????????. ?????? ??????????????? ???????????????.");
        }
        sshExecutor.startInstance(instance.getInstanceHash(), instance.getServer().getSshPort());
        //sshExecutor.copyRemoteAccessScriptToInstance(instance.getServer().getSshPort(), instance.getInstanceHash());
        //server.restartRemoteAccessServices(instance.getInstanceHash());
    }

}


