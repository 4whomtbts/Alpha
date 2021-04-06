package com.dna.rna.domain.instanceGpu;

import com.dna.rna.domain.gpu.Gpu;
import com.dna.rna.domain.instance.Instance;
import com.dna.rna.domain.server.Server;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static com.dna.rna.domain.gpu.Gpu.GPU_ID;
import static com.dna.rna.domain.instance.Instance.INSTANCE_ID;
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
    @JoinColumn(name = INSTANCE_ID)
    private Instance instance;

    @ManyToOne
    @JoinColumn(name = GPU_ID)
    private Gpu gpu;

    private boolean isExclusivelyOccupied = false;

    private InstanceGpu() {}

    public InstanceGpu(Instance instance, Gpu gpu, boolean isExclusivelyOccupied) {
        requireNonNull(instance);
        requireNonNull(gpu);
        this.instance = instance;
        this.gpu = gpu;
        this.isExclusivelyOccupied = isExclusivelyOccupied;
    }
}
