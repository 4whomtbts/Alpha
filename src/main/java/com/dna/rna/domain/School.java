package com.dna.rna.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Entity for School of RNA service.
 *
 * School.java
 * created 2020.3.25
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Getter
@Setter
@Entity
@Table(name="school")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bno;

    @Column(nullable = false)
    private String schoolName;
}
