package com.dna.rna.domain.project;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProject is a Querydsl query type for Project
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProject extends EntityPathBase<Project> {

    private static final long serialVersionUID = 869930919L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProject project = new QProject("project");

    public final com.dna.rna.domain.QBaseAuditorEntity _super = new com.dna.rna.domain.QBaseAuditorEntity(this);

    public final com.dna.rna.domain.club.QClub club;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> currentMember = createNumber("currentMember", Integer.class);

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final BooleanPath hiring = createBoolean("hiring");

    public final DatePath<java.time.LocalDate> hiringDueDate = createDate("hiringDueDate", java.time.LocalDate.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final com.dna.rna.domain.clubUser.QClubUser mentor;

    public final StringPath projectDescription = createString("projectDescription");

    public final NumberPath<Long> projectId = createNumber("projectId", Long.class);

    public final StringPath projectName = createString("projectName");

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final NumberPath<Integer> totalMember = createNumber("totalMember", Integer.class);

    public QProject(String variable) {
        this(Project.class, forVariable(variable), INITS);
    }

    public QProject(Path<? extends Project> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProject(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProject(PathMetadata metadata, PathInits inits) {
        this(Project.class, metadata, inits);
    }

    public QProject(Class<? extends Project> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new com.dna.rna.domain.club.QClub(forProperty("club"), inits.get("club")) : null;
        this.mentor = inits.isInitialized("mentor") ? new com.dna.rna.domain.clubUser.QClubUser(forProperty("mentor"), inits.get("mentor")) : null;
    }

}

