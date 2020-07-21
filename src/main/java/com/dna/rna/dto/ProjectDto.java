package com.dna.rna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class ProjectDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Card {

        private long projectId;
        private long remainDay;
        private ClubUserDto.Response mentor;
        private int currentMember;
        private int totalMember;
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean hiring;
    }
}
