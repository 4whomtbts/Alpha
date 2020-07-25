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

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final com.dna.rna.domain.QBaseAuditorEntity _super = new com.dna.rna.domain.QBaseAuditorEntity(this);

    public final ListPath<com.dna.rna.domain.admission.AdmissionCandidate, com.dna.rna.domain.admission.QAdmissionCandidate> admissionCandidates = this.<com.dna.rna.domain.admission.AdmissionCandidate, com.dna.rna.domain.admission.QAdmissionCandidate>createList("admissionCandidates", com.dna.rna.domain.admission.AdmissionCandidate.class, com.dna.rna.domain.admission.QAdmissionCandidate.class, PathInits.DIRECT2);

    public final ListPath<com.dna.rna.domain.clubUser.ClubUser, com.dna.rna.domain.clubUser.QClubUser> clubUsers = this.<com.dna.rna.domain.clubUser.ClubUser, com.dna.rna.domain.clubUser.QClubUser>createList("clubUsers", com.dna.rna.domain.clubUser.ClubUser.class, com.dna.rna.domain.clubUser.QClubUser.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final StringPath loginId = createString("loginId");

    public final StringPath password = createString("password");

    public final com.dna.rna.domain.school.QSchool school;

    public final BooleanPath school_authorized = createBoolean("school_authorized");

    public final ListPath<com.dna.rna.domain.schoolUser.SchoolUser, com.dna.rna.domain.schoolUser.QSchoolUser> schoolUsers = this.<com.dna.rna.domain.schoolUser.SchoolUser, com.dna.rna.domain.schoolUser.QSchoolUser>createList("schoolUsers", com.dna.rna.domain.schoolUser.SchoolUser.class, com.dna.rna.domain.schoolUser.QSchoolUser.class, PathInits.DIRECT2);

    public final StringPath userName = createString("userName");

    public final ListPath<UserRole, QUserRole> userRoles = this.<UserRole, QUserRole>createList("userRoles", UserRole.class, QUserRole.class, PathInits.DIRECT2);

    public final EnumPath<UserType> userType = createEnum("userType", UserType.class);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.school = inits.isInitialized("school") ? new com.dna.rna.domain.school.QSchool(forProperty("school")) : null;
    }

}

