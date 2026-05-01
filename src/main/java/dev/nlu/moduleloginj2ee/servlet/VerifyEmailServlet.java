package dev.nlu.moduleloginj2ee.servlet;

import dev.nlu.moduleloginj2ee.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(urlPatterns = "/verify")
public class VerifyEmailServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(VerifyEmailServlet.class);
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");

        if (token == null || token.isBlank()) {
            req.setAttribute("error", "Token xác thực không hợp lệ");
            req.getRequestDispatcher("/WEB-INF/views/auth/verify_result.jsp").forward(req, resp);
            return;
        }

        try {
            boolean verified = authService.verifyEmail(token);
            if (verified) {
                req.setAttribute("success", "Xác thực email thành công. Vui lòng đăng nhập.");
                log.info("Email verified successfully with token: " + token);
            } else {
                req.setAttribute("error", "Token xác thực không tìm thấy hoặc đã hết hạn");
                log.warn("Email verification failed - token not found: " + token);
            }
        } catch (Exception e) {
            log.error("Lỗi xác thực email", e);
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
        }

        req.getRequestDispatcher("/WEB-INF/views/auth/verify_result.jsp").forward(req, resp);
    }
}



