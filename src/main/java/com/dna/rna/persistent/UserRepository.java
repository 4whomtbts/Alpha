package com.dna.rna.persistent;

import com.dna.rna.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByLoginId(String loginId);
}
