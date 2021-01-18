package com.dna.rna.dto;

import com.dna.rna.domain.containerImage.ContainerImage;
import com.dna.rna.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class InstanceDto {

    private long instanceId;
    private String instanceContainerId;
    private String instanceHash;
    private String instanceName;
    private String containerImageNickName;
    private String status;
    private boolean normalStatus;
    private String resources;
    private String internalIP;
    private String externalIP;
    private String externalPorts;
    private String internalPorts;
    private String period;
    private boolean error;
    private boolean initialized;
    private String instanceLog;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Post {
        private String instanceName;
        private User owner;
        private String sudoerId;
        private String sudoerPwd;
        private String purpose;
        private Long containerImageId;
        private int reserveHour;
        private boolean indefinitelyUse;
        private Integer numberOfGpuToUse;
        private boolean useGpuExclusively;
        private List<ServerPortDto.Creation> externalPorts;
        private List<ServerPortDto.Creation> internalPorts;

        public Post() {
            this.externalPorts = new ArrayList<>();
            this.internalPorts = new ArrayList<>();
            this.useGpuExclusively = false;
            this.numberOfGpuToUse = 0;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Port {
        private String tag;
        private int from;
        private int to;
    }

}
