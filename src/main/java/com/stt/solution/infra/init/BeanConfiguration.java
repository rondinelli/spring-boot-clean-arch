package com.stt.solution.infra.init;


import com.stt.solution.domain.repository.UserRepository;
import com.stt.solution.domain.service.DomainUserService;
import com.stt.solution.domain.util.EmailService;
import com.stt.solution.domain.util.EncryptService;
import com.stt.solution.infra.util.EncryptServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration
public class BeanConfiguration {

    @Bean
    public EncryptService encryptService() {

        return new EncryptServiceImpl(new BCryptPasswordEncoder());
    }

//    @Bean
//    UserService userService(UserRepository userRepository, EncryptService encryptService, EmailService emailService) {
//        return new DomainUserService(userRepository, encryptService, emailService);
//    }

    @Bean
    public Boolean disableSSLValidation() throws Exception {
        final SSLContext sslContext = SSLContext.getInstance("TLS");

        sslContext.init(null, new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, null);

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        return true;
    }
}
