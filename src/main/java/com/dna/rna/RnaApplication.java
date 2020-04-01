package com.dna.rna;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RnaApplication {

    @RequestMapping("/")
    public String home() {
        return "hello docker spring boot";
    }

    public static void main(String[] args) {
        SpringApplication.run(RnaApplication.class, args);
    }

}
