package com.dna.rna.domain.Club;

import com.dna.rna.domain.Admission.AdmissionUnit;
import com.dna.rna.domain.School.School;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    public static final String CLUB_ID = "club_id";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = CLUB_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = School.SCHOOL_ID, nullable = false)
    private School school;

    @Column(nullable = false)
    private String clubName;

    // 단체의 기수를 나타내는 컬럼
    @Column(nullable = false)
    private String season;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String shortDescription;

    @Column(nullable = false)
    private String longDescription;

    @OneToMany(mappedBy = "club")
    private List<ClubUser> clubUsers = new ArrayList<>();

    @OneToMany(mappedBy = "clubAdmission")
    private List<AdmissionUnit> admissionUnits = new ArrayList<>();

}
