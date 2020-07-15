package com.dna.rna.domain.SchoolUser;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.School.School;
import com.dna.rna.domain.User.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static com.dna.rna.domain.School.School.SCHOOL_ID;
import static com.dna.rna.domain.User.User.USER_ID;
import static java.util.Objects.requireNonNull;

/**
 * Entity for School of RNA service.
 *
 * SchoolUser.java
 * created 2020.7.14
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Getter
@Setter
@Entity
@Table(name = "school_user")
public class SchoolUser extends BaseAuditorEntity {

    public static final String SCHOOL_USER_ID = "school_user_id";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = SCHOOL_USER_ID)
    private Long id;

    @ManyToOne
    @JoinColumn(name = USER_ID, nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = SCHOOL_ID, nullable = false)
    private School school;

    @JoinColumn(name = "admin")
    private boolean admin = false;

    @ElementCollection
    @CollectionTable(name = "school_user_role",
            joinColumns = @JoinColumn(name = SCHOOL_USER_ID, nullable = true))
    private Set<SchoolUserPermissions> schoolUserPermissions = new HashSet<>();

    private SchoolUser() {}

    private SchoolUser(final User user, final School school, final boolean admin) {
        this.user = user;
        this.school = school;
        this.admin = admin;
    }

    public static SchoolUser of(final User user, final School school, final boolean admin) {
        requireNonNull(user, "User shouldn't be null for SchoolUser constructor");
        requireNonNull(school, "School shouldn't be null for SchoolUser constructor");
        requireNonNull(admin, "Admin shouldn't be null for SchoolUser constructor");
        return new SchoolUser(user, school, admin);
    }

}
