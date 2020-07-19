package com.dna.rna.domain.School;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.Club.Club;
import com.dna.rna.domain.SchoolUser.SchoolUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Entity for School of RNA service.
 *
 * School.java
 * created 2020.3.25
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Getter
@Setter
@Entity
@Table(name= "school",
       uniqueConstraints = {
        @UniqueConstraint(columnNames = "school_name")})
public class School extends BaseAuditorEntity {

    public static final String SCHOOL_ID = "school_id";
    public static final String LEADER_ID = "leader_id";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = SCHOOL_ID)
    private Long id;

    @Column(name = "school_name", nullable = false)
    private String schoolName;

    @OneToMany(mappedBy = "school", fetch = FetchType.LAZY)
    private List<SchoolUser> schoolUsers = new ArrayList<>();

    @OneToMany(mappedBy = "school", fetch = FetchType.LAZY)
    private List<Club> schoolClubs = new ArrayList<>();

    // 기본생성자를 public 으로 바꿔야 할 시 논의 후 바꿀 것
    protected School() {}

    private School(String schoolName) {
        this.schoolName = schoolName;
    }
    private School(long schoolId) {
        this.id = schoolId;
    }

    public static School of(final String schoolName) {
        requireNonNull(schoolName);
        return new School(schoolName);
    }

    public static School of(final long schoolId) {
        return new School(schoolId);
    }
}
