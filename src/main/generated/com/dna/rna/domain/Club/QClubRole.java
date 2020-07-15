package com.dna.rna.domain.Club;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClubRole is a Querydsl query type for ClubRole
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QClubRole extends EntityPathBase<ClubRole> {

    private static final long serialVersionUID = -1105599545L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClubRole clubRole = new QClubRole("clubRole");

    public final com.dna.rna.domain.QBaseAuditorEntity _super = new com.dna.rna.domain.QBaseAuditorEntity(this);

    public final QClub club;

    public final NumberPath<Long> club_role_id = createNumber("club_role_id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final StringPath role_type = createString("role_type");

    public final com.dna.rna.domain.User.QUser user;

    public QClubRole(String variable) {
        this(ClubRole.class, forVariable(variable), INITS);
    }

    public QClubRole(Path<? extends ClubRole> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClubRole(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClubRole(PathMetadata metadata, PathInits inits) {
        this(ClubRole.class, metadata, inits);
    }

    public QClubRole(Class<? extends ClubRole> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new QClub(forProperty("club"), inits.get("club")) : null;
        this.user = inits.isInitialized("user") ? new com.dna.rna.domain.User.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

