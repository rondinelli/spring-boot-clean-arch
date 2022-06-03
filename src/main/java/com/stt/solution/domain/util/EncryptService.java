package com.stt.solution.domain.util;

public interface EncryptService {
    String hashString(String strToHash);
    String encryptPassword(String password);
    Boolean matches(String rawPassword, String encryptedPass);
}
