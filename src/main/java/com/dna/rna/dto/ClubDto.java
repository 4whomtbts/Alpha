package com.dna.rna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class ClubDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String profileImage;
        private String schoolName;
        private String clubLeader;
        private long currentSeasonUser;
        private long totalUser;
        private String since;
        private String season;
        private String location;
        private BoardItemDto.BoardList boardList;
        private List<ProjectDto.Card> projectCards;
    }
}
