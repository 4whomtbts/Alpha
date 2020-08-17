package com.dna.rna.service.util;

import com.dna.rna.domain.ServerResource;
import com.dna.rna.dto.InstanceCreationDto;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SshExecutorTest {

    @Test
    public void createInstance() throws Exception {
        SshExecutor sshExecutor = new SshExecutor();
        ServerResource serverResource = new ServerResource();
        List<Integer> gpus = new ArrayList(8);
        for (int i=0; i < 8; i++) {
            gpus.add(0);
        }
        gpus.set(0, 1);
        gpus.set(3, 1);
        gpus.set(5, 1);
        serverResource.setGpus(gpus);
        InstanceCreationDto result = sshExecutor.createNewInstance("8081", "7071", serverResource);
        assertThat(result).isNotNull();
    }


}
