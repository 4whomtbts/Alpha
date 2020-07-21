package com.dna.rna.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
public class ProjectDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Card {

        private long projectId;
        @ApiModelProperty(example = "3 // 모집마감까지 남은 날짜, 0 이상의 정수이며 -1 일 때는 모집중이 아님")
        private long remainDay;
        private ClubUserDto.Response mentor;
        @ApiModelProperty(example = "1 // 현재 스터디 참가인원")
        private int currentMember;
        @ApiModelProperty(example = "1 // 스터디 최대인원")
        private int totalMember;
        @ApiModelProperty(example = "2020.02.18 // 첫 스터디 일자")
        private LocalDate startDate;
        @ApiModelProperty(example = "2020.06.18 // 스터디 종료일자")
        private LocalDate endDate;
        @ApiModelProperty(example = "true or false // 현재 모집진행 여부")
        private boolean hiring;
    }
}
