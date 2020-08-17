package com.dna.rna.domain.serverPort;

import com.dna.rna.domain.instance.Instance;
import com.dna.rna.domain.server.Server;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name= "server_port")
public class ServerPort {

    public static final String SERVER_PORT = "server_port";
    public static final int MIN_PORT = 9000;

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = SERVER_PORT)
    private Long serverPortId;

    @Column(name = "tag")
    private String tag;

    @Column(name = "external")
    private boolean external;

    @Column(name = "port")
    private int port;

    @ManyToOne(fetch = FetchType.LAZY)
    private Server server;

    @ManyToOne(fetch = FetchType.LAZY)
    private Instance instance;

    protected ServerPort() {}

    public ServerPort(String tag, boolean external, int port,
                      Server server, Instance instance) {
        this.tag = tag;
        this.external = external;
        this.port = port;
        this.server = server;
        this.instance = instance;
    }
}
