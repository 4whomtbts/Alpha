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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for signup api and related apis.
 *
 * SignupController.java
 *
 * created 2020.3.25
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */


@Api(value ="Singup 컨트롤러 v1")
@RestController
@RequiredArgsConstructor
public class RootController {

    private static final Logger logger= LoggerFactory.getLogger(UserController.class);

    private static final String DUPLICATE_LOGIN_ID_EXISTS= "중복되는 아이디입니다.";
    private static final String PASSWORD_POLICY_VIOLATION= "패스워드는 규정에 맞춰 정해주세요";

    private final UserRepository userRepository;
    private final ContainerImageRepository containerImageRepository;
    private final InstanceRepository instanceRepository;
    private final ServerRepository serverRepository;
    private final UserService userService;
    private final SigninService signinService;




}

