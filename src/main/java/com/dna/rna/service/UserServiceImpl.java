
package com.dna.rna.service;

import com.dna.rna.domain.user.User;
import com.dna.rna.domain.user.UserRepository;
import com.dna.rna.domain.user.UserType;
import com.dna.rna.dto.UserDto;
import com.dna.rna.exception.DCloudException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
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

    @Override
    public void deleteUser(final long userId) {
        User targetUser =  userRepository.findUserById(userId);
        boolean isAdmin = targetUser.getUserType().equals(UserType.ADMIN);
        if (isAdmin)
            throw DCloudException.ofIllegalArgumentException("관리자 계정은 웹에서 삭제할 수 없습니다.",
                    "관리자 계정 : " + targetUser + "에 대해 삭제요청이 들어왔습니다");
        int numOfInstances = targetUser.getInstanceList().size();
        if (numOfInstances > 0) {
            throw DCloudException.ofIllegalArgumentException("잔여 인스턴스가 있어 유저를 삭제할 수 없습니다.",
                    "삭제되지 않은 인스턴스 " + numOfInstances +"개가 남아있습니다");
        }
        userRepository.delete(targetUser);
    }
}
