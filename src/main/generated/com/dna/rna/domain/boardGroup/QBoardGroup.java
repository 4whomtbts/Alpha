package com.dna.rna.domain.boardGroup;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardGroup is a Querydsl query type for BoardGroup
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBoardGroup extends EntityPathBase<BoardGroup> {

    private static final long serialVersionUID = 1890926961L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardGroup boardGroup = new QBoardGroup("boardGroup");

    public final com.dna.rna.domain.QBaseAuditorEntity _super = new com.dna.rna.domain.QBaseAuditorEntity(this);

    public final NumberPath<Long> boardGroupId = createNumber("boardGroupId", Long.class);

    public final StringPath boardGroupName = createString("boardGroupName");

    public final com.dna.rna.domain.Club.QClub club;

    public final ListPath<com.dna.rna.domain.ClubBoard.ClubBoard, com.dna.rna.domain.ClubBoard.QClubBoard> clubBoards = this.<com.dna.rna.domain.ClubBoard.ClubBoard, com.dna.rna.domain.ClubBoard.QClubBoard>createList("clubBoards", com.dna.rna.domain.ClubBoard.ClubBoard.class, com.dna.rna.domain.ClubBoard.QClubBoard.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> displayOrder = createNumber("displayOrder", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public QBoardGroup(String variable) {
        this(BoardGroup.class, forVariable(variable), INITS);
    }

    public QBoardGroup(Path<? extends BoardGroup> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardGroup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardGroup(PathMetadata metadata, PathInits inits) {
        this(BoardGroup.class, metadata, inits);
    }

    public QBoardGroup(Class<? extends BoardGroup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new com.dna.rna.domain.Club.QClub(forProperty("club"), inits.get("club")) : null;
    }

}

