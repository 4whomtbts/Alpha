package com.dna.rna.controller;

import com.dna.rna.domain.allowCode.AllowCodeType;
import com.dna.rna.domain.user.User;
import com.dna.rna.dto.ApiResponse;
import com.dna.rna.security.MainUserDetails;
import com.dna.rna.service.AdminService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;


    @GetMapping("/admin")
    public String adminMain(Model model) {
        List<User> userList = adminService.fetchUserList();
        model.addAttribute("userList", userList);
        return "admin/index";
    }

    @Getter
    @Setter
    class AllowCodeGenerateDto {
        private String loginId;
    }

    @ResponseBody
    @PostMapping(value = "/admin/allow_codes/allow_code/register")
    public ResponseEntity issueAllowCode(@RequestBody final AllowCodeGenerateDto body) throws UnsupportedEncodingException {
       ApiResponse response = new ApiResponse(adminService.generateSignUpAllowCode(body.getLoginId(), AllowCodeType.ACCOUNT_REGISTRATION));
       return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "admin/allow_codes/unexpired", produces = "text/plain;charset=UTF-8")
    public String getRandomUnexpired(HttpServletResponse response) throws UnsupportedEncodingException {
        return adminService.getRandomUnExpiredAllowCodeAndExpiresIt();
    }





}
