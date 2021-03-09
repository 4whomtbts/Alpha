package com.dna.rna.service;

import com.dna.rna.dto.UserDto;

import javax.transaction.Transactional;

public interface UserService {

    //@Transactional
    //User createUser(final String loginId, final String userName, final String encodedPassword);

    @Transactional
    String getUserRoles(final String loginId);
    
    UserDto fetchUserMyPage(final String loginId);

}
