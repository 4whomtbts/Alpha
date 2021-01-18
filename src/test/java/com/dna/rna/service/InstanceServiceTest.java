package com.dna.rna.service;

import com.dna.rna.domain.containerImage.ContainerImageRepository;
import com.dna.rna.domain.instance.Instance;
import com.dna.rna.domain.instance.InstanceRepository;
import com.jcraft.jsch.JSchException;
import org.junit.After;
import org.junit.Test;
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
        private ContainerImageRepository containerImageRepository;

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

    @Test
    public void createInstanceTest() throws Exception {
/*
        final List<ContainerImage> containerImage = containerImageRepository.findAll();
        if (containerImage.size() == 0) {
            System.out.println("컨테이너 이미지가 존재하지 않습니다");
        }
        InstanceDto.Post newInstance = new InstanceDto.Post(
                "my_instance", User.of("loginId", "pass", "username" ,"org", "010-0000-0000", "abc@dgu.ac.kr"),
                "sudoer", "sudoerpwd", "purpose", containerImage.get(0).getContainerImageId(), 1,true, 5, true, new ArrayList<>(), new ArrayList<>());
        try {
            instanceService.createInstance(newInstance, LocalDateTime.now());
        } catch (Exception e) {
            System.out.println(e);
        }

        */
        System.out.println("hello");
    }
}
