package com.dna.rna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ServerDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class AdminResponse {
        private int serverNum;
        private int exclusivelyUsedGpu;
        private int sshPort;
        private int minExternalPort;
        private boolean excluded;
    }
}
