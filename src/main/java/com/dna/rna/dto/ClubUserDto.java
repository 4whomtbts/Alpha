package com.dna.rna.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class ClubUserDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        
        private long clubUserId;
        @ApiModelProperty(example = "1234 // 유저 아이디")
        private long userId;
        @ApiModelProperty(example = "김현준 // 유저 이름")
        private String name;
    }
}
