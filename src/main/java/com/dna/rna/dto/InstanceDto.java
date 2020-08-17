package com.dna.rna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InstanceDto {

    private String instanceContainerId;
    private String instanceHash;
    private String instanceName;
    private String containerImageNickName;
    private String status;
    private String resources;
    private String internalIP;
    private String externalIP;
    private String externalPorts;
    private String internalPorts;
    private String period;

}
