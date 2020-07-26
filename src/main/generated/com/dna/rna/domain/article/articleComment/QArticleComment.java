package com.dna.rna.domain.article.articleComment;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArticleComment is a Querydsl query type for ArticleComment
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QArticleComment extends EntityPathBase<ArticleComment> {

    private static final long serialVersionUID = 28745721L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArticleComment articleComment = new QArticleComment("articleComment");

    public final com.dna.rna.domain.QBaseAuditorEntity _super = new com.dna.rna.domain.QBaseAuditorEntity(this);

    public final NumberPath<Long> articleCommentId = createNumber("articleCommentId", Long.class);

    public final com.dna.rna.domain.user.QUser author;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final BooleanPath deleted = createBoolean("deleted");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final ListPath<ArticleComment, QArticleComment> nestedComments = this.<ArticleComment, QArticleComment>createList("nestedComments", ArticleComment.class, QArticleComment.class, PathInits.DIRECT2);

    public final QArticleComment parent;

    public QArticleComment(String variable) {
        this(ArticleComment.class, forVariable(variable), INITS);
    }

    public QArticleComment(Path<? extends ArticleComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArticleComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArticleComment(PathMetadata metadata, PathInits inits) {
        this(ArticleComment.class, metadata, inits);
    }

    public QArticleComment(Class<? extends ArticleComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new com.dna.rna.domain.user.QUser(forProperty("author"), inits.get("author")) : null;
        this.parent = inits.isInitialized("parent") ? new QArticleComment(forProperty("parent"), inits.get("parent")) : null;
    }

}

