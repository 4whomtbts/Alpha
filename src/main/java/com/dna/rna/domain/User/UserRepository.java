package com.dna.rna.domain.User;

import com.dna.rna.domain.User.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByLoginId(String loginId);
}
