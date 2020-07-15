package com.dna.rna.domain.User;

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

    private static final long serialVersionUID = -1060219695L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final com.dna.rna.domain.QBaseAuditorEntity _super = new com.dna.rna.domain.QBaseAuditorEntity(this);

    public final ListPath<com.dna.rna.domain.Admission.AdmissionCandidate, com.dna.rna.domain.Admission.QAdmissionCandidate> admissionCandidates = this.<com.dna.rna.domain.Admission.AdmissionCandidate, com.dna.rna.domain.Admission.QAdmissionCandidate>createList("admissionCandidates", com.dna.rna.domain.Admission.AdmissionCandidate.class, com.dna.rna.domain.Admission.QAdmissionCandidate.class, PathInits.DIRECT2);

    public final ListPath<com.dna.rna.domain.Club.ClubUser, com.dna.rna.domain.Club.QClubUser> clubUsers = this.<com.dna.rna.domain.Club.ClubUser, com.dna.rna.domain.Club.QClubUser>createList("clubUsers", com.dna.rna.domain.Club.ClubUser.class, com.dna.rna.domain.Club.QClubUser.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final StringPath loginId = createString("loginId");

    public final StringPath password = createString("password");

    public final com.dna.rna.domain.School.QSchool school;

    public final BooleanPath school_authorized = createBoolean("school_authorized");

    public final ListPath<com.dna.rna.domain.SchoolUser.SchoolUser, com.dna.rna.domain.SchoolUser.QSchoolUser> schoolUsers = this.<com.dna.rna.domain.SchoolUser.SchoolUser, com.dna.rna.domain.SchoolUser.QSchoolUser>createList("schoolUsers", com.dna.rna.domain.SchoolUser.SchoolUser.class, com.dna.rna.domain.SchoolUser.QSchoolUser.class, PathInits.DIRECT2);

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
        this.school = inits.isInitialized("school") ? new com.dna.rna.domain.School.QSchool(forProperty("school")) : null;
    }

}

