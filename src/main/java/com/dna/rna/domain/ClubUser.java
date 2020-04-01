package com.dna.rna.domain;

import com.dna.rna.domain.User.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Club과 User를 위한 Join table. JPA @manytomany 사용 회피를 위해 생성.
 *
 * ClubUser.java
 * created 2020.4.1
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Getter
@Setter
@Entity
@Table(name = "club_user")
public class ClubUser extends BaseAuditorEntity {

    private static final String CLUB_USER_ID = "CLUB_USER_ID";

    @Id @GeneratedValue
    @Column(name = CLUB_USER_ID)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "CLUB_ID")
    private Club club;

}
