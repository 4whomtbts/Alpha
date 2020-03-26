package com.dna.rna.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Entity for General Board of RNA service.
 *
 * School.java
 * created 2020.3.25
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Getter
@Setter
@Entity
@Table(name="board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long boardId;

    @Column(nullable = false, updatable = false)
    private Long clubId;
    @Column(nullable = false)
    private Long boardName;
}
