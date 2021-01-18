package com.dna.rna.domain.userRole;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class UserRoleRepositoryImpl extends QuerydslRepositorySupport implements CustomUserRoleRepository {

    public UserRoleRepositoryImpl() {
        super(UserRoleRepositoryImpl.class);
    }
}
