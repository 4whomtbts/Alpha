package com.dna.rna.domain.user;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.allowCode.AllowCode;
import com.dna.rna.domain.groupUser.GroupUser;
import com.dna.rna.domain.groupUser.GroupUserType;
import com.dna.rna.domain.instance.Instance;
import com.dna.rna.domain.userRole.UserRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Entity for user of RNA service.
 *
 * user.java
 * created 2020.3.25
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */
@Getter
@Setter
@Entity
@Table(name= "user")
@ToString(exclude = "userRoles")
public class User extends BaseAuditorEntity {

    public static final String USER_ID = "user_id";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = USER_ID)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String organization;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Instance> instanceList;

    @Column(nullable = true)
    private UserType userType = UserType.USER;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL)
    private List<UserRole> userRoles = new ArrayList<>();

    @OneToMany(mappedBy = "user",
               orphanRemoval = true)
    private List<AllowCode> allowCodeList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<GroupUser> groupUserList = new ArrayList<>();

    // 기본생성자를 public 으로 바꿔야 할 시 논의 후 바꿀 것
    protected User() {}

    private User(final String loginId, final String password, final String userName,
                 final String organization) {
        this.loginId = loginId;
        this.password = password;
        this.instanceList = new ArrayList<>();
        this.userName = userName;
        this.organization = organization;
        this.allowCodeList = new ArrayList<>();
        this.groupUserList = new ArrayList<>();
    }

    public static User of(final String loginId, final String userName, final String password,
                          final String organization) {
        requireNonNull(loginId, "loginId is null");
        requireNonNull(password, "password is null");
        requireNonNull(userName, "userName is null");
        requireNonNull(organization, "organization is null");
        User user = new User(loginId, password, userName, organization);
        return user;
    }
}
