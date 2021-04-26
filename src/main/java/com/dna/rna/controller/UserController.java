package com.dna.rna.controller;

import com.dna.rna.DCloudResponse;
import com.dna.rna.domain.user.User;
import com.dna.rna.domain.user.UserType;
import com.dna.rna.security.MainUserDetails;
import com.dna.rna.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @PreAuthorize("isAuthenticated() and hasRole('UESR')")
    @ResponseBody
    @GetMapping("/user")
    public String getUser() {
        String result = userService.getUserRoles("jun");
        return result;
    }

    @ResponseBody
    @DeleteMapping("/users/user/{userId}")
    public ResponseEntity<DCloudResponse> deleteUser(Authentication authentication, @PathVariable("userId") long userId) {
        MainUserDetails details = (MainUserDetails) authentication.getPrincipal();
        User user = details.getUser();
        boolean isAdmin = user.getUserType().equals(UserType.ADMIN);
        if (user.getId() == userId || isAdmin) {
            userService.deleteUser(userId);
            return new ResponseEntity<>(DCloudResponse.ofSuccess("삭제가 성공적으로 완료되었습니다!"), HttpStatus.OK);
        }

        return new ResponseEntity<>(
                DCloudResponse.ofFailure(
                        "UNAUTHORIZED",
                        "권한이 없습니다",
                        String.format("userId = %s 삭제를- 요청한 userId %s 는 동일한 유저 혹은 관리자가 아닙니다.",
                                userId, user.getId())), HttpStatus.UNAUTHORIZED);
    }
}
