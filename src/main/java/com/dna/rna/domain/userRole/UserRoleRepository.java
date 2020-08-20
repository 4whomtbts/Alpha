package com.dna.rna.domain.userRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public interface UserRoleRepository extends JpaRepository<UserRole, Long>, CustomUserRoleRepository {
}
