package com.dna.rna.domain.Admission;

import com.dna.rna.domain.User.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "admission_candidate")
public class AdmissionCandidate {

    public static final String ADMISSION_CANDIDATE_ID = "ADMISSION_CANDIDATE_ID";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = ADMISSION_CANDIDATE_ID)
    private Long id;

    @ManyToOne
    @JoinColumn(name = User.USER_ID)
    private User user;

    @ManyToOne
    @JoinColumn(name = AdmissionUnit.ADMISSION_UNIT_ID)
    private AdmissionUnit admissionUnit;

    @Enumerated(value = EnumType.STRING)
    private CandidateStateType admissionState;

}
