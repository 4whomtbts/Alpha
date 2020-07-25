package com.dna.rna.domain.school;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSchool is a Querydsl query type for School
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSchool extends EntityPathBase<School> {

    private static final long serialVersionUID = 1212894865L;

    public static final QSchool school = new QSchool("school");

    public final com.dna.rna.domain.QBaseAuditorEntity _super = new com.dna.rna.domain.QBaseAuditorEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final ListPath<com.dna.rna.domain.club.Club, com.dna.rna.domain.club.QClub> schoolClubs = this.<com.dna.rna.domain.club.Club, com.dna.rna.domain.club.QClub>createList("schoolClubs", com.dna.rna.domain.club.Club.class, com.dna.rna.domain.club.QClub.class, PathInits.DIRECT2);

    public final StringPath schoolName = createString("schoolName");

    public final ListPath<com.dna.rna.domain.schoolUser.SchoolUser, com.dna.rna.domain.schoolUser.QSchoolUser> schoolUsers = this.<com.dna.rna.domain.schoolUser.SchoolUser, com.dna.rna.domain.schoolUser.QSchoolUser>createList("schoolUsers", com.dna.rna.domain.schoolUser.SchoolUser.class, com.dna.rna.domain.schoolUser.QSchoolUser.class, PathInits.DIRECT2);

    public QSchool(String variable) {
        super(School.class, forVariable(variable));
    }

    public QSchool(Path<? extends School> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSchool(PathMetadata metadata) {
        super(School.class, metadata);
    }

}

