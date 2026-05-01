package dev.nlu.moduleloginj2ee.servlet;

import dev.nlu.moduleloginj2ee.entity.User;
import dev.nlu.moduleloginj2ee.service.AuthService;
import dev.nlu.moduleloginj2ee.util.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(LoginServlet.class);
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || password == null) {
            req.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
            return;
        }

        if (!EmailUtil.isValidEmail(email)) {
            req.setAttribute("error", "Email không hợp lệ!");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
            return;
        }

        try {
            Optional<User> userOpt = authService.loginWithCredential(email, password);
            if (userOpt.isEmpty()) {
                req.setAttribute("error", "Email hoặc mật khẩu không đúng!");
                req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
                return;
            }

            User user = userOpt.get();
            if (!user.isEmailVerified()) {
                req.setAttribute("error", "Tài khoản chưa được xác thực. Vui lòng kiểm tra email để kích hoạt tài khoản.");
                req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
                return;
            }

            req.getSession().setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/home");

        } catch (Exception e) {
            log.error("Lỗi khi đăng nhập", e);
            throw new RuntimeException(e);
        }
    }
}
