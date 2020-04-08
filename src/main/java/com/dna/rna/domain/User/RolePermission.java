package com.dna.rna.domain.User;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "role_permission")
public class RolePermission implements GrantedAuthority {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


    @Override
    public String getAuthority() {
        return null;
    }
}
