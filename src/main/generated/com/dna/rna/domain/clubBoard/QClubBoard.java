package com.dna.rna.domain.clubBoard;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClubBoard is a Querydsl query type for clubBoard
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QClubBoard extends EntityPathBase<ClubBoard> {

    private static final long serialVersionUID = 445609973L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClubBoard clubBoard = new QClubBoard("clubBoard");

    public final com.dna.rna.domain.QBaseAuditorEntity _super = new com.dna.rna.domain.QBaseAuditorEntity(this);

    public final com.dna.rna.domain.board.QBoard board;

    public final com.dna.rna.domain.boardGroup.QBoardGroup boardGroup;

    public final com.dna.rna.domain.club.QClub club;

    public final NumberPath<Long> clubBoardId = createNumber("clubBoardId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> displayOrder = createNumber("displayOrder", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public QClubBoard(String variable) {
        this(ClubBoard.class, forVariable(variable), INITS);
    }

    public QClubBoard(Path<? extends ClubBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClubBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClubBoard(PathMetadata metadata, PathInits inits) {
        this(ClubBoard.class, metadata, inits);
    }

    public QClubBoard(Class<? extends ClubBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new com.dna.rna.domain.board.QBoard(forProperty("board")) : null;
        this.boardGroup = inits.isInitialized("boardGroup") ? new com.dna.rna.domain.boardGroup.QBoardGroup(forProperty("boardGroup"), inits.get("boardGroup")) : null;
        this.club = inits.isInitialized("club") ? new com.dna.rna.domain.club.QClub(forProperty("club"), inits.get("club")) : null;
    }

}

