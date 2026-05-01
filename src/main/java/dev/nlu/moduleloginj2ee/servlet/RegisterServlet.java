package dev.nlu.moduleloginj2ee.servlet;

import dev.nlu.moduleloginj2ee.entity.User;
import dev.nlu.moduleloginj2ee.exception.AppException;
import dev.nlu.moduleloginj2ee.service.AuthService;
import dev.nlu.moduleloginj2ee.util.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {
    private final AuthService authService = new AuthService();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("WEB-INF/views/auth/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<User> validatedUser = isInfoValidation(req, resp);
        if (validatedUser.isEmpty()) return;

        String baseUrl = req.getScheme() + "://" + req.getServerName()
                + (req.getServerPort() == 80 || req.getServerPort() == 443 ? "" : ":" + req.getServerPort())
                + req.getContextPath();

        User user = validatedUser.get();
        String password = req.getParameter("password");
        try {
            authService.registerWithCredential(user, password, baseUrl);
            req.setAttribute("message", "Tạo tài khoản thành công, kiểm tra email đế kích hoạt tài khoản");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
        } catch (Exception e) {
            if (e instanceof AppException) {
                req.setAttribute("error", e.getMessage());
                req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
        }
    }

    public Optional<User> isInfoValidation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String lastName = req.getParameter("lastName");
        String firstName = req.getParameter("firstName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPass = req.getParameter("confirm-password");

        if (lastName == null || firstName == null
                || email == null || password == null || confirmPass == null) {
            req.setAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
            req.getRequestDispatcher("WEB-INF/views/auth/register.jsp").forward(req, resp);
            return Optional.empty();
        }

        if (!EmailUtil.isValidEmail(email)) {
            req.setAttribute("error", "Email chưa chính xác.");
            req.getRequestDispatcher("WEB-INF/views/auth/register.jsp").forward(req, resp);
            return Optional.empty();
        }

        if (!password.equals(confirmPass)) {
            req.setAttribute("error", "Xác nhập mật khẩu không chính xác.");
            req.getRequestDispatcher("WEB-INF/views/auth/register.jsp").forward(req, resp);
            return Optional.empty();
        }

        User existingUser = authService.findByEmail(email);
        if (existingUser != null) {
            req.setAttribute("error", "Email đã được sử dụng.");
            req.getRequestDispatcher("WEB-INF/views/auth/register.jsp").forward(req, resp);
            return Optional.empty();
        }

        User newUser = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();

        return Optional.ofNullable(newUser);
    }
}
