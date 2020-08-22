package com.dna.rna.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {

    @GetMapping("/mypage")
    public String mypageGETPage() {
        return "/mypage/index";

    }
}
