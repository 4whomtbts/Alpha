package com.dna.rna;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * swagger configuration class..
 *
 * SwaggerConfig.java
 *
 * created 2020.3.25
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("RNA")
                .description("RNA swagger")
                .termsOfServiceUrl("http://github.com")
                .contact("4whomtbts@gmail.com")
                .license("Apache License Version 2.0")
                .licenseUrl("")
                .version("0.1")
                .build();
    }
}

