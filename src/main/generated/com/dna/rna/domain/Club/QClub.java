package com.dna.rna.domain.Club;

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

    private static final long serialVersionUID = -1344000079L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClub club = new QClub("club");

    public final ListPath<com.dna.rna.domain.Admission.AdmissionUnit, com.dna.rna.domain.Admission.QAdmissionUnit> admissionUnits = this.<com.dna.rna.domain.Admission.AdmissionUnit, com.dna.rna.domain.Admission.QAdmissionUnit>createList("admissionUnits", com.dna.rna.domain.Admission.AdmissionUnit.class, com.dna.rna.domain.Admission.QAdmissionUnit.class, PathInits.DIRECT2);

    public final StringPath clubName = createString("clubName");

    public final ListPath<ClubUser, QClubUser> clubUsers = this.<ClubUser, QClubUser>createList("clubUsers", ClubUser.class, QClubUser.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath location = createString("location");

    public final StringPath longDescription = createString("longDescription");

    public final com.dna.rna.domain.School.QSchool school;

    public final StringPath season = createString("season");

    public final StringPath shortDescription = createString("shortDescription");

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
        this.school = inits.isInitialized("school") ? new com.dna.rna.domain.School.QSchool(forProperty("school")) : null;
    }

}

