package dev.nlu.moduleloginj2ee.service;

import dev.nlu.moduleloginj2ee.entity.User;
import dev.nlu.moduleloginj2ee.entity.UserCredential;
import dev.nlu.moduleloginj2ee.util.PasswordUtil;
import dev.nlu.moduleloginj2ee.util.TokenUtil;
import jakarta.mail.MessagingException;

import java.time.LocalDateTime;
import java.util.Optional;

public class AuthService {
    private final UserService userService = new UserService();
    private final EmailService emailService = new EmailService();

    public void registerWithCredential(User user, String plainPassword, String verifyBaseUrl) throws MessagingException {
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        String verificationToken = TokenUtil.generateSecureToken();

        UserCredential credential = UserCredential.builder()
                .user(user)
                .passwordHash(hashedPassword)
                .emailVerified(false)
                .verificationToken(verificationToken)
                .verificationSentAt(LocalDateTime.now())
                .verificationAttempts(1)
                .build();

        user.setCredentials(credential);
        userService.createNewUser(user);
        emailService.sendVerification(
                user.getEmail(),
                verificationToken,
                user.getFirstName() + " " + user.getLastName(),
                verifyBaseUrl);
    }

    public User findByEmail(String email) {
        return userService.findByEmail(email);
    }

    public Optional<User> loginWithCredential(String email, String plainPassword) throws Exception {
        User user = userService.findByEmail(email);
        if (user == null || user.getCredentials() == null) {
            throw new Exception("Email hoặc mật khẩu không đúng");
        }

        if (!user.getCredentials().isEmailVerified()) {
            throw new Exception("Tài khoản chưa được xác thực, vui lòng kiểm tra email để kích hoạt tài khoản");
        }

        boolean passwordMatch = PasswordUtil.checkPassword(plainPassword, user.getCredentials().getPasswordHash());
        if (!passwordMatch) {
            throw new Exception("Email hoặc mật khẩu không đúng");
        }

        return Optional.of(user);
    }

    public boolean verifyEmail(String token) {
        return userService.verifyEmailByToken(token);
    }
}
