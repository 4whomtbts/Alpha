package com.dna.rna.domain.ClubUser;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.club.Club;
import com.dna.rna.domain.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

import static com.dna.rna.domain.club.Club.CLUB_ID;
import static com.dna.rna.domain.user.User.USER_ID;
import static java.util.Objects.requireNonNull;

@Getter
@Data
@Setter
@ToString
@Entity
@Table(name = "club_user")
public class ClubUser extends BaseAuditorEntity {

    private static final Logger logger= LoggerFactory.getLogger(ClubUser.class);

    public static final String CLUB_USER_ID = "club_user_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = CLUB_USER_ID)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = USER_ID, nullable = false)
    private User user;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = CLUB_ID, nullable = false)
    private Club club;

    @Column(name = "join_season")
    private String joinSeason;

    @Convert(converter = ClubUserStatusConverter.class)
    @Column(name = "club_user_status_code", nullable = true)
    private ClubUserStatus clubUserStatus = ClubUserStatus.ACTIVE;

    public static ClubUser of(User user, Club club, String joinSeason, ClubUserStatus status) throws IllegalArgumentException {
        requireNonNull(user, "user는 null이 될 수 없습니다");
        requireNonNull(club, "club은 null이 될 수 없습니다.");
        requireNonNull(joinSeason, "joinSeason은 null이 될 수 없습니다.");
        if (joinSeason.equals("")) {
            IllegalArgumentException exception =
                    new IllegalArgumentException("기수는 공백문자열이 될 수 없습니다.");
            logger.error("심각 : 기수는 공백 문자열이 될 수 없습니다.", exception);
            throw exception;
        }
        return new ClubUser(user, club, joinSeason, status);
    }

    protected ClubUser() {}

    private ClubUser(User user, Club club, String joinSeason, ClubUserStatus status) {
        this.user = user;
        this.club = club;
        this.joinSeason = joinSeason;
        this.clubUserStatus = status;
    }

}
