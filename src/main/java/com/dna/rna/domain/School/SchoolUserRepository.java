package com.dna.rna.domain.School;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * SchoolUser의 CrudRepository
 *
 * SchoolUserRepository.java
 * created 2020.4.2
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Repository
public interface SchoolUserRepository extends CrudRepository<SchoolUser, Long> {
}
