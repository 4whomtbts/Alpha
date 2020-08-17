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

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = SERVER_ID)
    private Long serverId;

    @Column(name = "server_num")
    private int serverNum;

    @Column(name = "internal_ip")
    private String internalIP;

    @Column(name = "ssh_port")
    private int sshPort;

    @Embedded
    private ServerResource serverResource;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "server")
    private List<Instance> instanceList;

    private Server() {}

    public Server(int serverNum, String internalIP, int sshPort, ServerResource serverResource) {
        this.serverNum = serverNum;
        this.internalIP = internalIP;
        this.sshPort = sshPort;
        this.serverResource = serverResource;
        this.instanceList = new ArrayList<>();
    }
}
