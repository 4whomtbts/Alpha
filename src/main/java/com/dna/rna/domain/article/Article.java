package com.dna.rna.domain.article;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.board.Board;
import com.dna.rna.domain.rnaFile.RNAFile;
import com.dna.rna.domain.user.User;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

import java.util.List;

import static com.dna.rna.domain.board.Board.BOARD_ID;
import static com.dna.rna.domain.user.User.USER_ID;
import static java.util.Objects.requireNonNull;


@Getter
@Entity
@Table(name = "article")
public class Article extends BaseAuditorEntity {
    // ! 필드명 바꿀 시 ArticleRepositoryImpl 의 정렬 컬럼명도 같이 변경해주어야 함.

    private static final Logger logger = LoggerFactory.getLogger(Article.class);

    public static final String ARTICLE_ID = "article_id";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = ARTICLE_ID)
    private long articleId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = USER_ID, nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = BOARD_ID, nullable = false)
    private Board board;

    @OneToMany(mappedBy = "article", orphanRemoval = true)
    @Column(name = "attached_files", nullable = false)
    private List<RNAFile> attachedFiles;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "vote_count")
    private int voteCount;


    public static Article of(User author, Board board, List<RNAFile> attachedFiles, String title, String content) {
        requireNonNull(author, "Article의 생성자에서 author는 null이 될 수 없습니다.");
        requireNonNull(board, "Article의 생성자에서 board는 null이 될 수 없습니다.");
        requireNonNull(attachedFiles, "Article의 생성자에서 attachedFiles는 null이 될 수 없습니다.");
        requireNonNull(title, "Article의 생성자에서 title은 null이 될 수 없습니다.");
        requireNonNull(content, "Article의 생성자에서 content는 null이 될 수 없습니다.");
        if (title.equals("")) {
            throw new IllegalArgumentException("Article의 생성자에서 title은 공백문자일 수 없습니다.");
        }
        if (content.equals("")) {
            throw new IllegalArgumentException("Article의 생성자에서 content는 공백문자일 수 없습니다.");
        }

        return new Article(author, board, attachedFiles, title, content);
    }

    private Article() {}

    private Article(User author, Board board, List<RNAFile> attachedFiles, String title, String content) {

        this.author = author;
        this.board = board;
        this.attachedFiles = attachedFiles;
        this.title = title;
        this.content = content;
        this.viewCount = 0;
        this.voteCount = 0;
    }
}
