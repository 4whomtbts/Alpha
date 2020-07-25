package com.dna.rna.domain.club;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClub is a Querydsl query type for Club
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QClub extends EntityPathBase<Club> {

    private static final long serialVersionUID = 946999249L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClub club = new QClub("club");

    public final com.dna.rna.domain.QBaseAuditorEntity _super = new com.dna.rna.domain.QBaseAuditorEntity(this);

    public final ListPath<com.dna.rna.domain.admission.AdmissionUnit, com.dna.rna.domain.admission.QAdmissionUnit> admissionUnits = this.<com.dna.rna.domain.admission.AdmissionUnit, com.dna.rna.domain.admission.QAdmissionUnit>createList("admissionUnits", com.dna.rna.domain.admission.AdmissionUnit.class, com.dna.rna.domain.admission.QAdmissionUnit.class, PathInits.DIRECT2);

    public final StringPath clubName = createString("clubName");

    public final ListPath<com.dna.rna.domain.clubUser.ClubUser, com.dna.rna.domain.clubUser.QClubUser> clubUsers = this.<com.dna.rna.domain.clubUser.ClubUser, com.dna.rna.domain.clubUser.QClubUser>createList("clubUsers", com.dna.rna.domain.clubUser.ClubUser.class, com.dna.rna.domain.clubUser.QClubUser.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final com.dna.rna.domain.user.QUser leader;

    public final StringPath location = createString("location");

    public final StringPath longDescription = createString("longDescription");

    public final StringPath profileImageUri = createString("profileImageUri");

    public final com.dna.rna.domain.school.QSchool school;

    public final StringPath season = createString("season");

    public final StringPath shortDescription = createString("shortDescription");

    public final DatePath<java.time.LocalDate> since = createDate("since", java.time.LocalDate.class);

    public QClub(String variable) {
        this(Club.class, forVariable(variable), INITS);
    }

    public QClub(Path<? extends Club> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClub(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClub(PathMetadata metadata, PathInits inits) {
        this(Club.class, metadata, inits);
    }

    public QClub(Class<? extends Club> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.leader = inits.isInitialized("leader") ? new com.dna.rna.domain.user.QUser(forProperty("leader"), inits.get("leader")) : null;
        this.school = inits.isInitialized("school") ? new com.dna.rna.domain.school.QSchool(forProperty("school")) : null;
    }

}

