package com.dna.rna.domain.serverInstance;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QServerInstnace is a Querydsl query type for ServerInstnace
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QServerInstnace extends EntityPathBase<ServerInstnace> {

    private static final long serialVersionUID = -1876103657L;

    public static final QServerInstnace serverInstnace = new QServerInstnace("serverInstnace");

    public final NumberPath<Long> serverInstanceId = createNumber("serverInstanceId", Long.class);

    public QServerInstnace(String variable) {
        super(ServerInstnace.class, forVariable(variable));
    }

    public QServerInstnace(Path<? extends ServerInstnace> path) {
        super(path.getType(), path.getMetadata());
    }

    public QServerInstnace(PathMetadata metadata) {
        super(ServerInstnace.class, metadata);
    }

}

