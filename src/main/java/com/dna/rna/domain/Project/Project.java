package com.dna.rna.domain.Project;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.Club.Club;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static com.dna.rna.domain.Club.Club.CLUB_ID;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = CLUB_ID)
    private Club club;

    public static Project of(String projectName, String projectDescription, Club club) {
        return new Project(projectName, projectDescription, club);
    }

    private Project(String projectName, String projectDescription, Club club) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.club = club;
    }
}
