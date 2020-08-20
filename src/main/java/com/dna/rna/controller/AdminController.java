package com.dna.rna.controller;

import com.dna.rna.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;

    @PostMapping("/allow_codes/allow_code")
    public void issueAllowCode() throws UnsupportedEncodingException {
        adminService.generateNnewAllowCodeAndSave(50);
    }

    @ResponseBody
    @GetMapping(value = "/allow_codes/unexpired", produces = "text/plain;charset=UTF-8")
    public String getRandomUnexpired(HttpServletResponse response) throws UnsupportedEncodingException {
        return adminService.getRandomUnExpiredAllowCodeAndExpiresIt();
    }
}
