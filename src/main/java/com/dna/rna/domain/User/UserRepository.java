package com.dna.rna.domain.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository("UserRepository")
public interface UserRepository extends CrudRepository<User, Long> {

    @Transactional
    User findUserByLoginId(String loginId);
}
