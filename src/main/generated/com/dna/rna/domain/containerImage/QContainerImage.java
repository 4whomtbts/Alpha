package com.dna.rna.domain.containerImage;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContainerImage is a Querydsl query type for ContainerImage
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContainerImage extends EntityPathBase<ContainerImage> {

    private static final long serialVersionUID = -1996347951L;

    public static final QContainerImage containerImage = new QContainerImage("containerImage");

    public final StringPath containerImageDescription = createString("containerImageDescription");

    public final NumberPath<Long> containerImageId = createNumber("containerImageId", Long.class);

    public final StringPath containerImageName = createString("containerImageName");

    public final StringPath containerImageNickName = createString("containerImageNickName");

    public final ListPath<com.dna.rna.domain.instance.Instance, com.dna.rna.domain.instance.QInstance> instanceList = this.<com.dna.rna.domain.instance.Instance, com.dna.rna.domain.instance.QInstance>createList("instanceList", com.dna.rna.domain.instance.Instance.class, com.dna.rna.domain.instance.QInstance.class, PathInits.DIRECT2);

    public QContainerImage(String variable) {
        super(ContainerImage.class, forVariable(variable));
    }

    public QContainerImage(Path<? extends ContainerImage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContainerImage(PathMetadata metadata) {
        super(ContainerImage.class, metadata);
    }

}

