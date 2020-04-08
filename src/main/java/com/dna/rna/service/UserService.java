package com.dna.rna.service;

import com.dna.rna.domain.CRUDPermissions;
import com.dna.rna.domain.User.User;
import com.dna.rna.domain.User.UserRepository;
import com.dna.rna.domain.User.UserRole;
import com.dna.rna.domain.User.UserType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger= LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Transactional
    public User createUser(String loginId, String userName, String encodedPassword) {
        User newUser = User.of(loginId, userName, encodedPassword);
        UserRole newUserRole = new UserRole();
        List<CRUDPermissions> permissionsList = new ArrayList<>();
        permissionsList.add(CRUDPermissions.CREATE);
        newUserRole.setPermissions(permissionsList);
        newUserRole.setUser(newUser);
        newUserRole.setRoleName(UserType.USER.toString());
        List<UserRole> userRoles = new ArrayList<>();
        userRoles.add(newUserRole);
        newUser.setUserRoles(userRoles);
        userRepository.save(newUser);

        User user = userRepository.findUserByLoginId(loginId);
        for (UserRole role : user.getUserRoles()) {
            System.out.println(role.getRoleName());
        }

        return newUser;
    }

    @Transactional
    public String getUserRoles(String loginId) {
        User user = userRepository.findUserByLoginId(loginId);
        if(user.getUserRoles() != null) {
            for (int i=0; i < user.getUserRoles().size(); i++) {
                return user.getUserRoles().get(i).getRoleName();
            }
        }
        return user.getUserRoles().get(0).getRoleName();
    }
}
