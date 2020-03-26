package com.dna.rna.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity for General Article of RNA service.
 *
 * School.java
 * created 2020.3.25
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Getter
@Setter
@Entity
@Table(name="article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long articleId;
    @Column(nullable = false, updatable = false)
    private Long boardId;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private int viewCount;
    @Column(nullable = false)
    private int voteCount;
    @Column(nullable = false)
    private int scrapCount;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime modifiedAt;
    @Column(nullable = false)
    private LocalDateTime deletedAt;
}
