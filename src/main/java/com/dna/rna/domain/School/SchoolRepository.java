package com.dna.rna.domain.School;

import com.dna.rna.domain.User.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * School의 CrudRepository
 *
 * SchoolRepository.java
 * created 2020.4.2
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Repository
public interface SchoolRepository extends CrudRepository<School, Long> {

    School findSchoolBySchoolName(String schoolName);

}
