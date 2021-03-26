package com.dna.rna.domain.instanceGpu;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInstanceGpu is a Querydsl query type for InstanceGpu
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInstanceGpu extends EntityPathBase<InstanceGpu> {

    private static final long serialVersionUID = 1776198563L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInstanceGpu instanceGpu = new QInstanceGpu("instanceGpu");

    public final com.dna.rna.domain.gpu.QGpu gpu;

    public final BooleanPath isExclusivelyOccupied = createBoolean("isExclusivelyOccupied");

    public final com.dna.rna.domain.server.QServer server;

    public final NumberPath<Long> serverGpuId = createNumber("serverGpuId", Long.class);

    public QInstanceGpu(String variable) {
        this(InstanceGpu.class, forVariable(variable), INITS);
    }

    public QInstanceGpu(Path<? extends InstanceGpu> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInstanceGpu(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInstanceGpu(PathMetadata metadata, PathInits inits) {
        this(InstanceGpu.class, metadata, inits);
    }

    public QInstanceGpu(Class<? extends InstanceGpu> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.gpu = inits.isInitialized("gpu") ? new com.dna.rna.domain.gpu.QGpu(forProperty("gpu"), inits.get("gpu")) : null;
        this.server = inits.isInitialized("server") ? new com.dna.rna.domain.server.QServer(forProperty("server"), inits.get("server")) : null;
    }

}

