package com.dna.rna.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Entity for User of RNA service.
 *
 * User.java
 * created 2020.3.25
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Getter
@Setter
@Entity
@Table(name= "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    @Column(nullable = false)
    private Long schoolId;

    @Column(nullable = false, updatable = false)
    private String loginId;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean superAdmin;
    @Column(nullable = false)
    private String userName;



}
