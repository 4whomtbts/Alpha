package com.dna.rna.domain.projectUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
}
