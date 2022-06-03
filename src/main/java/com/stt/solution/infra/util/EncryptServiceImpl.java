package com.stt.solution.infra.util;

import com.stt.solution.domain.util.EncryptService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class EncryptServiceImpl implements EncryptService {
    private final BCryptPasswordEncoder encoder;

    public EncryptServiceImpl(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String hashString(String str) {
        try {
            String random = this.randomString(10);
            str = str + random ;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            byte[] digested = digest.digest(bytes);
            return this.bytesToHex(digested);
        } catch (NoSuchAlgorithmException e) {
            return this.unsecureHash(str);
        }
    }

    private String randomString(int size) {
        byte[] array = new byte[size];
        new Random().nextBytes(array);
        return this.bytesToHex(array);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xff & aByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Override
    public String encryptPassword(String password) {
        return encoder.encode(password);
    }

    @Override
    public Boolean matches(String passwordToCompare, String encryptedPass) {
        return encoder.matches(passwordToCompare, encryptedPass);
    }

    private String unsecureHash(String toHash) {
        Random random = new Random();
        byte[] bytes = toHash.getBytes(StandardCharsets.UTF_8);
        byte[] newBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            int d1 = random.nextInt();
            int d2 = random.nextInt();
            byte b = bytes[i];
            int a = b & d1;
            a = a | d2;
            newBytes[i] = (byte) a;
        }
        return this.bytesToHex(newBytes);
    }
}
