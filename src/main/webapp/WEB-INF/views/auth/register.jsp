<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 4/28/2026
  Time: 7:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký tài khoản</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth/register.css">
</head>
<body>

<div class="register-container">
    <h2>Tạo tài khoản mới</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-error">
            <c:out value="${error}" />
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/register" method="POST">

        <div class="form-group">
            <label for="lastName">Họ</label>
            <input type="text" id="lastName" name="lastName"
                   value="${not empty param.lastName ? param.lastName : ''}"
                   placeholder="Nhập họ của bạn" required autofocus>
        </div>

        <div class="form-group">
            <label for="firstName">Tên</label>
            <input type="text" id="firstName" name="firstName"
                   value="${not empty param.firstName ? param.firstName : ''}"
                   placeholder="Nhập tên của bạn" required autofocus>
        </div>

        <div class="form-group">
            <label for="email">Địa chỉ Email</label>
            <input type="email" id="email" name="email"
                   value="${not empty param.email ? param.email : ''}"
                   placeholder="Nhập địa chỉ email" required>
        </div>

        <div class="form-group">
            <label for="password">Mật khẩu</label>
            <input type="password" id="password" name="password"
                   placeholder="Tạo mật khẩu" minlength="6" required>
        </div>

        <div class="form-group">
            <label for="confirm-password">Nhập lại mật khẩu</label>
            <input type="password" id="confirm-password" name="confirm-password"
                   placeholder="Nhập lại mật khẩu" minlength="6" required>
        </div>

        <button type="submit" class="btn-submit">Đăng ký</button>
    </form>

    <div class="links">
        <p>Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập tại đây</a></p>
    </div>
</div>

</body>
</html>
