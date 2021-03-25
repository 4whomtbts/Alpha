package com.dna.rna.domain.instanceGpu;

import com.dna.rna.domain.gpu.Gpu;
import com.dna.rna.domain.server.Server;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static com.dna.rna.domain.gpu.Gpu.GPU_ID;
import static com.dna.rna.domain.server.Server.SERVER_ID;
import static java.util.Objects.requireNonNull;

@Getter
@Setter
@Entity
@Table(name = "instance_gpu")
public class InstanceGpu {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long serverGpuId;

    @ManyToOne
    @JoinColumn(name = SERVER_ID)
    private Server server;

    @ManyToOne
    @JoinColumn(name = GPU_ID)
    private Gpu gpu;

    private boolean isExclusivelyOccupied = false;

    private InstanceGpu() {}

    public InstanceGpu(Server server, Gpu gpu, boolean isExclusivelyOccupied) {
        requireNonNull(server);
        requireNonNull(gpu);
        this.server = server;
        this.gpu = gpu;
        this.isExclusivelyOccupied = isExclusivelyOccupied;
    }
}
