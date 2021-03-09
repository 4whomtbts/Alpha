package com.dna.rna;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class DCloudUtil {

    private static final Logger logger= LoggerFactory.getLogger(DCloudUtil.class);

    public static String generateRandomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 64;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String generateRandomMd5DirName(String salt) {
        String MD5 = "";
        String randomString = generateRandomString() + salt;
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(randomString.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            MD5 = sb.toString();
        }catch(NoSuchAlgorithmException e){
            logger.error("매우심각 : generateRandomMD5DirName이 알 수 없는 이유로 실패했습니다.",
                         randomString, MD5);
            MD5 = null;
        }
        return MD5;
    }
}
