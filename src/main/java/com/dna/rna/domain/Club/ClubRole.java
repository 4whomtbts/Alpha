package com.dna.rna.domain.Club;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.User.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Entity for club's role of RNA service.
 *
 * ClubRole.java
 * created 2020.3.27
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Getter
@Setter
@Entity
@Table(name="club_role")
public class ClubRole extends BaseAuditorEntity {

    public static final String MEMBER = "MEMBER";
    public static final String STAFF = "STAFF";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long club_role_id;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false, updatable = false)
    private Club club;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(nullable = false)
    private String role_type;

}
