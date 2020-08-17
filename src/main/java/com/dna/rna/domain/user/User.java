package com.dna.rna.domain.user;

import com.dna.rna.domain.BaseAuditorEntity;
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

    public static final String USER_ID = "USER_ID";

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

    @Column(nullable = false)
    private String userGroup;

    @Column(nullable = true)
    private UserType userType = UserType.USER;

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<UserRole> userRoles = new ArrayList<>();

    // 기본생성자를 public 으로 바꿔야 할 시 논의 후 바꿀 것
    protected User() {}

    private User(final String loginId, final String password, final String userName,
                 final String organization, final String userGroup) {
        this.loginId = loginId;
        this.password = password;
        this.userName = userName;
        this.organization = organization;
        this.userGroup = userGroup;
    }

    public static User of(final String loginId, final String userName, final String password,
                          final String organization, final String userGroup) {
        requireNonNull(loginId, "loginId is null");
        requireNonNull(password, "password is null");
        requireNonNull(userName, "userName is null");
        requireNonNull(organization, "organization is null");
        requireNonNull(userGroup, "userGroup is null");
        User user = new User(loginId, password, userName, organization, userGroup);
        return user;
    }
}
