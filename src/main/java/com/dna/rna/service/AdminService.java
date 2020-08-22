package com.dna.rna.service;

import com.dna.rna.domain.allowCode.AllowCode;
import com.dna.rna.domain.allowCode.AllowCodeRepository;
import com.dna.rna.domain.allowCode.AllowCodeType;
import com.dna.rna.domain.user.User;
import com.dna.rna.domain.user.UserRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

@Service
@RequiredArgsConstructor
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    private final UserRepository userRepository;
    private final AllowCodeRepository allowCodeRepository;

    public List<User> fetchUserList() {
        return userRepository.findAll();
    }

    @Transactional
    public String getRandomUnExpiredAllowCodeAndExpiresIt() throws UnsupportedEncodingException {
        AllowCode allowCode = allowCodeRepository.findUnExpiredAllowCode();
        if (allowCode == null) {
            return "허가코드가 고갈되었습니다.";
        }
        allowCode.setExpired(true);
        allowCodeRepository.save(allowCode);
        return allowCode.getAllowCode();
    }

    @Transactional
    public String generateSignUpAllowCode(String loginId, AllowCodeType allowCodeType) throws UnsupportedEncodingException {

        if (loginId == null || loginId.equals(""))
            throw DCloudException.ofIllegalArgumentException("유효하지 않은 유저 아이디 입니다.");


        boolean alreadyGotRegisterAllowCode = false;
        User user = userRepository.findUserByLoginId(loginId);

        if (user == null)
            throw DCloudException.ofIllegalArgumentException("존재하지 않은 유저 아이디 입니다.");

        List<AllowCode> allowCodeList = user.getAllowCodeList();

        for (int i=0; i < allowCodeList.size(); i++) {
            AllowCode allowCode = allowCodeList.get(i);
            if (allowCode.getAllowCodeType() == AllowCodeType.ACCOUNT_REGISTRATION) {
                alreadyGotRegisterAllowCode = true;
            }
            if (alreadyGotRegisterAllowCode)
                throw DCloudException.ofIllegalArgumentException("이미 허가 하에 가입 된 유저입니다.");
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
            logger.error("심각 : allow code를 hashing 하는 도중에 예외가 발생했습니다. {}", e.getMessage());
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
