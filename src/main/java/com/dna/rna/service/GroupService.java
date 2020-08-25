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

    public User getUser(User user) {
        return userRepository.findUserByLoginId(user.getLoginId());
    }

    public List<GroupUser> getUserGroups(User user) {
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

    // 귀찮아서 일단 초대하면 바로 가입되도록 함..
    public void inviteUserToGroup(long groupId, String loginId) {
        User user = userRepository.findUserByLoginId(loginId);
        Group group = groupRepository.findById(groupId).orElseThrow(() ->
                DCloudException.ofIllegalArgumentException("["+groupId+"] 는 존재하지 않는 유저입니다."));
        GroupUser groupUser = new GroupUser(user, group, GroupUserType.MEMBER);
        groupUserRepository.save(groupUser);
    }

    public void confirmGroupCreation(long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않은 groupId = [" + groupId + "]  가 입력 되었습니다."));
        group.setGroupStatus(Group.CONFIRMED);
        groupRepository.save(group);

    }
}
