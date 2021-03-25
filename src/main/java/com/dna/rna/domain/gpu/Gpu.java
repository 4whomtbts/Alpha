package com.dna.rna.domain.gpu;

import com.dna.rna.domain.server.Server;
import com.dna.rna.domain.instanceGpu.InstanceGpu;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static com.dna.rna.domain.server.Server.SERVER_ID;
import static java.util.Objects.requireNonNull;

@Getter
@Setter
@Entity
@Table(name = "gpu")
public class Gpu {

    public static final String GPU_ID = "GPU_ID";

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = GPU_ID)
    private long gpuId;

    // 실제 Machine 내에서 gpu가 꼽혀있는 슬롯의 인덱스
    private int slotIndex;

    // GPU의 uuid
    private String uuid;

    // GPU 모델명  예) GTX 3080
    private String modelName;

    @ManyToOne
    @JoinColumn(name = SERVER_ID)
    private Server server;

    @OneToMany
    @JoinColumn(name = "gpu")
    private List<InstanceGpu> instanceGpuList = new ArrayList<>();

    private Gpu() {}

    public Gpu(Server server, int slotIndex, String uuid, String modelName) {
        assert slotIndex >= 0;
        requireNonNull(server);
        requireNonNull(uuid);
        requireNonNull(modelName);
        this.server = server;
        this.slotIndex = slotIndex;
        this.uuid = uuid;
        this.modelName = modelName;
    }
}
