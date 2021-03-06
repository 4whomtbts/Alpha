package com.dna.rna.service;

import com.dna.rna.domain.user.User;
import com.dna.rna.domain.user.UserRepository;
import com.dna.rna.domain.userRole.UserRole;
import com.dna.rna.dto.SignupForm;
import com.dna.rna.exception.DCloudException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service("SinginService")
@RequiredArgsConstructor
public class SigninService {

    private static final Logger logger = LoggerFactory.getLogger(SigninService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public List<GrantedAuthority> getAuthorities(final String loginId) {
        User newUser = userRepository.findUserByLoginId(loginId);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(newUser.getUserType().toString()));
        List<UserRole> userRoles = newUser.getUserRoles();
        for (UserRole role : userRoles) {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getAuthority());
            for (int j = 0; j < role.getPermissions().size(); j++) {
                GrantedAuthority permission = new SimpleGrantedAuthority("OP_" + role.getPermissions().get(j));
                authorities.add(permission);
            }
            authorities.add(authority);
        }
        return authorities;
    }

    @Transactional
    public void SignUp(SignupForm signupForm) throws DCloudException {
        String loginId= signupForm.getLoginId();
        signupForm.setPassword(passwordEncoder.encode(signupForm.getPassword()));

        if(userRepository.findUserByLoginId(loginId) != null) {
            throw DCloudException.ofIllegalArgumentException("???????????? ?????????????????????.");
        }

        User user = signupForm.toUserEntity();
        User newUser = userRepository.save(user);
        List<UserRole> userRoleList = new ArrayList<>();
        //userRoleList.add(new UserRole(newUser, UserRole.USER_ROLE_MEMBER));
        //userRoleList = userRoleRepository.saveAll(userRoleList);
        newUser.setUserRoles(userRoleList);
        userRepository.save(newUser);

        logger.info("????????? ?????? '{}' ??? ?????????????????????", user.getLoginId());
    }

}
