package com.stt.solution.domain.util;


import java.util.Map;

public interface EmailService {
    void sendMail(String email, String subject, String templateName) ;
    void sendMail(String email, String subject, String templateName, Map<String, String> context) ;
}
