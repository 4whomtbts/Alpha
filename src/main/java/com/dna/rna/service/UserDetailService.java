package com.dna.rna.service;

import com.dna.rna.domain.User.User;
import com.dna.rna.domain.User.UserRepository;
import com.dna.rna.security.MainUserDetails;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private static final Logger logger= LoggerFactory.getLogger(SigninService.class);

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        requireNonNull(username, "username is null");
        User user = userRepository.findUserByLoginId(username);
        MainUserDetails mainUserDetail = new MainUserDetails(user);
        requireNonNull(mainUserDetail, "mainUserDetail is null");
        return mainUserDetail;
    }
}
