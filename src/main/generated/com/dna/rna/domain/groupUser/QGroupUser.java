package com.dna.rna.domain.groupUser;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroupUser is a Querydsl query type for GroupUser
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QGroupUser extends EntityPathBase<GroupUser> {

    private static final long serialVersionUID = -599892535L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGroupUser groupUser = new QGroupUser("groupUser");

    public final com.dna.rna.domain.group.QGroup group;

    public final NumberPath<Long> groupUserId = createNumber("groupUserId", Long.class);

    public final EnumPath<GroupUserType> groupUserType = createEnum("groupUserType", GroupUserType.class);

    public final com.dna.rna.domain.user.QUser user;

    public QGroupUser(String variable) {
        this(GroupUser.class, forVariable(variable), INITS);
    }

    public QGroupUser(Path<? extends GroupUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGroupUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGroupUser(PathMetadata metadata, PathInits inits) {
        this(GroupUser.class, metadata, inits);
    }

    public QGroupUser(Class<? extends GroupUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.group = inits.isInitialized("group") ? new com.dna.rna.domain.group.QGroup(forProperty("group")) : null;
        this.user = inits.isInitialized("user") ? new com.dna.rna.domain.user.QUser(forProperty("user")) : null;
    }

}

