package com.dna.rna.service;

import com.dna.rna.domain.SchoolUser.SchoolUserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolUserService {

    private static final Logger logger= LoggerFactory.getLogger(SchoolUserService.class);
    private final SchoolUserRepositoryImpl schoolUserRepository;

    public List<GrantedAuthority> getAuthorities(final String loginId) {
        return null;
    }
}
