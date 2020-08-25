package com.dna.rna.controller;

import com.dna.rna.domain.user.User;
import com.dna.rna.dto.ApiResponse;
import com.dna.rna.dto.GroupDto;
import com.dna.rna.security.MainUserDetails;
import com.dna.rna.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @Secured(value = {"ROLE_MEMBER", "ROLE_ADMIN"})
    @PostMapping("/groups/group")
    public ResponseEntity<ApiResponse> createNewGroup(@RequestBody GroupDto.GroupCreation groupCreation, Authentication authentication) {
        MainUserDetails details = (MainUserDetails) authentication.getPrincipal();
        User user = details.getUser();
        groupService.createNewGroup(user, groupCreation);
        return new ResponseEntity<ApiResponse>(ApiResponse.OK(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_MEMBER", "ROLE_ADMIN"})
    @PostMapping("/groups/group/{groupId}/invite/{loginId}")
    public ResponseEntity<ApiResponse> inviteUserToGroup(@PathVariable long groupId,
                                                         @PathVariable String loginId, Authentication authentication) {
        MainUserDetails details = (MainUserDetails) authentication.getPrincipal();
        User user = details.getUser();

        groupService.inviteUserToGroup(groupId, loginId);
        return new ResponseEntity<ApiResponse>(ApiResponse.OK(), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_MEMBER", "ROLE_ADMIN"})
    @PostMapping("/groups/group/{groupId}/confirm")
    public ResponseEntity<ApiResponse> confirmGroupCreation(@Param("groupID") long groupId, Authentication authentication) {
        MainUserDetails details = (MainUserDetails) authentication.getPrincipal();
        User user = details.getUser();
        if (user == null || groupService.getUser(user) == null) {
            return new ResponseEntity<ApiResponse>(ApiResponse.UNAUTHORIZED(), HttpStatus.UNAUTHORIZED);
        }
        groupService.confirmGroupCreation(groupId);
        return new ResponseEntity<ApiResponse>(ApiResponse.OK(), HttpStatus.OK);
    }

}
