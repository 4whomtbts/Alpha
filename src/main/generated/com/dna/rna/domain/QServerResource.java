package com.dna.rna.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QServerResource is a Querydsl query type for ServerResource
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QServerResource extends BeanPath<ServerResource> {

    private static final long serialVersionUID = 535187886L;

    public static final QServerResource serverResource = new QServerResource("serverResource");

    public final ListPath<Integer, NumberPath<Integer>> gpus = this.<Integer, NumberPath<Integer>>createList("gpus", Integer.class, NumberPath.class, PathInits.DIRECT2);

    public QServerResource(String variable) {
        super(ServerResource.class, forVariable(variable));
    }

    public QServerResource(Path<? extends ServerResource> path) {
        super(path.getType(), path.getMetadata());
    }

    public QServerResource(PathMetadata metadata) {
        super(ServerResource.class, metadata);
    }

}

