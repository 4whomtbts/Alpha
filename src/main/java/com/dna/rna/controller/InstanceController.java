package com.dna.rna.controller;

import com.dna.rna.domain.containerImage.ContainerImage;
import com.dna.rna.domain.instance.Instance;
import com.dna.rna.domain.instance.InstanceRepository;
import com.dna.rna.dto.InstanceDto;
import com.dna.rna.service.InstanceService;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class InstanceController {

    private static final Logger logger= LoggerFactory.getLogger(InstanceController.class);

    private final InstanceRepository instanceRepository;
    private final InstanceService instanceService;

    @Transactional
    @GetMapping("/instance")
    public String instanceGET(Model model) {
        List<Instance> instanceList = instanceRepository.findAll();
        List<InstanceDto> instanceDtoList = new ArrayList<>();
        for (int i=0; i < instanceList.size(); i++) {
            instanceDtoList.add(instanceList.get(i).toInstanceDto());
        }
        model.addAttribute("instanceList", instanceDtoList);
        return "/instance/index";
    }

    @GetMapping("/instance/create")
    public String instanceCreateGET(Model model) {
        return "/instance/create";
    }

    @PostMapping("/instance/create")
    public String instanceCreatePOST(@ModelAttribute Instance instance, Model model) throws Exception {

        instanceService.createInstance("테스트",
                                        new ContainerImage("aitf", "기본 이미지", "hello world"),
                                        5,
                                        true,
                                        LocalDateTime.now().plusHours(15));
        return "/instance/index";
    }

    @GetMapping("/ssh")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity sshTest() throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession("4whomtbts", "210.94.223.123", 8081);
        session.setPassword("Hndp^(%#9!Q");
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();  //연결


        Channel channel = session.openChannel("exec");  //채널접속
        ChannelExec channelExec = (ChannelExec) channel; //명령 전송 채널사용
        channelExec.setPty(true);
        channelExec.setCommand("netstat -tnlp"); //내가 실행시킬 명령어를 입력


        //콜백을 받을 준비.
        StringBuilder outputBuffer = new StringBuilder();
        InputStream in = channel.getInputStream();
        ((ChannelExec) channel).setErrStream(System.err);

        channel.connect();  //실행


        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                outputBuffer.append(new String(tmp, 0, i));
                if (i < 0) break;
            }
            if (channel.isClosed()) {
                System.out.println("결과");
                System.out.println(outputBuffer.toString());
                channel.disconnect();
            }
        }
    }

}
