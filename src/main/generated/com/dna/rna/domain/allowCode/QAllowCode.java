package com.dna.rna.domain.allowCode;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QAllowCode is a Querydsl query type for AllowCode
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAllowCode extends EntityPathBase<AllowCode> {

    private static final long serialVersionUID = -469640543L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAllowCode allowCode1 = new QAllowCode("allowCode1");

    public final StringPath allowCode = createString("allowCode");

    public final NumberPath<Long> allowCodeId = createNumber("allowCodeId", Long.class);

    public final EnumPath<AllowCodeType> allowCodeType = createEnum("allowCodeType", AllowCodeType.class);

    public final BooleanPath expired = createBoolean("expired");

    public final com.dna.rna.domain.user.QUser user;

    public QAllowCode(String variable) {
        this(AllowCode.class, forVariable(variable), INITS);
    }

    public QAllowCode(Path<? extends AllowCode> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAllowCode(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAllowCode(PathMetadata metadata, PathInits inits) {
        this(AllowCode.class, metadata, inits);
    }

    public QAllowCode(Class<? extends AllowCode> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.dna.rna.domain.user.QUser(forProperty("user")) : null;
    }

}

