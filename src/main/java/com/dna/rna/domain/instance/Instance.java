package com.dna.rna.domain.instance;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.containerImage.ContainerImage;
import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.server.Server;
import com.dna.rna.domain.serverPort.ServerPort;
import com.dna.rna.dto.InstanceDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.dna.rna.domain.containerImage.ContainerImage.CONTAINER_IMAGE_ID;
import static com.dna.rna.domain.server.Server.SERVER_ID;


@Getter
@Setter
@Entity
@Table(name= "instance")
public class Instance extends BaseAuditorEntity {

    static final String INSTANCE_ID = "instance_id";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = INSTANCE_ID)
    private Long instanceId;

    @Column(name = "instance_name")
    private String instanceName;

    // docker container 이름
    @Column(name = "instance_container_id")
    private String instanceContainerId;

    @Column(name = "instance_hash")
    private String instanceHash;

    @OneToMany
    @JoinColumn(name = INSTANCE_ID)
    private List<ServerPort> instancePorts;

    @ManyToOne
    @JoinColumn(name = CONTAINER_IMAGE_ID, nullable = false)
    private ContainerImage containerImage;

    @ManyToOne
    @JoinColumn(name = SERVER_ID, nullable = false)
    private Server server;

    @Embedded
    @Column(name = "allocated_resources")
    private ServerResource allocatedResources;

    @Embedded
    @Column(name = "instance_network_setting")
    private InstanceNetworkSetting instanceNetworkSetting;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    private Instance() {}

    public Instance(String instanceName, String instanceContainerId, String instanceHash, ContainerImage containerImage,
                    Server server, ServerResource allocatedResources, InstanceNetworkSetting instanceNetworkSetting,
                    LocalDateTime expiredAt) {
        this.instanceName = instanceName;
        this.instanceContainerId = instanceContainerId;
        this.instanceHash = instanceHash;
        this.containerImage = containerImage;
        this.server = server;
        this.allocatedResources = allocatedResources;
        this.instanceNetworkSetting = instanceNetworkSetting;
        this.instancePorts = new ArrayList<>();
        this.expiredAt = expiredAt;
    }

    public InstanceDto toInstanceDto() {
        StringBuilder gpuResource = new StringBuilder();
        List<Integer> gpus = this.allocatedResources.getGpus();
        for (int i=0; i < gpus.size(); i++) {
            int gpu = gpus.get(i);
            if (gpu == ServerResource.EXCLUSIVELY_ALLOC) {
                gpuResource.append("/Xgpu:");
                gpuResource.append(i);
                gpuResource.append(", ");
            } else if (gpu == ServerResource.ALLOC) {
                gpuResource.append("/gpu:");
                gpuResource.append(i);
                gpuResource.append(", ");
            }
        }
        StringBuilder periodBuilder = new StringBuilder();
        if (this.expiredAt == null) {
            periodBuilder.append("무기한");
        } else {
            String formattedStartDate = this.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            String formattedExpiredDate = this.expiredAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            periodBuilder.append(formattedStartDate);
            periodBuilder.append(" ~ ");
            periodBuilder.append(formattedExpiredDate);
        }

        StringBuilder internalPortsBuilder = new StringBuilder();
        StringBuilder externalPortsBuilder = new StringBuilder();

        for (ServerPort serverPort : this.getInstancePorts()) {
            if (serverPort.isExternal()) {
                externalPortsBuilder.append(serverPort.getTag());
                externalPortsBuilder.append(" : ");
                externalPortsBuilder.append(serverPort.getPort());
            } else {
                internalPortsBuilder.append(serverPort.getTag());
                internalPortsBuilder.append(" : ");
                internalPortsBuilder.append(serverPort.getPort());
            }
        }
        /*
        for (Map.Entry<String, Integer> elem : instanceNetworkSetting.getExternalPorts().entrySet()) {
            internalPortsBuilder.append(elem.getKey());
            internalPortsBuilder.append(" : ");
            internalPortsBuilder.append(elem.getValue());
            internalPortsBuilder.append("\n");
        }
        */

        /*
        for (Map.Entry<String, Integer> elem : instanceNetworkSetting.getInternalPorts().entrySet()) {
            externalPortsBuilder.append(elem.getKey());
            externalPortsBuilder.append(" : ");
            externalPortsBuilder.append(elem.getValue());
            externalPortsBuilder.append("\n");
        }

        */

        return new InstanceDto(
                this.instanceContainerId, this.instanceHash.substring(0, 12), this.instanceName, this.containerImage.getContainerImageNickName(),
                "RUNNING", gpuResource.toString(), server.getInternalIP(), "210.94.223.123",
                 externalPortsBuilder.toString(), internalPortsBuilder.toString(), periodBuilder.toString());
    }
}
