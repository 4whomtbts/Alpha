package com.dna.rna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ClubUserDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private long clubUserId;
        private long userId;
        private String name;
    }
}
