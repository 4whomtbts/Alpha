package com.dna.rna.domain.rnaFile;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRNAFile is a Querydsl query type for RNAFile
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRNAFile extends EntityPathBase<RNAFile> {

    private static final long serialVersionUID = -368705633L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRNAFile rNAFile = new QRNAFile("rNAFile");

    public final com.dna.rna.domain.QBaseAuditorEntity _super = new com.dna.rna.domain.QBaseAuditorEntity(this);

    public final com.dna.rna.domain.article.QArticle article;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DatePath<java.time.LocalDate> expiredAt = createDate("expiredAt", java.time.LocalDate.class);

    public final StringPath fileName = createString("fileName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final NumberPath<Long> rnaFileId = createNumber("rnaFileId", Long.class);

    public final StringPath uri = createString("uri");

    public QRNAFile(String variable) {
        this(RNAFile.class, forVariable(variable), INITS);
    }

    public QRNAFile(Path<? extends RNAFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRNAFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRNAFile(PathMetadata metadata, PathInits inits) {
        this(RNAFile.class, metadata, inits);
    }

    public QRNAFile(Class<? extends RNAFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.article = inits.isInitialized("article") ? new com.dna.rna.domain.article.QArticle(forProperty("article"), inits.get("article")) : null;
    }

}

