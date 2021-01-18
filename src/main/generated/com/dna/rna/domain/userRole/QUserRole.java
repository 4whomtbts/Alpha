package com.dna.rna.domain.userRole;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserRole is a Querydsl query type for UserRole
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserRole extends EntityPathBase<UserRole> {

    private static final long serialVersionUID = 2052419889L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserRole userRole = new QUserRole("userRole");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.dna.rna.domain.CRUDPermissions, EnumPath<com.dna.rna.domain.CRUDPermissions>> permissions = this.<com.dna.rna.domain.CRUDPermissions, EnumPath<com.dna.rna.domain.CRUDPermissions>>createList("permissions", com.dna.rna.domain.CRUDPermissions.class, EnumPath.class, PathInits.DIRECT2);

    public final StringPath roleName = createString("roleName");

    public final com.dna.rna.domain.user.QUser user;

    public QUserRole(String variable) {
        this(UserRole.class, forVariable(variable), INITS);
    }

    public QUserRole(Path<? extends UserRole> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserRole(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserRole(PathMetadata metadata, PathInits inits) {
        this(UserRole.class, metadata, inits);
    }

    public QUserRole(Class<? extends UserRole> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.dna.rna.domain.user.QUser(forProperty("user")) : null;
    }

}

