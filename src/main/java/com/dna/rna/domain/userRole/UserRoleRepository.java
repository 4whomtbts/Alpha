package com.dna.rna.domain.userRole;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long>, CustomUserRoleRepository {
}
