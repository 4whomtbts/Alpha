package com.dna.rna.domain.Admission;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdmissionCandidate is a Querydsl query type for AdmissionCandidate
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAdmissionCandidate extends EntityPathBase<AdmissionCandidate> {

    private static final long serialVersionUID = -1789668324L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAdmissionCandidate admissionCandidate = new QAdmissionCandidate("admissionCandidate");

    public final EnumPath<CandidateStateType> admissionState = createEnum("admissionState", CandidateStateType.class);

    public final QAdmissionUnit admissionUnit;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.dna.rna.domain.User.QUser user;

    public QAdmissionCandidate(String variable) {
        this(AdmissionCandidate.class, forVariable(variable), INITS);
    }

    public QAdmissionCandidate(Path<? extends AdmissionCandidate> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAdmissionCandidate(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAdmissionCandidate(PathMetadata metadata, PathInits inits) {
        this(AdmissionCandidate.class, metadata, inits);
    }

    public QAdmissionCandidate(Class<? extends AdmissionCandidate> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.admissionUnit = inits.isInitialized("admissionUnit") ? new QAdmissionUnit(forProperty("admissionUnit"), inits.get("admissionUnit")) : null;
        this.user = inits.isInitialized("user") ? new com.dna.rna.domain.User.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

