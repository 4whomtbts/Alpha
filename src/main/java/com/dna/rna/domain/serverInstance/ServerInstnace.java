package com.dna.rna.domain.serverInstance;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name= "server_instance")
public class ServerInstnace {

    public static final String SERVER_INSTANCE_ID = "server_instance_id";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = SERVER_INSTANCE_ID)
    private Long serverInstanceId;

}
