package com.stt.solution.infra.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "smtp")
@Configuration("SmptConfiguration")
@Getter
@Setter
public class SmtpConfiguration {
    private String host;
    private String port;
    private String user;
    private String pass;
    private String from;
}
