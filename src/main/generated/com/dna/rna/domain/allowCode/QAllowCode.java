package com.dna.rna.domain.allowCode;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAllowCode is a Querydsl query type for AllowCode
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAllowCode extends EntityPathBase<AllowCode> {

    private static final long serialVersionUID = -469640543L;

    public static final QAllowCode allowCode1 = new QAllowCode("allowCode1");

    public final StringPath allowCode = createString("allowCode");

    public final NumberPath<Long> allowCodeId = createNumber("allowCodeId", Long.class);

    public final BooleanPath expired = createBoolean("expired");

    public QAllowCode(String variable) {
        super(AllowCode.class, forVariable(variable));
    }

    public QAllowCode(Path<? extends AllowCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAllowCode(PathMetadata metadata) {
        super(AllowCode.class, metadata);
    }

}

