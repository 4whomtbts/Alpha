package com.dna.rna.domain;

import com.dna.rna.domain.Club.Club;
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
public class Board extends BaseAuditorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "BOARD_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CLUB_ID")
    private Club club;

    @Column(nullable = false)
    private Long boardName;
}
