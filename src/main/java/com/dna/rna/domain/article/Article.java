package com.dna.rna.domain.article;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.board.Board;
import com.dna.rna.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

import static com.dna.rna.domain.board.Board.BOARD_ID;
import static com.dna.rna.domain.user.User.USER_ID;
import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "article")
public class Article extends BaseAuditorEntity {

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

    public static Article of(User author, Board board) {
        return new Article(author, board);
    }

    private Article() {}

    private Article(User author, Board board) {
        requireNonNull(author, "Article의 생성자 author는 null이 될 수 없습니다.");
        requireNonNull(board, "Article의 생성자 board는 null이 될 수 없습니다.");
        this.author = author;
        this.board = board;
    }

}
