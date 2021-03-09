package com.dna.rna.domain.server;

import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.instance.Instance;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name= "server")
public class Server {

    public static final String SERVER_ID = "server_id";
    public static final int PORT_RANGE = 100;
    public static final int MIN_INTERNAL_PORT = 10000;

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = SERVER_ID)
    private long serverId;

    @Column(name = "server_num")
    private int serverNum;

    @Column(name = "internal_ip")
    private String internalIP;

    @Column(name = "ssh_port")
    private int sshPort;

    @Column(name = "min_external_port")
    private int minExternalPort;

    @Column(name = "min_internal_port")
    private int minInternalPort;

    @Column(name = "excluded")
    private boolean excluded;

    @Column(name = "shared_directory_path")
    private String sharedDirectoryPath;

    @Embedded
    private ServerResource serverResource;

    @OneToMany(fetch = FetchType.EAGER,
               orphanRemoval = true)
    @JoinColumn(name = "server")
    private List<Instance> instanceList;

    private Server() {}

    public Server(int serverNum, String internalIP, int sshPort, int minExternalPort, ServerResource serverResource) {
        this.serverNum = serverNum;
        this.internalIP = internalIP;
        this.sshPort = sshPort;
        this.minExternalPort= minExternalPort;
        this.minInternalPort = MIN_INTERNAL_PORT;
        this.serverResource = serverResource;
        this.instanceList = new ArrayList<>();
        this.excluded = false;
    }

}
