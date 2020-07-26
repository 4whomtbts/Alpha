package com.dna.rna.domain.article.articleComment;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.article.Article;
import com.dna.rna.domain.user.User;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.dna.rna.domain.article.Article.ARTICLE_ID;
import static com.dna.rna.domain.user.User.USER_ID;
import static java.util.Objects.requireNonNull;

@Getter
@Setter
@Entity
@Table(name = "article_comment")
public class ArticleComment extends BaseAuditorEntity {

    private static final Logger logger = LoggerFactory.getLogger(ArticleComment.class);

    public static final String ARTICLE_COMMENT_ID = "article_comment_id";
    public static final String PARENT_ARTICLE_COMMENT_ID = "parent_article_comment_id";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = ARTICLE_COMMENT_ID)
    private long articleCommentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = USER_ID)
    private User author;

    @Column(name = "content", nullable = false)
    private String content;

    // 대댓글의 경우 Article은 null이다.
    @Nullable
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ARTICLE_ID, nullable = true)
    private Article article;

    @Nullable
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = PARENT_ARTICLE_COMMENT_ID, nullable = true)
    private ArticleComment parent;

    // 부모 댓글이 지워지더라도 자식 댓글들은 지워지면 안 되므로
    // orphanRemoval을 명시적으로 false로 선언한다.
    @OneToMany(mappedBy = "parent", orphanRemoval = false)
    @Column(nullable = false)
    private List<ArticleComment> nestedComments;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    private ArticleComment() {}

    private ArticleComment(@Nullable Article article, User author, String content, ArticleComment parent) {
        this.article = article;
        this.author = author;
        this.content = content;
        this.parent = parent;
        this.deleted = false;
        this.nestedComments = new ArrayList<>();
    }

    public static ArticleComment of(Article article, User author, String content, @Nullable ArticleComment parent) {
        requireNonNull(article, "ArticleComment 생성자의 article은 null이 될 수 없습니다.");
        requireNonNull(author, "ArticleComment 생성자의 author는 null이 될 수 없습니다.");
        requireNonNull(content, "ArticleComment 생성자의 content는 null이 될 수 없습니다.");
        return new ArticleComment(article, author, content, parent);
    }

    public static ArticleComment ofNestedComment(String content, @NonNull ArticleComment parent) {
        requireNonNull(content, "ofNestedComment의 parent null이 될 수 없습니다.");
        return new ArticleComment(null, parent.getAuthor(), content, parent);
    }

}
