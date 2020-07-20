package com.dna.rna.domain.SchoolUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolUserRepository extends JpaRepository<SchoolUser, Long>, CustomSchoolUserRepository {
}
