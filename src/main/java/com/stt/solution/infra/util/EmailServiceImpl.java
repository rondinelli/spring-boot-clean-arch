package com.stt.solution.infra.util;


import com.stt.solution.domain.util.EmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {
    private final Session session;
    private final String from;
    private final Configuration configuration;

    public EmailServiceImpl(Configuration configuration, SmtpConfiguration smtpConfiguration) {
        this.configuration = configuration;
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.transport.protocol", "smtp");

        prop.put("mail.smtp.host", smtpConfiguration.getHost());
        prop.put("mail.smtp.port", smtpConfiguration.getPort());
        prop.put("mail.smtp.ssl.trust", smtpConfiguration.getHost());

        // TLS
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

        this.session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpConfiguration.getUser(), smtpConfiguration.getPass());
            }
        });
        this.from = smtpConfiguration.getFrom();
    }

    @Override
    public void sendMail(String email, String subject, String templateName) {
        try {
            String template = this.createTemplate(templateName);
            Message msg = this.createMessage(email, subject, template);
            this.threadedEmail(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMail(@NotNull String email, @NotNull String subject, @NotNull String templateName, Map<String, String> context) {
        try {
            String template = this.createTemplate(templateName, context);
            Message mg = this.createMessage(email, subject, template);
            this.threadedEmail(mg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void threadedEmail(Message msg) {
        Runnable sendMail = () -> {
            try {
                Transport.send(msg);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        };
        new Thread(sendMail).start();
    }

    private String createTemplate(String templateName) throws IOException {
        Template tm = configuration.getTemplate(templateName);
        StringWriter writer = new StringWriter();
        tm.dump(writer);
        return writer.toString();
    }

    private String createTemplate(String templateName, Map<String, String> context) throws IOException, TemplateException {
        Template template = configuration.getTemplate(templateName);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, context);
    }

    private Message createMessage(String to, String subject, String strOrTemplate) throws MessagingException {
        Message message = new MimeMessage(this.session);
        message.setFrom(new InternetAddress(this.from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);

        MimeBodyPart mimeBody = new MimeBodyPart();
        mimeBody.setContent(strOrTemplate, "text/html; charset=UTF-8");

        Multipart mt = new MimeMultipart();
        mt.addBodyPart(mimeBody);
        message.setContent(mt);

        return message;
    }
}
