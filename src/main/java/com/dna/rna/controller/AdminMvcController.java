package com.dna.rna.controller;

import com.dna.rna.domain.group.Group;
import com.dna.rna.domain.instance.Instance;
import com.dna.rna.domain.server.Server;
import com.dna.rna.domain.user.User;
import com.dna.rna.dto.InstanceDto;
import com.dna.rna.service.AdminService;
import com.dna.rna.service.util.SshExecutor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

@Controller
@RequiredArgsConstructor
public class AdminMvcController {

    private static final Logger logger = LoggerFactory.getLogger(AdminMvcController.class);

    private final AdminService adminService;
    private final SshExecutor sshExecutor;
    private final static ExecutorService executor = Executors.newFixedThreadPool(10);

    @Secured(value = {"ROLE_ADMIN"})
    @GetMapping("/admin")
    @Transactional
    public String adminMain(Model model) throws InterruptedException {
        List<User> userList = adminService.fetchUserList();
        List<Group> groupList = adminService.fetchAllGroup();
        List<Boolean> isMemberList = new ArrayList<>();
        List<Boolean> isGroupCreationConfirmedList = new ArrayList<>();
        for (int i=0; i < userList.size(); i++) {
            User user = userList.get(i);
            for (int j=0; j < user.getGroupUserList().size(); j++) {
                user.getGroupUserList().get(j);
            }
            boolean isMember = false;
            for (int j=0; j < user.getUserRoles().size(); j++) {
                if (user.getUserRoles().get(j).getRoleName().equals("MEMBER")) {
                    isMember = true;
                    break;
                }
            }
            isMemberList.add(isMember);
        }

        for (int i=0; i < groupList.size(); i++) {
            boolean isConfirmed = false;
            if (groupList.get(i).getGroupStatus() == Group.CONFIRMED) {
                isConfirmed = true;
            }
            isGroupCreationConfirmedList.add(isConfirmed);
        }

        List<Server> serverList = adminService.fetchAllServer();

        List<Instance> instanceList = adminService.fetchAllInstance();
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
            try {
                status = fetchedStatus.get(5000, TimeUnit.MILLISECONDS);
                if (status.equals("") || status.equals("STATUS\r\n")) {
                    status = "서버에러 발생";
                }
            } catch (ExecutionException e) {
                status = "서버에러 발생";
            } catch (TimeoutException e) {
                status = "서버에러 발생";
                fetchedStatus.cancel(true);
            }
            instanceDto.setStatus(status);
            instanceDtoList.add(instanceDto);
        }
        model.addAttribute("userList", userList);
        model.addAttribute("groupList", groupList);
        model.addAttribute("isMemberList", isMemberList);
        model.addAttribute("serverList", serverList);
        model.addAttribute("instanceList", instanceDtoList);
        model.addAttribute("isGroupCreationConfirmedList", isGroupCreationConfirmedList);
        return "admin/index";
    }
}
