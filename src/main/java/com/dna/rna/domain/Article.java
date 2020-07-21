package com.dna.rna.domain;

import com.dna.rna.domain.board.Board;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Entity for General Article of RNA service.
 *
 * school.java
 * created 2020.3.25
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Getter
@Setter
@Entity
@Table(name="article")
public class Article extends BaseAuditorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ARTICLE_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID", nullable = false)
    private Board board;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false,
            columnDefinition = "integer default 0")
    private int viewCount;

    @Column(nullable = false,
            columnDefinition = "integer default 0")
    private int voteCount;

    @Column(nullable = false,
            columnDefinition = "integer default 0")
    private int scrapCount;

}
