package com.dna.rna.domain.instance;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInstance is a Querydsl query type for Instance
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInstance extends EntityPathBase<Instance> {

    private static final long serialVersionUID = -550757455L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInstance instance = new QInstance("instance");

    public final com.dna.rna.domain.QBaseAuditorEntity _super = new com.dna.rna.domain.QBaseAuditorEntity(this);

    public final com.dna.rna.domain.QServerResource allocatedResources;

    public final com.dna.rna.domain.containerImage.QContainerImage containerImage;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> expiredAt = createDateTime("expiredAt", java.time.LocalDateTime.class);

    public final StringPath instanceContainerId = createString("instanceContainerId");

    public final StringPath instanceHash = createString("instanceHash");

    public final NumberPath<Long> instanceId = createNumber("instanceId", Long.class);

    public final StringPath instanceName = createString("instanceName");

    public final QInstanceNetworkSetting instanceNetworkSetting;

    public final ListPath<com.dna.rna.domain.serverPort.ServerPort, com.dna.rna.domain.serverPort.QServerPort> instancePorts = this.<com.dna.rna.domain.serverPort.ServerPort, com.dna.rna.domain.serverPort.QServerPort>createList("instancePorts", com.dna.rna.domain.serverPort.ServerPort.class, com.dna.rna.domain.serverPort.QServerPort.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final com.dna.rna.domain.server.QServer server;

    public QInstance(String variable) {
        this(Instance.class, forVariable(variable), INITS);
    }

    public QInstance(Path<? extends Instance> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInstance(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInstance(PathMetadata metadata, PathInits inits) {
        this(Instance.class, metadata, inits);
    }

    public QInstance(Class<? extends Instance> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.allocatedResources = inits.isInitialized("allocatedResources") ? new com.dna.rna.domain.QServerResource(forProperty("allocatedResources")) : null;
        this.containerImage = inits.isInitialized("containerImage") ? new com.dna.rna.domain.containerImage.QContainerImage(forProperty("containerImage")) : null;
        this.instanceNetworkSetting = inits.isInitialized("instanceNetworkSetting") ? new QInstanceNetworkSetting(forProperty("instanceNetworkSetting")) : null;
        this.server = inits.isInitialized("server") ? new com.dna.rna.domain.server.QServer(forProperty("server"), inits.get("server")) : null;
    }

}

