package com.dna.rna.domain.admission;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 단체의 한 모집단위 내의 지원절차를 나타내는 Entity
 *
 * AdmissionUnit.java
 * created 2020.4.1
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Getter
@Setter
@Entity
@Table(name = "admission_unit")
public class AdmissionUnit {

    public static final String ADMISSION_UNIT_ID = "ADMISSION_UNIT_ID";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = ADMISSION_UNIT_ID)
    private Long id;

    @ManyToOne
    @JoinColumn(name = ClubAdmission.CLUB_ADMISSION_UNIT_ID)
    private ClubAdmission clubAdmission;

    // 지원 과정에서 몇 번 째인지 저장하는 컬럼
    @Column(nullable = false)
    private int admissionOrder;

    // 현재 절차의 이름
    @Column(nullable = false)
    private String admissionUnitTitle;

    // 현재 절차에 대한 소개
    @Column(nullable = false)
    private String admissionUnitDescription;

    @OneToMany(mappedBy = "admissionUnit")
    private List<AdmissionCandidate> admissionCandidateList = new ArrayList<>();

}
