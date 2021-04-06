package com.dna.rna.domain.gpu;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGpu is a Querydsl query type for Gpu
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QGpu extends EntityPathBase<Gpu> {

    private static final long serialVersionUID = 522097101L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGpu gpu = new QGpu("gpu");

    public final NumberPath<Long> gpuId = createNumber("gpuId", Long.class);

    public final ListPath<com.dna.rna.domain.instanceGpu.InstanceGpu, com.dna.rna.domain.instanceGpu.QInstanceGpu> instanceGpuList = this.<com.dna.rna.domain.instanceGpu.InstanceGpu, com.dna.rna.domain.instanceGpu.QInstanceGpu>createList("instanceGpuList", com.dna.rna.domain.instanceGpu.InstanceGpu.class, com.dna.rna.domain.instanceGpu.QInstanceGpu.class, PathInits.DIRECT2);

    public final StringPath modelName = createString("modelName");

    public final com.dna.rna.domain.server.QServer server;

    public final NumberPath<Integer> slotIndex = createNumber("slotIndex", Integer.class);

    public final StringPath uuid = createString("uuid");

    public QGpu(String variable) {
        this(Gpu.class, forVariable(variable), INITS);
    }

    public QGpu(Path<? extends Gpu> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGpu(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGpu(PathMetadata metadata, PathInits inits) {
        this(Gpu.class, metadata, inits);
    }

    public QGpu(Class<? extends Gpu> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.server = inits.isInitialized("server") ? new com.dna.rna.domain.server.QServer(forProperty("server"), inits.get("server")) : null;
    }

}

