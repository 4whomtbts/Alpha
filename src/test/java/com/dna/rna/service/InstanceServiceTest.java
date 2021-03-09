package com.dna.rna.service;

import com.dna.rna.domain.instance.Instance;
import com.dna.rna.domain.instance.InstanceRepository;
import com.jcraft.jsch.JSchException;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
    @SpringBootTest
    @TestPropertySource(locations="classpath:application-test.properties")
    public class InstanceServiceTest {

        @Autowired
        private InstanceService instanceService;

        @Autowired
        private InstanceRepository instanceRepository;

        @Autowired
        ApplicationContext ctx;

        @Value("${ssh.id}")
        private String sshId;


    @After
    public void removeAllInstance() {
        List<Instance> instances = instanceRepository.findAll();
        instances.forEach(x -> {
            try {
                instanceService.deleteInstance(x.getInstanceId());
            } catch (IOException | JSchException e) {
                e.printStackTrace();
            }
        });
    }
}
