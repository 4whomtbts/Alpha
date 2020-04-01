package com.dna.rna.domain.School;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.User.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 유저와 학교 Entity 간의 join table
 *
 * SchoolUser.java
 * created 2020.4.2
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Getter
@Setter
@Entity
@Table(name = "school_user")
public class SchoolUser extends BaseAuditorEntity {

    private static final String SCHOOL_USER_ID = "SCHOOL_USER_ID";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = SCHOOL_USER_ID)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "school", nullable = false)
    private School school;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    private User user;



}
