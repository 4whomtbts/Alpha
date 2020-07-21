package com.dna.rna.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class BoardItemDto {

    public enum BoardItemType {
           GROUP("GROUP"),
           BOARD("BOARD");

           private String itemType;

           BoardItemType(String itemType) {
                this.itemType = itemType;
           }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Item {
        @ApiModelProperty(example = "BoardGroup 혹은 Board의 Id")
        private long id;
        @ApiModelProperty(example = "GROUP은 그룹(카테고리), BOARD는 게시판")
        private BoardItemType boardItemType;
        @ApiModelProperty(example = "카테고리 혹은 게시판 이름")
        private String boardItemName;
        private List<ClubBoardDto.Response> nestedItems;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class BoardList {
        private List<Item> boardItems;
    }

}
