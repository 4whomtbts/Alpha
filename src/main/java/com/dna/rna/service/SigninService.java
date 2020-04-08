package com.dna.rna.service;

import com.dna.rna.domain.User.User;
import com.dna.rna.domain.User.UserRepository;
import com.dna.rna.domain.User.UserRole;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service("SinginService")
@NoArgsConstructor
public class SigninService {

    private static final Logger logger = LoggerFactory.getLogger(SigninService.class);

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public List<GrantedAuthority> getAuthorities(final String loginId) {
        User newUser = userRepository.findUserByLoginId("jun");
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

}
