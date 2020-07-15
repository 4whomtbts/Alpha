package com.dna.rna.domain.User;

import com.dna.rna.domain.Admission.AdmissionCandidate;
import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.Club.ClubUser;
import com.dna.rna.domain.School.School;
import com.dna.rna.domain.SchoolUser.SchoolUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.dna.rna.domain.School.School.SCHOOL_ID;
import static java.util.Objects.requireNonNull;

/**
 * Entity for User of RNA service.
 *
 * User.java
 * created 2020.3.25
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Getter
@Setter
@Entity
@Table(name= "USER")
@ToString(exclude = "userRoles")
public class User extends BaseAuditorEntity {

    public static final String USER_ID = "USER_ID";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = USER_ID)
    private Long id;

    //@Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = SCHOOL_ID)
    private School school;

    private boolean school_authorized = false;

    @Column(nullable = false, updatable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = true)
    private UserType userType = UserType.USER;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<SchoolUser> schoolUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ClubUser> clubUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user",
               fetch = FetchType.LAZY,
               cascade = CascadeType.ALL)
    private List<UserRole> userRoles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<AdmissionCandidate> admissionCandidates = new ArrayList<>();

    // 기본생성자를 public 으로 바꿔야 할 시 논의 후 바꿀 것
    protected User() {}

    private User(final String loginId, final String password, final String userName) {
        this.loginId = loginId;
        this.password = password;
        this.userName = userName;
    }

    public static User of(final String loginId, final String userName, final String password) {
        requireNonNull(loginId, "loginId is null");
        requireNonNull(password, "password is null");
        requireNonNull(userName, "userName is null");

        User user = new User(loginId, password, userName);
        return user;
    }
}
