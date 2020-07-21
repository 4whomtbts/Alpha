package com.dna.rna.domain.admission;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdmissionUnit is a Querydsl query type for AdmissionUnit
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAdmissionUnit extends EntityPathBase<AdmissionUnit> {

    private static final long serialVersionUID = 1615601483L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAdmissionUnit admissionUnit = new QAdmissionUnit("admissionUnit");

    public final ListPath<AdmissionCandidate, QAdmissionCandidate> admissionCandidateList = this.<AdmissionCandidate, QAdmissionCandidate>createList("admissionCandidateList", AdmissionCandidate.class, QAdmissionCandidate.class, PathInits.DIRECT2);

    public final NumberPath<Integer> admissionOrder = createNumber("admissionOrder", Integer.class);

    public final StringPath admissionUnitDescription = createString("admissionUnitDescription");

    public final StringPath admissionUnitTitle = createString("admissionUnitTitle");

    public final QClubAdmission clubAdmission;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QAdmissionUnit(String variable) {
        this(AdmissionUnit.class, forVariable(variable), INITS);
    }

    public QAdmissionUnit(Path<? extends AdmissionUnit> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAdmissionUnit(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAdmissionUnit(PathMetadata metadata, PathInits inits) {
        this(AdmissionUnit.class, metadata, inits);
    }

    public QAdmissionUnit(Class<? extends AdmissionUnit> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.clubAdmission = inits.isInitialized("clubAdmission") ? new QClubAdmission(forProperty("clubAdmission")) : null;
    }

}

