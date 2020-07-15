package com.dna.rna.domain.SchoolUser;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSchoolUser is a Querydsl query type for SchoolUser
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSchoolUser extends EntityPathBase<SchoolUser> {

    private static final long serialVersionUID = 1328471441L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSchoolUser schoolUser = new QSchoolUser("schoolUser");

    public final com.dna.rna.domain.QBaseAuditorEntity _super = new com.dna.rna.domain.QBaseAuditorEntity(this);

    public final BooleanPath admin = createBoolean("admin");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final com.dna.rna.domain.School.QSchool school;

    public final SetPath<SchoolUserPermissions, EnumPath<SchoolUserPermissions>> schoolUserPermissions = this.<SchoolUserPermissions, EnumPath<SchoolUserPermissions>>createSet("schoolUserPermissions", SchoolUserPermissions.class, EnumPath.class, PathInits.DIRECT2);

    public final com.dna.rna.domain.User.QUser user;

    public QSchoolUser(String variable) {
        this(SchoolUser.class, forVariable(variable), INITS);
    }

    public QSchoolUser(Path<? extends SchoolUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSchoolUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSchoolUser(PathMetadata metadata, PathInits inits) {
        this(SchoolUser.class, metadata, inits);
    }

    public QSchoolUser(Class<? extends SchoolUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.school = inits.isInitialized("school") ? new com.dna.rna.domain.School.QSchool(forProperty("school")) : null;
        this.user = inits.isInitialized("user") ? new com.dna.rna.domain.User.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

