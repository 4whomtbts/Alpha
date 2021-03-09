package com.dna.rna.domain.serverPort;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QServerPort is a Querydsl query type for ServerPort
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QServerPort extends EntityPathBase<ServerPort> {

    private static final long serialVersionUID = -1188353903L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QServerPort serverPort = new QServerPort("serverPort");

    public final BooleanPath external = createBoolean("external");

    public final NumberPath<Integer> from = createNumber("from", Integer.class);

    public final com.dna.rna.domain.instance.QInstance instance;

    public final NumberPath<Long> serverPortId = createNumber("serverPortId", Long.class);

    public final StringPath tag = createString("tag");

    public final NumberPath<Integer> to = createNumber("to", Integer.class);

    public QServerPort(String variable) {
        this(ServerPort.class, forVariable(variable), INITS);
    }

    public QServerPort(Path<? extends ServerPort> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QServerPort(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QServerPort(PathMetadata metadata, PathInits inits) {
        this(ServerPort.class, metadata, inits);
    }

    public QServerPort(Class<? extends ServerPort> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.instance = inits.isInitialized("instance") ? new com.dna.rna.domain.instance.QInstance(forProperty("instance"), inits.get("instance")) : null;
    }

}

