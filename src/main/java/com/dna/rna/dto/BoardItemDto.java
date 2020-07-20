package com.dna.rna.dto;

import com.dna.rna.domain.ClubBoard.ClubBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

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
        private long id;
        private BoardItemType boardItemType;
        private String boardItemName;
        private List<ClubBoard> nestedItems;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class BoardList {
        private List<Item> boardItems;
    }

}
