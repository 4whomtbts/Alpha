package com.dna.rna.service;

import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.containerImage.ContainerImage;
import com.dna.rna.domain.containerImage.ContainerImageRepository;
import com.dna.rna.domain.instance.Instance;
import com.dna.rna.domain.instance.InstanceNetworkSetting;
import com.dna.rna.domain.instance.InstanceRepository;
import com.dna.rna.domain.server.Server;
import com.dna.rna.domain.server.ServerRepository;
import com.dna.rna.domain.serverPort.ServerPort;
import com.dna.rna.domain.serverPort.ServerPortRepository;
import com.dna.rna.dto.InstanceCreationDto;
import com.dna.rna.service.util.InstanceNetworkAllocator;
import com.dna.rna.service.util.InstanceResourceAllocator;
import com.dna.rna.service.util.SshExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InstanceService {

    private final InstanceRepository instanceRepository;
    private final ServerRepository serverRepository;
    private final ServerPortRepository serverPortRepository;
    private final ContainerImageRepository containerImageRepository;

    @Transactional
    public void createInstance(String instanceName, ContainerImage containerImage, int requestedGPU,
                               boolean useResourceExclusively, LocalDateTime expiredAt) throws Exception {
        containerImageRepository.save(containerImage);
        List<Server> servers = serverRepository.findAll();
        InstanceResourceAllocator instanceResourceAllocator = new  InstanceResourceAllocator();
        final InstanceResourceAllocator.AllocationResult result = instanceResourceAllocator.allocateGPU(servers, requestedGPU, useResourceExclusively);
        Server selectedServer = servers.get(result.getIndex());
        selectedServer.getServerResource().setGpus(result.getGpus());
        serverRepository.saveAll(servers);
        InstanceNetworkAllocator instanceNetworkAllocator = new InstanceNetworkAllocator();
        Map<String, Integer> externalPorts = new HashMap<>();
        Map<String, Integer> internalPorts = new HashMap<>();
        ServerPort maxAllocatedPort = serverPortRepository.findFirstByServerOrderByPortDesc(selectedServer);
        int startPort;
        if (maxAllocatedPort != null) {
            startPort = maxAllocatedPort.getPort();
        }
        startPort = ServerPort.MIN_PORT;

        SshExecutor sshExecutor = new SshExecutor();
        InstanceCreationDto instanceCreationResult =
                sshExecutor.createNewInstance(selectedServer.getSshPort(), startPort, new ServerResource(result.getGpus()));
        Instance instance =
                new Instance(instanceName, instanceCreationResult.getInstanceUUID(), instanceCreationResult.getInstanceHash(),
                             containerImage, selectedServer, new ServerResource(), null, expiredAt);
        instanceRepository.save(instance);
        List<ServerPort> serverPortList = new ArrayList<>();
        ServerPort sshServerPort = new ServerPort("ssh", true, startPort, selectedServer, instance);
        ServerPort xrdpServerPort = new ServerPort("xrdp", false, 3389, selectedServer, instance);
        serverPortList.add(sshServerPort);
        serverPortList.add(xrdpServerPort);
        serverPortRepository.saveAll(serverPortList);
        instance.setInstancePorts(serverPortList);
        instanceRepository.save(instance);
    }


}


