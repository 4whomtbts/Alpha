package com.dna.rna.controller;

import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.containerImage.ContainerImage;
import com.dna.rna.domain.containerImage.ContainerImageRepository;
import com.dna.rna.domain.instance.Instance;
import com.dna.rna.domain.instance.InstanceRepository;
import com.dna.rna.domain.server.Server;
import com.dna.rna.domain.server.ServerRepository;
import com.dna.rna.domain.user.User;
import com.dna.rna.domain.user.UserRepository;
import com.dna.rna.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
@Controller
@RequiredArgsConstructor
public class RootController {

    private static final Logger logger= LoggerFactory.getLogger(UserController.class);

    private static final String DUPLICATE_LOGIN_ID_EXISTS= "중복되는 아이디입니다.";
    private static final String PASSWORD_POLICY_VIOLATION= "패스워드는 규정에 맞춰 정해주세요";

    private final UserRepository userRepository;
    private final ContainerImageRepository containerImageRepository;
    private final InstanceRepository instanceRepository;
    private final ServerRepository serverRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @GetMapping("/")
    public String index() {
        return "/index";
    }

    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = DUPLICATE_LOGIN_ID_EXISTS)
    })

    @GetMapping("/signup")
    public String singupGET() {
        return "/signup";
    }

    @PostMapping("/signup")
    public String signupPOST(SignupForm signupForm) {
        String loginId= signupForm.getLoginId();
        signupForm.setPassword(passwordEncoder.encode(signupForm.getPassword()));
        if(userRepository.findUserByLoginId(loginId) != null) {
            return "/denied";
        }

        User user = signupForm.toUserEntity();
        userRepository.save(user);

        logger.info("New user '{}' has been created.", user.getLoginId());
        return "/login";
    }

    @GetMapping("/login")
    public String loginGET() {
        return "/login";
    }

    @GetMapping("/init")
    public String initGET() {
        List<Server> servers = new ArrayList<>();
        for (int i=0; i < 6; i++) {
            Server server = new Server(i+1, "192.168.1.1" + (i+1), 8081 + i, new ServerResource());
            serverRepository.save(server);
            servers.add(server);
        }
        ContainerImage containerImage = new ContainerImage("aitf", "기본 이미지", "ssh, xrdp, jupyter");
        containerImageRepository.save(containerImage);
        /*
        Instance instance = new Instance("test", "a", "b", containerImage, servers.get(0), new ServerResource(), null, LocalDateTime.now().plusDays(10));
        Instance instance1 = new Instance("test", "c", "d", containerImage, servers.get(0), new ServerResource(), null, null);
        instanceRepository.save(instance);
        instanceRepository.save(instance1);
        */
        return "/";
    }

}

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class SignupForm {

    private Long id;
    private String loginId;
    private String userName;
    private String password;
    private String organization;
    private String userGroup;
    private String permitCode;

    public User toUserEntity() {
        return User.of(this.loginId, this.userName, this.password,
                       this.organization, this.userGroup);
    }

}
