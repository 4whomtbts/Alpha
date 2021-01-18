package com.dna.rna.dto;

import lombok.Getter;
import lombok.Setter;

public class GroupDto {

    @Getter
    @Setter
    public static class GroupCreation {
        private String groupName;
        private String representative;
    }

}
