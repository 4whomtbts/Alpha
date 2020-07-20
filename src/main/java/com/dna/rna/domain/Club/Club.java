package com.dna.rna.domain.Club;

import com.dna.rna.domain.Admission.AdmissionUnit;
import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.ClubUser.ClubUser;
import com.dna.rna.domain.School.School;
import com.dna.rna.domain.User.User;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.dna.rna.domain.School.School.LEADER_ID;
import static java.util.Objects.requireNonNull;

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
@Table(name = "club")
public class Club extends BaseAuditorEntity {

    private static final Logger logger= LoggerFactory.getLogger(Club.class);

    public static final String CLUB_ID = "club_id";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = CLUB_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = School.SCHOOL_ID, nullable = false)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = LEADER_ID)
    private User leader;

    @Column(nullable = false)
    private String clubName;

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate since;

    // 단체의 기수를 나타내는 컬럼
    @Column(nullable = false)
    private String season;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String shortDescription;

    @Column(nullable = false)
    private String longDescription;

    @Column(name = "profile_image_uri")
    private String profileImageUri;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY)
    private List<ClubUser> clubUsers = new ArrayList<>();

    @OneToMany(mappedBy = "clubAdmission", fetch = FetchType.LAZY)
    private List<AdmissionUnit> admissionUnits = new ArrayList<>();

    public static Club of(long schoolId, String clubName, LocalDate since, String season,
                          String location, String shortDescription, String longDescription, String profileImageUri) throws IllegalArgumentException {
        if (schoolId < 0) {
            IllegalArgumentException exception = new IllegalArgumentException("schoolId 는 음수일 수 없습니다.");
            logger.error("심각 : schoolId 는 음수일 수 없습니다.", exception);
            throw exception;
        }
        School school = School.of(schoolId);
        return new Club(school, clubName, since, season, location, shortDescription, longDescription, profileImageUri);
    }

    public static Club of(School school, String clubName, LocalDate since, String season,
                           String location, String shortDescription, String longDescription, String profileImageUri) throws IllegalArgumentException {
        requireNonNull(school, "school은 null이 될 수 없습니다.");
        requireNonNull(clubName, "clubName은 null이 될 수 없습니다.");
        requireNonNull(since, "since는 null이 될 수 없습니다.");
        requireNonNull(season, "season은 null이 될 수 없습니다.");
        requireNonNull(location, "location은 null이 될 수 없습니다.");
        requireNonNull(shortDescription, "shortDescription은 null이 될 수 없습니다.");
        requireNonNull(longDescription, "longDescription은 null이 될 수 없습니다.");
        if (clubName.equals("")) {
            throw new IllegalArgumentException("동아리명은 공백 문자일 수 없습니다.");
        }
        if (season.equals("")) {
            throw new IllegalArgumentException("기수는 공백 문자일 수 없습니다.");
        }
        return new Club(school, clubName, since, season, location, shortDescription, longDescription, profileImageUri);
    }

    private Club() {}

    protected Club(School school, String clubName, LocalDate since, String season,
                   String location, String shortDescription, String longDescription, String profileImageUri) {
        this.school = school;
        this.clubName = clubName;
        this.since = since;
        this.season = season;
        this.location = location;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.profileImageUri = profileImageUri;
    }
}
