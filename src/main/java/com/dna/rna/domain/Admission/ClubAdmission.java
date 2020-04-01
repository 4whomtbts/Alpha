package com.dna.rna.domain.Admission;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 동아리의 한 모집단위를 나타내는 Entity
 *
 * ClubAdmission.java
 * created 2020.4.1
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Getter
@Setter
@Entity
@Table(name = "club_admission")
public class ClubAdmission {

    public static final String CLUB_ADMISSION_UNIT_ID = "CLUB_ADMISSION_UNIT_ID";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = CLUB_ADMISSION_UNIT_ID)
    private Long id;

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false, updatable = false)
    private String admissionCode;

    @Column(nullable = false)
    private int totalAdmissionStep;

    @Column(nullable =  false)
    private String admissionTitle;

    @OneToMany(mappedBy = "clubAdmission")
    private List<AdmissionUnit> admissionUnits = new ArrayList<>();

}
