package com.dna.rna.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    User findUserById(long userId);
    User findUserByLoginId(String loginId);
}
