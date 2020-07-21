package com.dna.rna.domain.projectUser;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProjectUser is a Querydsl query type for ProjectUser
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProjectUser extends EntityPathBase<ProjectUser> {

    private static final long serialVersionUID = -1978370691L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProjectUser projectUser = new QProjectUser("projectUser");

    public final com.dna.rna.domain.QBaseAuditorEntity _super = new com.dna.rna.domain.QBaseAuditorEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final BooleanPath leader = createBoolean("leader");

    public final com.dna.rna.domain.project.QProject project;

    public final StringPath projectUserId = createString("projectUserId");

    public final com.dna.rna.domain.user.QUser user;

    public QProjectUser(String variable) {
        this(ProjectUser.class, forVariable(variable), INITS);
    }

    public QProjectUser(Path<? extends ProjectUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProjectUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProjectUser(PathMetadata metadata, PathInits inits) {
        this(ProjectUser.class, metadata, inits);
    }

    public QProjectUser(Class<? extends ProjectUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.project = inits.isInitialized("project") ? new com.dna.rna.domain.project.QProject(forProperty("project"), inits.get("project")) : null;
        this.user = inits.isInitialized("user") ? new com.dna.rna.domain.user.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

