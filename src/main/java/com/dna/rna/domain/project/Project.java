package com.dna.rna.domain.project;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.club.Club;
import com.dna.rna.domain.ClubUser.ClubUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDate;

import static com.dna.rna.domain.club.Club.CLUB_ID;
import static com.dna.rna.domain.ClubUser.ClubUser.CLUB_USER_ID;
import static java.util.Objects.requireNonNull;

@Getter
@Setter
@Entity
@Table(name = "project")
public class Project extends BaseAuditorEntity {

    public static final String PROJECT_ID = "project_id";

    @Id @GeneratedValue(strategy=  GenerationType.SEQUENCE)
    @Column(name = PROJECT_ID)
    private long projectId;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "project_description", nullable = false)
    private String projectDescription;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = CLUB_USER_ID)
    private ClubUser mentor;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = CLUB_ID)
    private Club club;

    @Column(name = "start_date",
            nullable = false,
            columnDefinition = "DATE")
    private LocalDate startDate;

    @Column(name = "end_date",
            nullable = false,
            columnDefinition = "DATE")
    private LocalDate endDate;

    @Column(name ="hiring_due_date",
            nullable = true,
            columnDefinition = "DATE")
    private LocalDate hiringDueDate;

    @Column(name ="hiring",
            nullable = false)
    private Boolean hiring;

    @Column(name = "current_member")
    private int currentMember;

    @Column(name = "total_member")
    private int totalMember;

    public static Project of(String projectName, String projectDescription, Club club,
                             ClubUser mentor, int totalMember, LocalDate startDate, LocalDate endDate) {
        return new Project(projectName, projectDescription, club, mentor,
                           totalMember, startDate, endDate);
    }

    protected Project() {}

    // 파라미터 유효성 검증
    private Project(String projectName, String projectDescription, Club club,
                    ClubUser mentor, int totalMember, LocalDate startDate, LocalDate endDate) {
        requireNonNull(projectName, "project는 는 null이 될 수 없습니다.");
        requireNonNull(projectDescription, "projectDescription은 는 null이 될 수 없습니다.");
        requireNonNull(club, "club은 는 null이 될 수 없습니다.");
        requireNonNull(mentor, "mentor는 null이 될 수 없습니다.");
        requireNonNull(startDate, "startDate는 는 null이 될 수 없습니다.");
        requireNonNull(endDate, "endDate는 null이 될 수 없습니다.");

        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.club = club;
        this.totalMember = totalMember;
        this.startDate = startDate;
        this.endDate = endDate;
        this.mentor = mentor;
        this.hiring = false;
    }
}
