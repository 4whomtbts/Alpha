package com.dna.rna.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class ClubDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {

        @ApiModelProperty(example = "JPG,JPEG의 바이너리")
        private String profileImage;
        @ApiModelProperty(example = "학교 명")
        private String schoolName;
        @ApiModelProperty(example = "동아리 장 이름(loginId 아님)")
        private String clubLeader;
        @ApiModelProperty(example = "현재 기수의 활동인원")
        private long currentSeasonUser;
        @ApiModelProperty(example = "설립이래 누적 활동인원")
        private long totalUser;
        @ApiModelProperty(example = "설립일자")
        private String since;
        @ApiModelProperty(example = "현재 기수")
        private String season;
        @ApiModelProperty(example = "동아리 위치")
        private String location;
        private BoardItemDto.BoardList boardList;
        private List<ProjectDto.Card> projectCards;
    }
}
