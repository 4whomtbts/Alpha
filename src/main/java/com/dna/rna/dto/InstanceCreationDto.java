package com.dna.rna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class InstanceCreationDto {

    private String instanceUUID;
    private String instanceHash;

}
