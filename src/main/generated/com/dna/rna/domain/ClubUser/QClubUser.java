package com.dna.rna.domain.ClubUser;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClubUser is a Querydsl query type for ClubUser
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QClubUser extends EntityPathBase<ClubUser> {

    private static final long serialVersionUID = -1474606831L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClubUser clubUser = new QClubUser("clubUser");

    public final com.dna.rna.domain.QBaseAuditorEntity _super = new com.dna.rna.domain.QBaseAuditorEntity(this);

    public final com.dna.rna.domain.club.QClub club;

    public final EnumPath<ClubUserStatus> clubUserStatus = createEnum("clubUserStatus", ClubUserStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath joinSeason = createString("joinSeason");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final com.dna.rna.domain.user.QUser user;

    public QClubUser(String variable) {
        this(ClubUser.class, forVariable(variable), INITS);
    }

    public QClubUser(Path<? extends ClubUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClubUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClubUser(PathMetadata metadata, PathInits inits) {
        this(ClubUser.class, metadata, inits);
    }

    public QClubUser(Class<? extends ClubUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new com.dna.rna.domain.club.QClub(forProperty("club"), inits.get("club")) : null;
        this.user = inits.isInitialized("user") ? new com.dna.rna.domain.user.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

