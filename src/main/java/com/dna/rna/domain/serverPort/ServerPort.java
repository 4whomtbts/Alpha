package com.dna.rna.domain.serverPort;

import com.dna.rna.domain.instance.Instance;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name= "server_port")
public class ServerPort {

    public static final String SERVER_PORT = "server_port_id";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = SERVER_PORT)
    private Long serverPortId;

    @Column(name = "tag")
    private String tag;

    @Column(name = "external")
    private boolean external;

    @Column(name = "port_from")
    private int from;

    @Column(name = "port_to")
    private int to;

    @ManyToOne(fetch = FetchType.LAZY)
    private Instance instance;

    protected ServerPort() {}

    public ServerPort(String tag, boolean external, int from, int to, Instance instance) {
        this.tag = tag;
        this.external = external;
        this.from = from;
        this.to = to;
        this.instance = instance;
    }
}
