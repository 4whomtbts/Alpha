package com.dna.rna.dto;

import com.dna.rna.domain.clubBoard.ClubBoard;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClubBoardDto {

    public static List<ClubBoardDto.Response> clubBoardsToClubBoardDtos(List<ClubBoard> clubBoards) {
        List<ClubBoardDto.Response> clubBoardDtos = new ArrayList<>();
        for (int i=0; i < clubBoards.size(); i++) {
            ClubBoard curr = clubBoards.get(i);
            clubBoardDtos.add(
                    new ClubBoardDto.Response(
                            curr.getClubBoardId(),
                            curr.getBoardGroup().getBoardGroupId(),
                            curr.getBoard().getBoardName(),
                            curr.getDisplayOrder()));
        }
        return clubBoardDtos;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        @ApiModelProperty(example = "1234")
        private long clubBoardId;
        @ApiModelProperty(example = "5678")
        private long boardGroupId;
        @ApiModelProperty(example = "게시판 이름")
        private String boardName;
        @ApiModelProperty(example = "0 과 양의 정수")
        private int displayOrder;
    }
}
