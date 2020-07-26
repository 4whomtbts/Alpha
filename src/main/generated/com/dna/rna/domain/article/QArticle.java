package com.dna.rna.domain.article;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArticle is a Querydsl query type for Article
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QArticle extends EntityPathBase<Article> {

    private static final long serialVersionUID = 879619681L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArticle article = new QArticle("article");

    public final com.dna.rna.domain.QBaseAuditorEntity _super = new com.dna.rna.domain.QBaseAuditorEntity(this);

    public final NumberPath<Long> articleId = createNumber("articleId", Long.class);

    public final ListPath<com.dna.rna.domain.rnaFile.RNAFile, com.dna.rna.domain.rnaFile.QRNAFile> attachedFiles = this.<com.dna.rna.domain.rnaFile.RNAFile, com.dna.rna.domain.rnaFile.QRNAFile>createList("attachedFiles", com.dna.rna.domain.rnaFile.RNAFile.class, com.dna.rna.domain.rnaFile.QRNAFile.class, PathInits.DIRECT2);

    public final com.dna.rna.domain.user.QUser author;

    public final com.dna.rna.domain.board.QBoard board;

    public final ListPath<com.dna.rna.domain.article.articleComment.ArticleComment, com.dna.rna.domain.article.articleComment.QArticleComment> comments = this.<com.dna.rna.domain.article.articleComment.ArticleComment, com.dna.rna.domain.article.articleComment.QArticleComment>createList("comments", com.dna.rna.domain.article.articleComment.ArticleComment.class, com.dna.rna.domain.article.articleComment.QArticleComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final StringPath title = createString("title");

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public final NumberPath<Integer> voteCount = createNumber("voteCount", Integer.class);

    public QArticle(String variable) {
        this(Article.class, forVariable(variable), INITS);
    }

    public QArticle(Path<? extends Article> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArticle(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArticle(PathMetadata metadata, PathInits inits) {
        this(Article.class, metadata, inits);
    }

    public QArticle(Class<? extends Article> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new com.dna.rna.domain.user.QUser(forProperty("author"), inits.get("author")) : null;
        this.board = inits.isInitialized("board") ? new com.dna.rna.domain.board.QBoard(forProperty("board")) : null;
    }

}

