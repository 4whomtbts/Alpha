package com.dna.rna.domain.projectUser;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.project.Project;
import com.dna.rna.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static com.dna.rna.domain.project.Project.PROJECT_ID;
import static com.dna.rna.domain.user.User.USER_ID;
import static java.util.Objects.requireNonNull;

@Getter
@Setter
@Entity
@Table(name = "project_user")
public class ProjectUser extends BaseAuditorEntity {

    public static final String PROJECT_USER_ID = "project_user_id";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = PROJECT_USER_ID)
    private String projectUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = PROJECT_ID)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = USER_ID)
    private User user;

    @Column(name = "leader")
    private Boolean leader;

    public static ProjectUser of(Project project, User user, Boolean leader) {
        requireNonNull(project, "project는 null이 될 수 없습니다.");
        requireNonNull(user, "user는 null이 될 수 없습니다.");
        requireNonNull(leader, "leader는 null이 될 수 없습니다");
        return new ProjectUser(project, user, leader);
    }

    protected ProjectUser() {}

    private ProjectUser(Project project, User user, Boolean leader) {
        this.project = project;
        this.user = user;
        this.leader = leader;
    }
}
