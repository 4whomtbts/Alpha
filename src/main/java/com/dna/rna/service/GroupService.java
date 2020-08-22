package com.dna.rna.service;

import com.dna.rna.domain.group.Group;
import com.dna.rna.domain.group.GroupRepository;
import com.dna.rna.domain.groupUser.GroupUser;
import com.dna.rna.domain.groupUser.GroupUserRepository;
import com.dna.rna.domain.groupUser.GroupUserType;
import com.dna.rna.domain.user.User;
import com.dna.rna.domain.user.UserRepository;
import com.dna.rna.dto.GroupDto;
import com.dna.rna.exception.DCloudException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;

    public List<GroupUser> getUserGroups(User user) {
        user = userRepository.findUserByLoginId(user.getLoginId());
        return groupUserRepository.findByUser(user);
    }

    public void createNewGroup(User user, GroupDto.GroupCreation groupCreation) {
        String groupName = groupCreation.getGroupName();
        String representative = groupCreation.getRepresentative();
        logger.info("새로운 그룹 생성 요청 user = {}, groupCreation = {}, {}",
                    user.getLoginId(), groupName, representative);

        Group group = groupRepository.findByGroupName(groupName);
        if (group != null) {
            throw DCloudException.ofIllegalArgumentException("이미 존재하는 그룹 이름입니다.");
        }

        user = userRepository.findUserByLoginId(user.getLoginId());
        Group newGroup = new Group(groupName);
        GroupUser newGroupUser = new GroupUser(user, newGroup, GroupUserType.MANAGER);
        groupRepository.save(newGroup);
        groupUserRepository.save(newGroupUser);
    }
}
