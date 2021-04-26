package com.dna.rna.domain.userRole;

import com.dna.rna.domain.CRUDPermissions;
import com.dna.rna.domain.user.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "user_role")
public class UserRole implements GrantedAuthority {

    public static final String USER_ROLE_MEMBER = "MEMBER";
    public static final String USER_ROLE_ADMIN = "ADMIN";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "ROLE_NAME")
    private String roleName;

    @ElementCollection
    @CollectionTable(name = "PERMISSIONS",
        joinColumns = @JoinColumn(name = "USER_ROLE_ID"))
    private List<CRUDPermissions> permissions = new ArrayList<>();

    protected UserRole() {}

    public UserRole(User user, String roleName) {
        this.user = user;
        this.roleName = roleName;
    }
    @Override
        public String getAuthority() {
            return roleName;
    }
}
