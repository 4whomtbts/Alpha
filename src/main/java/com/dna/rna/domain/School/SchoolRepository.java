package com.dna.rna.domain.School;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long>, CustomSchoolRepository {

    public School findSchoolBySchoolName(String schoolName);
}
