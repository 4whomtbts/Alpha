package com.dna.rna.service;

import com.dna.rna.domain.allowCode.AllowCode;
import com.dna.rna.domain.allowCode.AllowCodeRepository;
import com.dna.rna.domain.allowCode.AllowCodeType;
import com.dna.rna.domain.group.Group;
import com.dna.rna.domain.group.GroupRepository;
import com.dna.rna.domain.instance.Instance;
import com.dna.rna.domain.instance.InstanceRepository;
import com.dna.rna.domain.server.Server;
import com.dna.rna.domain.server.ServerRepository;
import com.dna.rna.domain.user.User;
import com.dna.rna.domain.user.UserRepository;
import com.dna.rna.domain.userRole.UserRole;
import com.dna.rna.domain.userRole.UserRoleRepository;
import com.dna.rna.exception.DCloudException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserRoleRepository userRoleRepository;
    private final AllowCodeRepository allowCodeRepository;
    private final InstanceRepository instanceRepository;
    private final ServerRepository serverRepository;

    @Transactional
    public List<User> fetchUserList() {
        return userRepository.findAll();
    }

    @Transactional
    public List<Group> fetchAllGroup() {
        return groupRepository.findAll();
    }

    @Transactional
    public List<Instance> fetchAllInstance() {
        return instanceRepository.findAll();
    }

    @Transactional
    public List<Server> fetchAllServer() {
        return serverRepository.findAll();
    }

    @Transactional
    // ????????? instance ??? ??????????????? ???????????? ????????????(??????, ????????? ???????????? ????????? ?????????)
    // ?????? ????????? ?????? instance??? ????????? ?????? ??????, ??? ?????? instance ?????? ??????
    // ?????? ????????? ?????????????????? ???????????? ???.
    public void toggleServer(long serverId) {
        Server targetServer = serverRepository.findById(serverId).orElseThrow(() ->
                DCloudException.ofIllegalArgumentException(
                        "["+serverId+"] ??? primary key??? ?????? ????????? ???????????? ????????????."));
        boolean currentExcludeStatus = targetServer.isExcluded();
        targetServer.setExcluded(!currentExcludeStatus);
        serverRepository.save(targetServer);
    }

    @Transactional
    public void allowSignup(String loginId) {
        User targetUser = userRepository.findUserByLoginId(loginId);
        if (targetUser == null) throw DCloudException.ofIllegalArgumentException("???????????? ?????? loginId ?????????.");
        List<UserRole> userRoles = targetUser.getUserRoles();
        userRoles.add(new UserRole(targetUser, UserRole.USER_ROLE_MEMBER));
        userRoles = userRoleRepository.saveAll(userRoles);
        targetUser.setUserRoles(userRoles);
        userRepository.save(targetUser);
    }

    @Transactional
    public void allowGroupCreate(long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() ->
                DCloudException.ofIllegalArgumentException("["+groupId+"] ??? ???????????? ?????? groupId ?????????."));
        group.setGroupStatus(Group.CONFIRMED);
        groupRepository.save(group);
    }

    @Transactional
    public String getRandomUnExpiredAllowCodeAndExpiresIt() throws UnsupportedEncodingException {
        AllowCode allowCode = allowCodeRepository.findUnExpiredAllowCode();
        if (allowCode == null) {
            return "??????????????? ?????????????????????.";
        }
        allowCode.setExpired(true);
        allowCodeRepository.save(allowCode);
        return allowCode.getAllowCode();
    }

    @Transactional
    public String generateSignUpAllowCode(String loginId, AllowCodeType allowCodeType) throws UnsupportedEncodingException {

        if (loginId == null || loginId.equals(""))
            throw DCloudException.ofIllegalArgumentException("???????????? ?????? ?????? ????????? ?????????.");


        boolean alreadyGotRegisterAllowCode = false;
        User user = userRepository.findUserByLoginId(loginId);

        if (user == null)
            throw DCloudException.ofIllegalArgumentException("???????????? ?????? ?????? ????????? ?????????.");

        List<AllowCode> allowCodeList = user.getAllowCodeList();

        for (int i=0; i < allowCodeList.size(); i++) {
            AllowCode allowCode = allowCodeList.get(i);
            if (allowCode.getAllowCodeType() == AllowCodeType.ACCOUNT_REGISTRATION) {
                alreadyGotRegisterAllowCode = true;
            }
            if (alreadyGotRegisterAllowCode)
                throw DCloudException.ofIllegalArgumentException("?????? ?????? ?????? ?????? ??? ???????????????.");
        }

        AllowCode allowCode = new AllowCode(user, generateAllowCode(user), allowCodeType);
        allowCodeList.add(allowCode);
        user.setAllowCodeList(allowCodeList);
        allowCodeRepository.save(allowCode);
        userRepository.save(user);
        return allowCode.getAllowCode();
    }

    private String generateAllowCode(User user) throws UnsupportedEncodingException {
        String randomString = System.currentTimeMillis() + generateRandomString() + Integer.toString(user.hashCode());
        String sha1 = null;
        try {
            MessageDigest msdDigest = MessageDigest.getInstance("SHA-1");
            msdDigest.update(randomString.getBytes(StandardCharsets.UTF_8), 0, randomString.length());
            sha1 = DatatypeConverter.printHexBinary(msdDigest.digest());
            sha1 = sha1.substring(0, sha1.length()/2);
        } catch (NoSuchAlgorithmException e) {
            logger.error("?????? : allow code??? hashing ?????? ????????? ????????? ??????????????????. {}", e.getMessage());
        }
        return sha1;
    }

    private String generateRandomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }


}
