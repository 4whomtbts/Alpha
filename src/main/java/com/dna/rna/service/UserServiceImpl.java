
package com.dna.rna.service;

import com.dna.rna.domain.user.User;
import com.dna.rna.domain.user.UserRepository;
import com.dna.rna.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger= LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Transactional
    @Override
    public String getUserRoles(final String loginId) {
        User user = userRepository.findUserByLoginId(loginId);
        if(user.getUserRoles() != null) {
            for (int i=0; i < user.getUserRoles().size(); i++) {
                return user.getUserRoles().get(i).getRoleName();
            }
        }
        return user.getUserRoles().get(0).getRoleName();
    }

    @Override
    public UserDto fetchUserMyPage(final String loginId) {
        User user = userRepository.findUserByLoginId(loginId);
        return null;
    }
}
