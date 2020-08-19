package com.dna.rna.domain.server;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QServer is a Querydsl query type for Server
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QServer extends EntityPathBase<Server> {

    private static final long serialVersionUID = 2069575601L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QServer server = new QServer("server");

    public final ListPath<com.dna.rna.domain.instance.Instance, com.dna.rna.domain.instance.QInstance> instanceList = this.<com.dna.rna.domain.instance.Instance, com.dna.rna.domain.instance.QInstance>createList("instanceList", com.dna.rna.domain.instance.Instance.class, com.dna.rna.domain.instance.QInstance.class, PathInits.DIRECT2);

    public final StringPath internalIP = createString("internalIP");

    public final NumberPath<Integer> minExternalPort = createNumber("minExternalPort", Integer.class);

    public final NumberPath<Integer> minInternalPort = createNumber("minInternalPort", Integer.class);

    public final NumberPath<Long> serverId = createNumber("serverId", Long.class);

    public final NumberPath<Integer> serverNum = createNumber("serverNum", Integer.class);

    public final com.dna.rna.domain.QServerResource serverResource;

    public final NumberPath<Integer> sshPort = createNumber("sshPort", Integer.class);

    public QServer(String variable) {
        this(Server.class, forVariable(variable), INITS);
    }

    public QServer(Path<? extends Server> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QServer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QServer(PathMetadata metadata, PathInits inits) {
        this(Server.class, metadata, inits);
    }

    public QServer(Class<? extends Server> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.serverResource = inits.isInitialized("serverResource") ? new com.dna.rna.domain.QServerResource(forProperty("serverResource")) : null;
    }

}

