package com.dna.rna.domain.club;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.admission.AdmissionUnit;
import com.dna.rna.domain.clubUser.ClubUser;
import com.dna.rna.domain.school.School;
import com.dna.rna.domain.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.dna.rna.domain.school.School.LEADER_ID;
import static java.util.Objects.requireNonNull;

/**
 * Entity for ClubDto of RNA service.
 *
 * school.java
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

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = School.SCHOOL_ID, nullable = false)
    private School school;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = LEADER_ID, nullable = false)
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

    @JsonManagedReference
    @OneToMany(mappedBy = "club",
               fetch = FetchType.LAZY,
               orphanRemoval = true)
    private List<ClubUser> clubUsers = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "clubAdmission",
               fetch = FetchType.LAZY,
               orphanRemoval = true)
    private List<AdmissionUnit> admissionUnits = new ArrayList<>();

    //TODO mock data 바꾸기 (uri)
    public static Club of(long schoolId, String clubName, User leader,
                          LocalDate since, String season, String location,
                          String shortDescription, String longDescription) throws IllegalArgumentException {
        School school = School.of(schoolId);
        return new Club(school, clubName, leader, since, season, location, shortDescription, longDescription, "uri");
    }

    public static Club of(School school, String clubName, User leader,
                          LocalDate since, String season, String location,
                          String shortDescription, String longDescription) throws IllegalArgumentException {

        return new Club(school, clubName, leader, since, season, location, shortDescription, longDescription, "uri");
    }

    public static Club of(long schoolId, String clubName, User leader,
                          LocalDate since, String season, String location, String shortDescription,
                          String longDescription, String profileImageUri) throws IllegalArgumentException {
        if (schoolId < 0) {
            IllegalArgumentException exception = new IllegalArgumentException("schoolId 는 음수일 수 없습니다.");
            logger.error("심각 : schoolId 는 음수일 수 없습니다.", exception);
            throw exception;
        }
        School school = School.of(schoolId);
        return new Club(school, clubName, leader, since, season, location, shortDescription, longDescription, profileImageUri);
    }

    public static Club of(School school, String clubName, User leader,
                        LocalDate since, String season, String location,
                        String shortDescription, String longDescription, String profileImageUri) throws IllegalArgumentException {
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
        return new Club(school, clubName, leader, since, season, location, shortDescription, longDescription, profileImageUri);
    }

    protected Club() {}

    protected Club(School school, String clubName, User leader,
                   LocalDate since, String season, String location,
                   String shortDescription, String longDescription, String profileImageUri) {
        this.school = school;
        this.clubName = clubName;
        this.leader = leader;
        this.since = since;
        this.season = season;
        this.location = location;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.profileImageUri = profileImageUri;
    }
}
