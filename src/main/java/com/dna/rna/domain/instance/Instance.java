package com.dna.rna.domain.instance;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.containerImage.ContainerImage;
import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.server.Server;
import com.dna.rna.domain.serverPort.ServerPort;
import com.dna.rna.domain.user.User;
import com.dna.rna.dto.InstanceDto;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.dna.rna.domain.containerImage.ContainerImage.CONTAINER_IMAGE_ID;
import static com.dna.rna.domain.server.Server.SERVER_ID;
import static com.dna.rna.domain.user.User.USER_ID;


@Getter
@Setter
@Entity
@Table(name= "instance")
public class Instance extends BaseAuditorEntity {

    private static final Logger logger= LoggerFactory.getLogger(Instance.class);

    static final String INSTANCE_ID = "instance_id";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = INSTANCE_ID)
    private long instanceId;

    @Column(name = "instance_name")
    private String instanceName;

    @Column(name = "purpose")
    private String purpose;

    // docker container 이름
    @Column(name = "instance_container_id")
    private String instanceContainerId;

    @Column(name = "instance_hash", length = 512)
    private String instanceHash;

    @Column(name = "initialized")
    private boolean initialized = false;

    @Column(name = "error")
    private boolean error = false;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = INSTANCE_ID)
    private List<ServerPort> instancePorts;

    @ManyToOne
    @JoinColumn(name = CONTAINER_IMAGE_ID)
    private ContainerImage containerImage;

    @ManyToOne
    @JoinColumn(name = USER_ID)
    private User owner;

    @ManyToOne
    @JoinColumn(name = SERVER_ID)
    private Server server;

    @Embedded
    @Column(name = "allocated_resources")
    private ServerResource allocatedResources;

    @Embedded
    @Column(name = "instance_network_setting")
    private InstanceNetworkSetting instanceNetworkSetting;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;


    // 최소한의 정보만 가지고 있는 instance entity 만드는 목적
    // 생성 후 인스턴스 목록으로 갔을 때, 인스턴스가 즉시 생성되지 않아서
    // 우선 최소한의 entity 를 빠르게 만들어서 진행상황을 보여주는 용도
    /*
    public static Instance createFrameInstance() {

    }
    */

    protected Instance() {}

    private Instance(String instanceContainerId, User owner) {
        this.instanceContainerId = instanceContainerId;
        this.owner = owner;
    }

    public Instance(String instanceName, String instanceContainerId, String instanceHash, User owner, ContainerImage containerImage,
                    Server server, ServerResource allocatedResources, InstanceNetworkSetting instanceNetworkSetting,
                    LocalDateTime expiredAt) {
        this.instanceName = instanceName;
        this.instanceContainerId = instanceContainerId;
        this.instanceHash = instanceHash;
        this.owner = owner;
        this.containerImage = containerImage;
        this.server = server;
        this.allocatedResources = allocatedResources;
        this.instanceNetworkSetting = instanceNetworkSetting;
        this.instancePorts = new ArrayList<>();
        this.expiredAt = expiredAt;
    }

    public static Instance skeletonInstance(String instanceContainerName, User owner) {
        return new Instance(instanceContainerName, owner);
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
                externalPortsBuilder.append(serverPort.getFrom());
            } else {
                internalPortsBuilder.append(serverPort.getTag());
                internalPortsBuilder.append(" : ");
                internalPortsBuilder.append(serverPort.getFrom());
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

        String logFileName = this.getInstanceContainerId();
        StringBuilder logLinesBuilder = new StringBuilder();
        File logFile = new File("./" + logFileName);
        if (logFile.exists()) {
            try(Scanner reader = new Scanner(logFile)) {
                while (reader.hasNextLine()) {
                    logLinesBuilder.append(reader.nextLine()).append("\n");
                }
            } catch (FileNotFoundException e) {
                logger.error("인스턴스 [{}] 의 로그파일 [{}] 이 존재하지 않습니다",
                        this.getInstanceContainerId(), logFileName);
                logLinesBuilder.append(
                        String.format("인스턴스 로그를 불러오는 중 오류가 발생했습니다. 관리자에게 문의해주세요\n" +
                                        "오류정보 : [%s]\n stacktrace : [%s]",
                                ExceptionUtils.getMessage(e), ExceptionUtils.getStackTrace(e)));
            }

        } else {
            logger.error("인스턴스 [{}] 의 로그파일 [{}] 이 존재하지 않습니다",
                    this.getInstanceContainerId(), logFileName);
        }

        /* 생성실패한 인스턴스를 위한 임시데이터 만들어 주는 로직 */
        String serverIP;
        if (server == null) {
            serverIP = "미배정";
        } else {
            serverIP = server.getInternalIP();
        }

        String instanceHash;
        if (this.instanceHash == null) {
            instanceHash = "NOT_DEFINED";
        } else {
            instanceHash = this.instanceHash.substring(0, 12);
        }

        String containerImageName;
        if (this.containerImage == null) {
            containerImageName = "NULL";
        } else {
            containerImageName = this.containerImage.getContainerImageNickName();
        }

        return new InstanceDto(
                this.instanceId, this.instanceContainerId, instanceHash, this.instanceName, containerImageName,
                "RUNNING", false, gpuResource.toString(), serverIP, "210.94.223.123",
                 externalPortsBuilder.toString(), internalPortsBuilder.toString(), periodBuilder.toString(),
                 error, initialized, logLinesBuilder.toString());
    }

    public void writeInstanceLog(String log) {
        File logFile = new File("./" + this.getInstanceContainerId());
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(log + "\n");
        }  catch (IOException e) {
            logger.error(
                    String.format("[%s] 인스턴스의 오류를 기록하는 도중 IOException 발생 : 원래 로그 메세지 [%s]",
                            this.getInstanceContainerId(),  error));
        }
    }
}
