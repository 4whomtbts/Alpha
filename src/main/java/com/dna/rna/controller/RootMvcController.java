package com.dna.rna.controller;

import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.containerImage.ContainerImage;
import com.dna.rna.domain.containerImage.ContainerImageRepository;
import com.dna.rna.domain.instance.InstanceRepository;
import com.dna.rna.domain.server.Server;
import com.dna.rna.domain.server.ServerRepository;
import com.dna.rna.domain.user.UserRepository;
import com.dna.rna.dto.SignupForm;
import com.dna.rna.exception.DCloudException;
import com.dna.rna.service.SigninService;
import com.dna.rna.service.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RootMvcController {

    private static final Logger logger= LoggerFactory.getLogger(UserController.class);

    private static final String DUPLICATE_LOGIN_ID_EXISTS= "중복되는 아이디입니다.";

    private final SigninService signinService;

    @GetMapping("/topNav")
    public String topNav() {
        return "topNav";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = DUPLICATE_LOGIN_ID_EXISTS)
    })

    @GetMapping("/signup")
    public String singupGET() {
        return "signup";
    }

    @PostMapping(value = "/signup")
    public String signupPOST(@RequestBody SignupForm signupForm) {
        try {
            signinService.SignUp(signupForm);
        } catch (DCloudException exception) {
            return "signup";
        }
        return "login";
    }

    @GetMapping("/login")
    public String loginGET() {
        return "login";
    }

}
