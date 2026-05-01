package dev.nlu.moduleloginj2ee.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

public class EmailUtil {
    private static final Properties PROPERTIES = new Properties();
    private static final Logger log = LoggerFactory.getLogger(EmailUtil.class);
    private static final String RESOURCE_NAME = "application.properties";

    static {
        try (InputStream is = EmailUtil.class.getClassLoader().getResourceAsStream(RESOURCE_NAME)){
            if (is == null) {
                log.error("Error: Không tìm thấy file " + RESOURCE_NAME);
            } else {
                PROPERTIES.load(is);
            }

        } catch (IOException e) {
            log.error("Error: Không tìm thấy file " + RESOURCE_NAME, e);
        }
    }

    public static boolean sendEmail(String toEmail, String subject, String content) throws MessagingException {
        String fromEmail = PROPERTIES.getProperty("app.mail");
        String appPass = PROPERTIES.getProperty("app.pass");

        if (fromEmail == null || fromEmail.isBlank()) {
            log.debug("Debug: Thiếu from email");
            return false;
        }

        if (appPass == null || appPass.isBlank()) {
            log.debug("Debug: Thiếu app password");
            return false;
        }

        Session session = Session.getInstance(getSMTPConfig(),new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPass);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(content, "text/plain; charset=UTF-8");

            Transport.send(message);
            log.info("Email gửi thành công đến: " + toEmail);
            return true;
        } catch (MessagingException e) {
            log.error("Lỗi gửi email đến " + toEmail, e);
            throw new MessagingException("Gửi email thất bại: " + e.getMessage(), e);
        }
    }

    public static Properties getSMTPConfig(){
        Properties config = new Properties();
        config.put("mail.smtp.auth", "true");
        config.put("mail.smtp.starttls.enable", "true");
        config.put("mail.smtp.host", "smtp.gmail.com");
        config.put("mail.smtp.port", "587");
        config.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        config.put("mail.smtp.ssl.protocols", "TLSv1.2");

        return config;
    }

    public static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(regex, email);
    }
}
