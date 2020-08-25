package com.dna.rna.controller;

import com.dna.rna.domain.group.Group;
import com.dna.rna.domain.instance.Instance;
import com.dna.rna.domain.user.User;
import com.dna.rna.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminMvcController {

    private static final Logger logger = LoggerFactory.getLogger(AdminMvcController.class);

    private final AdminService adminService;


    @Secured(value = {"ROLE_ADMIN"})
    @GetMapping("/admin")
    @Transactional
    public String adminMain(Model model) {
        List<User> userList = adminService.fetchUserList();
        List<Group> groupList = adminService.fetchAllGroup();
        List<Instance> instanceList = adminService.fetchAllInstance();
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


        model.addAttribute("instanceList", instanceList);
        model.addAttribute("userList", userList);
        model.addAttribute("isMemberList", isMemberList);
        model.addAttribute("groupList", groupList);
        model.addAttribute("isGroupCreationConfirmedList", isGroupCreationConfirmedList);
        return "admin/index";
    }
}
