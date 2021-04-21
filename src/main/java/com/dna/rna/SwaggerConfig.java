package com.dna.rna;

import com.dna.rna.service.util.PageRequest;
import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
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

// 참고 : https://victorydntmd.tistory.com/341
@EnableAsync
@EnableSwagger2
@Configuration
public class SwaggerConfig extends WebMvcConfigurationSupport {
    @Bean
    public Docket api(TypeResolver typeResolver) {
        return new Docket(DocumentationType.SWAGGER_2)
                .alternateTypeRules(
                        AlternateTypeRules.newRule(
                                typeResolver.resolve(PageRequest.class),
                                typeResolver.resolve(Page.class)))
                .apiInfo(apiInfo()) //기본정보
                .select()
                .apis(RequestHandlerSelectors.any())  //주소접근 옵션
                .paths(PathSelectors.any())
                .build();
    }

    @Getter
    @Setter
    @ApiModel
    // 참고 : https://blog.jiniworld.me/20
    static class Page {
        @ApiModelProperty(value = "페이지 번호(0..N)")
        private Integer page;

        @ApiModelProperty(value = "한 페이지에 글 갯수")
        private Integer size;

        @ApiModelProperty(value = "정렬기준칼럼 sort=createdAt 혹은 sort=articleId 형태")
        private String sort;

        @ApiModelProperty(value = "정렬방법(사용법: ASC 혹은 DESC)")
        private Sort.Direction direction;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("DCloud Swagger")
                .description("")
                .termsOfServiceUrl("http://www-03.ibm.com/software/sla/sladb.nsf/sla/bm?Open")
                .contact("4whomtbts@gmail.com")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/IBM-Bluemix/news-aggregator/blob/master/LICENSE")
                .version("1.0")
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}



