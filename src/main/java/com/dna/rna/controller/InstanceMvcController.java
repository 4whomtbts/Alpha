package com.dna.rna.controller;

import com.dna.rna.domain.containerImage.ContainerImage;
import com.dna.rna.domain.containerImage.ContainerImageRepository;
import com.dna.rna.domain.instance.Instance;
import com.dna.rna.domain.instance.InstanceRepository;
import com.dna.rna.domain.server.ServerRepository;
import com.dna.rna.domain.user.User;
import com.dna.rna.domain.user.UserRepository;
import com.dna.rna.dto.InstanceDto;
import com.dna.rna.dto.ServerPortDto;
import com.dna.rna.exception.DCloudException;
import com.dna.rna.service.InstanceService;
import com.dna.rna.service.util.DCloudError;
import com.dna.rna.service.util.SshExecutor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@Controller
public class InstanceMvcController {

    private static final Logger logger = LoggerFactory.getLogger(InstanceMvcController.class);

    private final InstanceRepository instanceRepository;
    private final ContainerImageRepository containerImageRepository;
    private final InstanceService instanceService;
    private final UserRepository userRepository;
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private final ServerRepository serverRepository;
    private final SshExecutor sshExecutor;

    private InstanceDto.Post instance;

    public InstanceMvcController(InstanceRepository instanceRepository, UserRepository userRepository,
                                 ServerRepository serverRepository, ContainerImageRepository containerImageRepository, InstanceService instanceService, SshExecutor sshExecutor) {
        this.instanceRepository = instanceRepository;
        this.serverRepository = serverRepository;
        this.userRepository = userRepository;
        this.containerImageRepository = containerImageRepository;
        this.instanceService = instanceService;
        this.instance = new InstanceDto.Post();
        this.sshExecutor = sshExecutor;
    }

    @Secured(value = {"ROLE_MEMBER", "ROLE_ADMIN"})
    @Transactional
    @GetMapping("/instances")
    public String instanceGET(Principal principal, Model model) throws Exception {
        final List<Instance> instanceList = instanceRepository.findByUserLoginId(principal.getName());
        List<InstanceDto> instanceDtoList = Collections.synchronizedList(new ArrayList<>());
        List<Future<String>> futureList = new ArrayList<>();
        for (Instance instance : instanceList) {
            futureList.add(executor.submit(() -> sshExecutor.fetchStatusOfInstance(instance.getServer(),
                    instance.getInstanceContainerId())));
        }

        for (int i = 0; i < instanceList.size(); i++) {
            Instance instance = instanceList.get(i);
            String status;
            Future<String> fetchedStatus = futureList.get(i);
            InstanceDto instanceDto = instance.toInstanceDto();
            boolean normalStatus = false;
            try {
                status = fetchedStatus.get(3000, TimeUnit.MILLISECONDS);
                if (status.equals("") || status.equals("STATUS\r\n")) {
                    status = "서버에러 발생";
                } else if (status.contains("Up")) {
                    normalStatus = true;
                } else if (status.contains("Created")) {
                    status = "컨테이너가 생성되었으나 정상적으로 실행되지 않았습니다.";
                }
            } catch (ExecutionException e) {
                status = "서버에러 발생";
            } catch (TimeoutException e) {
                status = "서버에러 발생";
                fetchedStatus.cancel(true);
            }
            instanceDto.setStatus(status);
            instanceDto.setNormalStatus(normalStatus);
            instanceDtoList.add(instanceDto);
        }
        instanceRepository.saveAll(instanceList);
        model.addAttribute("instanceList", instanceDtoList);
        return "instances/index";
    }

    @Secured(value = {"ROLE_MEMBER", "ROLE_ADMIN"})
    @GetMapping("/instance/create")
    public String instanceCreateGET(Principal principal, Model model) {
        List<ContainerImage> containerImageList = containerImageRepository.findAll();
        model.addAttribute("instance", instance);
        model.addAttribute("containerImageList", containerImageList);
        return "instances/instance/create";
    }

    @Secured(value = {"ROLE_MEMBER", "ROLE_ADMIN"})
    @RequestMapping(value = "/instance/create", params={"addExternalPort"})
    public String instanceCreateAddExternalPorts(Principal principal, @ModelAttribute("instance") InstanceDto.Post instance, Model model) {
        this.instance = instance;
        this.instance.getExternalPorts().add(new ServerPortDto.Creation());
        return "redirect:/instance/create";
    }


    @RequestMapping(value = "/instance/create", params={"removeExternalPort"})
    public String instanceCreateRemoveExternalPorts(@ModelAttribute("instance") InstanceDto.Post instance, HttpServletRequest request, BindingResult bindingResult,
                                                    final HttpServletRequest req) {
        final Integer rowId = Integer.valueOf(req.getParameter("removeExternalPort"));
        instance.getExternalPorts().remove(rowId.intValue());
        return "instances/instance/create";
    }

    @RequestMapping(value = "/instance/create", params={"addInternalPorts"})
    public String instanceCreateAddInternalPorts(final InstanceDto.Post instance, final BindingResult bindingResult,
                                                 final HttpServletRequest req) {
        final Integer rowId = Integer.valueOf(req.getParameter("addInternalPorts"));
        instance.getExternalPorts().add(new ServerPortDto.Creation());
        return "instances/instance/create";

    }

    @Transactional
    @PostMapping("/instance/create")
    public String instanceCreatePOST(@ModelAttribute("instance") InstanceDto.Post instance, Principal principal, Model model) throws Exception {

        User owner = userRepository.findUserByLoginId(principal.getName());
        instance.setOwner(owner);

        if (instance.getSudoerId() == null || instance.getSudoerId().length() < 4) {
            logger.warn("유저 [{}, {}] 가 입력한 sudoerId 가 [{}] 로 4자 보다 짧습니다 ",
                    owner.getLoginId(), owner.getUserName(), instance.getSudoerId());
            return "instances/instance/create";
        }
        if (instance.getSudoerPwd() == null || instance.getSudoerPwd().length() < 8) {
            logger.warn("유저 [{}, {}] 가 입력한 sudoer 비밀번호가 [{}] 로 8자 보다 짧습니다 ",
                    owner.getLoginId(), owner.getUserName(), instance.getSudoerPwd());
            return "instances/instance/create";
        }

        new Thread(() -> {
            LocalDateTime expiredAt = null;
            if (!instance.isIndefinitelyUse()) {
                expiredAt = LocalDateTime.now().plusHours(instance.getReserveHour());
            }
            String newInstanceUUID = owner.getLoginId() + "-" + UUID.randomUUID().toString();
            try {
                DCloudError error =  instanceService.createInstance(newInstanceUUID, instance, expiredAt);
                if (error != null) {
                    File logFile = new File("./" + newInstanceUUID);
                    try(BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                        writer.write(error.toString());
                    }  catch (IOException e) {
                        logger.error(
                                String.format("[%s] 인스턴스의 오류를 기록하는 도중 IOException 발생 : 원래 로그 메세지 [%s]",
                                        newInstanceUUID,  error));
                    }
                }
            } catch (Exception e) {
                String message = String.format("[%s] createInstance 과정 중에 예외가 발생했습니다 [%s]", newInstanceUUID, ExceptionUtils.getStackTrace(e));
                logger.error(message);
                throw DCloudException.ofInternalServerError(message);
            }
        }).run();

        return "instances/index";
    }


}
