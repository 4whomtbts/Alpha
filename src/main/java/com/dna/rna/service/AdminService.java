package com.dna.rna.service;

import com.dna.rna.domain.allowCode.AllowCode;
import com.dna.rna.domain.allowCode.AllowCodeRepository;
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

    private final AllowCodeRepository allowCodeRepository;

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
    public void generateNnewAllowCodeAndSave(int n) throws UnsupportedEncodingException {
        List<AllowCode> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(new AllowCode(generateAllowCode()));
        }
        allowCodeRepository.saveAll(result);
    }

    private String generateAllowCode() throws UnsupportedEncodingException {
        String randomString = System.currentTimeMillis() + generateRandomString();
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
