package com.dna.rna.controller;

import com.dna.rna.domain.group.Group;
import com.dna.rna.domain.groupUser.GroupUser;
import com.dna.rna.domain.groupUser.GroupUserType;
import com.dna.rna.domain.user.User;
import com.dna.rna.dto.ApiResponse;
import com.dna.rna.dto.GroupDto;
import com.dna.rna.security.MainUserDetails;
import com.dna.rna.service.GroupService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/group")
    public String groupGET(Model model, Authentication authentication) {
        MainUserDetails details = (MainUserDetails) authentication.getPrincipal();
        User user = details.getUser();
        List<GroupUser> groupUserList = groupService.getUserGroups(user);
        List<List<GroupUser>> notAcceptedMemberLists = new ArrayList<>();
        List<List<GroupUser>> memberLists = new ArrayList<>();
        for (int i=0; i < groupUserList.size(); i++) {
            List<GroupUser> memberList = groupUserList.get(i).getGroup().getMembers();
            notAcceptedMemberLists.add(new ArrayList<>());
            memberLists.add(new ArrayList<>());
            for (int j=0; j < memberList.size(); j++) {
                GroupUser curr = memberList.get(j);
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
        return "/group/index";
    }

    @PostMapping("/groups/group")
    public ResponseEntity<ApiResponse> createNewGroup(@RequestBody GroupDto.GroupCreation groupCreation, Authentication authentication) {
        MainUserDetails details = (MainUserDetails) authentication.getPrincipal();
        User user = details.getUser();
        groupService.createNewGroup(user, groupCreation);
        return new ResponseEntity<ApiResponse>(HttpStatus.OK);

    }
}
