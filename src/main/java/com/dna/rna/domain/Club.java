package com.dna.rna.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Entity for Club of RNA service.
 *
 * School.java
 * created 2020.3.25
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Getter
@Setter
@Entity
@Table(name="club")
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long clubId;
    @Column(nullable = false, updatable = false)
    private Long schoolId;

    @Column(nullable = false)
    private String clubName;


}
