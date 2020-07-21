package com.dna.rna.controller;

import com.dna.rna.domain.User.User;
import com.dna.rna.domain.User.UserRepository;
import com.dna.rna.domain.User.UserRepositoryImpl;
import com.dna.rna.service.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for signup api and related apis.
 *
 * SignupController.java
 *
 * created 2020.3.25
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@RestController
@RequiredArgsConstructor
public class SignupController {

    private static final Logger logger= LoggerFactory.getLogger(UserController.class);

    private static final String DUPLICATE_LOGIN_ID_EXISTS= "중복되는 아이디입니다.";
    private static final String PASSWORD_POLICY_VIOLATION= "패스워드는 규정에 맞춰 정해주세요";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = DUPLICATE_LOGIN_ID_EXISTS)
    })
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody SignupForm signupForm) {
        String loginId= signupForm.getLoginId();
        String userName= signupForm.getUserName();
        String encodedPassword= passwordEncoder.encode(signupForm.getPassword());
        logger.info("DECODED PASSWORD [ {} ] ", encodedPassword);
        if(userRepository.findUserByLoginId(loginId) != null) {
            return new ResponseEntity(DUPLICATE_LOGIN_ID_EXISTS, HttpStatus.BAD_REQUEST);
        }

        User user = userService.createUser(loginId, userName, encodedPassword);
        logger.info("New user '{}' has been created.", user.getLoginId());
        return new ResponseEntity(HttpStatus.OK);
    }


}

@Getter
@Setter
class SignupForm {

    private String loginId;
    private String userName;
    private String password;

}
