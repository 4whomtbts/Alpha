package com.dna.rna.domain;

import com.dna.rna.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Embeddable
@Setter
@Getter
public class ServerResource {

    public static final int EXCLUSIVELY_ALLOC = -1;
    public static final int UN_ALLOC = 0;
    public static final int ALLOC = 1;

    public enum PortStat {
        ESTABLISHED,
        LISTEN,
        WAIT,
        FREE
    }

    @Column(name = "gpus")
    @ElementCollection(targetClass=Integer.class)
    private List<Integer> gpus;

    public ServerResource() {
        this.gpus = new ArrayList<>();
        //this.ports = new ArrayList<>();
        for (int i=0; i < 8; i++) {
            this.gpus.add(UN_ALLOC);
        }
    }

}
