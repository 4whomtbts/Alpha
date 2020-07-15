package com.dna.rna.domain.ClubUser;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.Club.Club;
import com.dna.rna.domain.User.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static com.dna.rna.domain.Club.Club.CLUB_ID;
import static com.dna.rna.domain.User.User.USER_ID;

@Getter
@Setter
@Entity
@Table(name = "club_user")
public class ClubUser extends BaseAuditorEntity {

    public static final String CLUB_USER_ID = "club_user_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = CLUB_USER_ID)
    private Long id;

    @ManyToOne
    @JoinColumn(name = USER_ID, nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = CLUB_ID, nullable = false)
    private Club club;

    @Column(name = "join_season", nullable = true)
    private int joinSeason;

    @Convert(converter = ClubeUserStatusConverter.class)
    private ClubUserStatus status;

}
