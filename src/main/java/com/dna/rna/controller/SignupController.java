package com.dna.rna.controller;

import com.dna.rna.domain.User;
import com.dna.rna.persistent.UserRepository;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
public class SignupController {

    private Logger logger= LoggerFactory.getLogger(getClass());
    private static final String DUPLICATE_LOGIN_ID_EXISTS= "중복되는 아이디입니다.";
    private static final String PASSWORD_POLICY_VIOLATION= "패스워드는 규정에 맞춰 정해주세요";

    @Autowired
    private UserRepository userRepository;

    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = DUPLICATE_LOGIN_ID_EXISTS)
    })
    @PostMapping("/signup")
    public ResponseEntity signuo(@RequestBody SignupForm signupForm) {
        String loginId= signupForm.getLoginId();
        String userName= signupForm.getUserName();
        String password= signupForm.getPassword();

        if(userRepository.findUserByLoginId(loginId) != null) {
            return new ResponseEntity(DUPLICATE_LOGIN_ID_EXISTS, HttpStatus.BAD_REQUEST);
        }

        User newUser = new User();
        newUser.setLoginId(loginId);
        newUser.setUserName(userName);
        newUser.setPassword(password);
        userRepository.save(newUser);
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
