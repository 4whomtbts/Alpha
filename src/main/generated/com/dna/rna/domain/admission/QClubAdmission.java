package com.dna.rna.domain.admission;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClubAdmission is a Querydsl query type for ClubAdmission
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QClubAdmission extends EntityPathBase<ClubAdmission> {

    private static final long serialVersionUID = 841180689L;

    public static final QClubAdmission clubAdmission = new QClubAdmission("clubAdmission");

    public final StringPath admissionCode = createString("admissionCode");

    public final StringPath admissionTitle = createString("admissionTitle");

    public final ListPath<AdmissionUnit, QAdmissionUnit> admissionUnits = this.<AdmissionUnit, QAdmissionUnit>createList("admissionUnits", AdmissionUnit.class, QAdmissionUnit.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> totalAdmissionStep = createNumber("totalAdmissionStep", Integer.class);

    public QClubAdmission(String variable) {
        super(ClubAdmission.class, forVariable(variable));
    }

    public QClubAdmission(Path<? extends ClubAdmission> path) {
        super(path.getType(), path.getMetadata());
    }

    public QClubAdmission(PathMetadata metadata) {
        super(ClubAdmission.class, metadata);
    }

}

