package com.dna.rna.controller;

import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.containerImage.ContainerImage;
import com.dna.rna.domain.containerImage.ContainerImageRepository;
import com.dna.rna.domain.instance.Instance;
import com.dna.rna.domain.instance.InstanceRepository;
import com.dna.rna.domain.server.Server;
import com.dna.rna.domain.server.ServerRepository;
import com.dna.rna.dto.InstanceDto;
import com.dna.rna.dto.ServerPortDto;
import com.dna.rna.service.InstanceService;
import com.dna.rna.service.util.SshExecutor;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

@Controller
public class InstanceController {

    private static final Logger logger = LoggerFactory.getLogger(InstanceController.class);

    private final InstanceRepository instanceRepository;
    private final ContainerImageRepository containerImageRepository;
    private final InstanceService instanceService;
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private final ServerRepository serverRepository;
    private InstanceDto.Post instance;

    public InstanceController(InstanceRepository instanceRepository, ServerRepository serverRepository, ContainerImageRepository containerImageRepository, InstanceService instanceService) {
        this.instanceRepository = instanceRepository;
        this.serverRepository = serverRepository;
        this.containerImageRepository = containerImageRepository;
        this.instanceService = instanceService;
        this.instance = new InstanceDto.Post();
    }

    public static class InstanceDtoCreationJob implements Callable<Integer> {
        int index;

        public InstanceDtoCreationJob(int index) {
            this.index = index;
        }

        @Override
        public Integer call() throws Exception {
            return index + 1;
        }
    }

    @Transactional
    @GetMapping("/instance")
    public String instanceGET(Model model) throws Exception {
        final List<Instance> instanceList = instanceRepository.findAll();
        List<InstanceDto> instanceDtoList = Collections.synchronizedList(new ArrayList<>());
        List<Future<String>> futureList = new ArrayList<>();
        for (Instance instance : instanceList) {
            futureList.add(executor.submit(() -> SshExecutor.fetchStatusOfInstance(instance.getServer(),
                    instance.getInstanceContainerId())));
        }

        for (int i = 0; i < instanceList.size(); i++) {
            Instance instance = instanceList.get(i);
            String status;
            Future<String> fetchedStatus = futureList.get(i);
            InstanceDto instanceDto = instance.toInstanceDto();
            try {
                status = fetchedStatus.get(3000, TimeUnit.MILLISECONDS);
            } catch (ExecutionException e) {
                status = "서버에러 발생";
            } catch (TimeoutException e) {
                status = "서버에러 발생";
                fetchedStatus.cancel(true);
            }
            instanceDto.setStatus(status);
            instanceDtoList.add(instanceDto);
        }
        instanceRepository.saveAll(instanceList);
        model.addAttribute("instanceList", instanceDtoList);
        return "/instance/index";
    }

    @RequestMapping("/instance/create")
    public String instanceCreateGET(Model model) {
        List<ContainerImage> containerImageList = containerImageRepository.findAll();
        model.addAttribute("instance", instance);
        model.addAttribute("containerImageList", containerImageList);
        return "/instance/create";
    }

    @RequestMapping(value = "/instance/create", params={"addExternalPort"})
    public String instanceCreateAddExternalPorts(@ModelAttribute("instance") InstanceDto.Post instance, Model model) {
        this.instance = instance;
        this.instance.getExternalPorts().add(new ServerPortDto.Creation());
        return "redirect:/instance/create";
    }


    @RequestMapping(value = "/instance/create", params={"removeExternalPort"})
    public String instanceCreateRemoveExternalPorts(@ModelAttribute("instance") InstanceDto.Post instance, HttpServletRequest request, BindingResult bindingResult,
                                                    final HttpServletRequest req) {
        final Integer rowId = Integer.valueOf(req.getParameter("removeExternalPort"));
        instance.getExternalPorts().remove(rowId.intValue());
        return "/instance/create";
    }

    @RequestMapping(value = "/instance/create", params={"addInternalPorts"})
    public String instanceCreateAddInternalPorts(final InstanceDto.Post instance, final BindingResult bindingResult,
                                                 final HttpServletRequest req) {
        final Integer rowId = Integer.valueOf(req.getParameter("addInternalPorts"));
        instance.getExternalPorts().add(new ServerPortDto.Creation());
        return "/instance/create";

    }

    @Transactional
    @PostMapping("/instance/create")
    public String instanceCreatePOST(@ModelAttribute("instance") InstanceDto.Post instance, Model model) throws Exception {

        ContainerImage selectedImage = containerImageRepository.findById(instance.getContainerImageId()).orElseThrow();

        LocalDateTime expiredAt = null;
        if (!instance.isIndefinitelyUse()) {
            expiredAt = LocalDateTime.now().plusHours(instance.getReserveHour());
        }
        instanceService.createInstance(
                instance.getInstanceName(), selectedImage, instance.getNumberOfGpuToUse(),
                instance.isUseGpuExclusively(), instance.getExternalPorts(), instance.getInternalPorts(), expiredAt);
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
