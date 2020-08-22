package com.dna.rna.domain.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1230779633L;

    public static final QUser user = new QUser("user");

    public final com.dna.rna.domain.QBaseAuditorEntity _super = new com.dna.rna.domain.QBaseAuditorEntity(this);

    public final ListPath<com.dna.rna.domain.allowCode.AllowCode, com.dna.rna.domain.allowCode.QAllowCode> allowCodeList = this.<com.dna.rna.domain.allowCode.AllowCode, com.dna.rna.domain.allowCode.QAllowCode>createList("allowCodeList", com.dna.rna.domain.allowCode.AllowCode.class, com.dna.rna.domain.allowCode.QAllowCode.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<com.dna.rna.domain.groupUser.GroupUserType, EnumPath<com.dna.rna.domain.groupUser.GroupUserType>> groupUserTypes = this.<com.dna.rna.domain.groupUser.GroupUserType, EnumPath<com.dna.rna.domain.groupUser.GroupUserType>>createList("groupUserTypes", com.dna.rna.domain.groupUser.GroupUserType.class, EnumPath.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.dna.rna.domain.instance.Instance, com.dna.rna.domain.instance.QInstance> instanceList = this.<com.dna.rna.domain.instance.Instance, com.dna.rna.domain.instance.QInstance>createList("instanceList", com.dna.rna.domain.instance.Instance.class, com.dna.rna.domain.instance.QInstance.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final StringPath loginId = createString("loginId");

    public final StringPath organization = createString("organization");

    public final StringPath password = createString("password");

    public final StringPath userGroup = createString("userGroup");

    public final StringPath userName = createString("userName");

    public final ListPath<com.dna.rna.domain.userRole.UserRole, com.dna.rna.domain.userRole.QUserRole> userRoles = this.<com.dna.rna.domain.userRole.UserRole, com.dna.rna.domain.userRole.QUserRole>createList("userRoles", com.dna.rna.domain.userRole.UserRole.class, com.dna.rna.domain.userRole.QUserRole.class, PathInits.DIRECT2);

    public final EnumPath<UserType> userType = createEnum("userType", UserType.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

