package com.dna.rna.domain.schoolUser;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.school.School;
import com.dna.rna.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static com.dna.rna.domain.school.School.SCHOOL_ID;
import static com.dna.rna.domain.user.User.USER_ID;
import static java.util.Objects.requireNonNull;

/**
 * Entity for school of RNA service.
 *
 * schoolUser.java
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
    private long id;

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

    protected SchoolUser() {}

    private SchoolUser(final User user, final School school, final boolean admin) {
        this.user = user;
        this.school = school;
        this.admin = admin;
    }

    public static SchoolUser of(final User user, final School school, final boolean admin) {
        requireNonNull(user, "user shouldn't be null for schoolUser constructor");
        requireNonNull(school, "school shouldn't be null for schoolUser constructor");
        requireNonNull(admin, "Admin shouldn't be null for schoolUser constructor");
        return new SchoolUser(user, school, admin);
    }

}
