package com.dna.rna.controller;

import com.dna.rna.domain.groupUser.GroupUser;
import com.dna.rna.domain.groupUser.GroupUserType;
import com.dna.rna.domain.user.User;
import com.dna.rna.security.MainUserDetails;
import com.dna.rna.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GroupMvcController {

    private final GroupService groupService;

    @Secured(value = {"ROLE_MEMBER", "ROLE_ADMIN"})
    @GetMapping("/group")
    public String groupGET(Model model, Authentication authentication) {
        MainUserDetails details = (MainUserDetails) authentication.getPrincipal();
        User user = groupService.getUser(details.getUser());
        List<GroupUser> groupUserList = groupService.getUserGroups(user);
        List<List<GroupUser>> notAcceptedMemberLists = new ArrayList<>();
        List<List<GroupUser>> memberLists = new ArrayList<>();
        List<Boolean> isAdminLists = new ArrayList<>();
        for (int i=0; i < groupUserList.size(); i++) {
            isAdminLists.add(false);
        }
        for (int i=0; i < groupUserList.size(); i++) {
            List<GroupUser> memberList = groupUserList.get(i).getGroup().getMembers();
            notAcceptedMemberLists.add(new ArrayList<>());
            memberLists.add(new ArrayList<>());

            for (int j=0; j < memberList.size(); j++) {
                GroupUser curr = memberList.get(j);
                if (curr.getUser().getLoginId().equals(user.getLoginId())) {
                    isAdminLists.set(i, true);
                }
                GroupUserType type = curr.getGroupUserType();
                if (type == GroupUserType.MANAGER || type == GroupUserType.MEMBER) {
                    memberLists.get(i).add(curr);
                } else {
                    notAcceptedMemberLists.get(i).add(curr);
                }
            }
        }

        model.addAttribute("groups", groupUserList);
        model.addAttribute("notAcceptedMemberLists", notAcceptedMemberLists);
        model.addAttribute("memberLists", memberLists);
        model.addAttribute("isAdminLists", isAdminLists);
        return "/group/index";
    }
}
