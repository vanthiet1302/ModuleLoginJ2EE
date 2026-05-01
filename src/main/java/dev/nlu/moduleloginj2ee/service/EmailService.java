package dev.nlu.moduleloginj2ee.service;

import dev.nlu.moduleloginj2ee.exception.AppException;
import dev.nlu.moduleloginj2ee.util.EmailUtil;
import jakarta.mail.MessagingException;

public class EmailService {

    public void sendVerification(String toEmail, String verificationToken, String fullName, String verifyBaseUrl) throws MessagingException {
        String verifyLink = verifyBaseUrl + "/verify?token=" + verificationToken;
        String emailContent = "Chào " + fullName + ",\n\n"
                + "Vui lòng xác thực tài khoản của bạn bằng cách nhấn vào liên kết sau:\n"
                + verifyLink + "\n\n"
                + "Nếu bạn không tạo tài khoản này, hãy bỏ qua email này.";

        boolean isSent = EmailUtil.sendEmail(toEmail, "Xác thực tài khoản", emailContent);
        if (!isSent) {
            throw new AppException("Đăng ký thành công nhưng không gửi được email xác thực");
        }
    }

}
