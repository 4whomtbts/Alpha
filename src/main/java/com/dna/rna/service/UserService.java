package com.dna.rna.service;

import com.dna.rna.domain.User.User;

import javax.transaction.Transactional;

public interface UserService {

    @Transactional
    User createUser(String loginId, String userName, String encodedPassword);

    @Transactional
    String getUserRoles(String loginId);

}
