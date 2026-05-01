<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 4/29/2026
  Time: 1:04 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Đăng nhập hệ thống</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth/login.css">
</head>
<body>

<div class="login-container">
  <h2>Đăng nhập</h2>

  <c:if test="${not empty error}">
    <div class="alert alert-error">
      <c:out value="${error}" />
    </div>
  </c:if>

  <c:if test="${not empty message}">
    <div class="alert alert-success">
      <c:out value="${message}" />
    </div>
  </c:if>

  <form action="${pageContext.request.contextPath}/login" method="POST">
    <div class="form-group">
      <label for="email">Email</label>
      <input type="email" id="email" name="email"
             value="${not empty param.email ? param.email : ''}"
             placeholder="Nhập địa chỉ email" required autofocus>
    </div>

    <div class="form-group">
      <label for="password">Mật khẩu</label>
      <input type="password" id="password" name="password"
             placeholder="Nhập mật khẩu" required>
    </div>

    <c:if test="${captchaQuestion != null and captchaQuestion ne ''}">
      <div class="captcha-box">
        <div class="captcha-header">
          <label for="g-recaptcha-response">Xác minh bảo mật</label>
          <span class="captcha-badge">Giải phép tính: <strong><c:out value="${captchaQuestion}" /></strong> = ?</span>
        </div>
        <input type="text" id="g-recaptcha-response" name="g-recaptcha-response"
               placeholder="Nhập kết quả" required>
      </div>
    </c:if>

    <button type="submit" class="btn-submit">Đăng nhập</button>
  </form>

  <div class="oauth-divider"><span>Hoặc</span></div>

  <a class="btn-google" href="${pageContext.request.contextPath}/auth/google">
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 48 48" width="20px" height="20px">
      <path fill="#FFC107" d="M43.611,20.083H42V20H24v8h11.303c-1.649,4.657-6.08,8-11.303,8c-6.627,0-12-5.373-12-12c0-6.627,5.373-12,12-12c3.059,0,5.842,1.154,7.961,3.039l5.657-5.657C34.046,6.053,29.268,4,24,4C12.955,4,4,12.955,4,24c0,11.045,8.955,20,20,20c11.045,0,20-8.955,20-20C44,22.659,43.862,21.35,43.611,20.083z"/>
      <path fill="#FF3D00" d="M6.306,14.691l6.571,4.819C14.655,15.108,18.961,12,24,12c3.059,0,5.842,1.154,7.961,3.039l5.657-5.657C34.046,6.053,29.268,4,24,4C16.318,4,9.656,8.337,6.306,14.691z"/>
      <path fill="#4CAF50" d="M24,44c5.166,0,9.86-1.977,13.409-5.192l-6.19-5.238C29.211,35.091,26.715,36,24,36c-5.202,0-9.619-3.317-11.283-7.946l-6.522,5.025C9.505,39.556,16.227,44,24,44z"/>
      <path fill="#1976D2" d="M43.611,20.083H42V20H24v8h11.303c-0.792,2.237-2.231,4.166-4.087,5.571c0.001-0.001,0.002-0.001,0.003-0.002l6.19,5.238C36.971,39.205,44,34,44,24C44,22.659,43.862,21.35,43.611,20.083z"/>
    </svg>
    Đăng nhập với Google
  </a>

  <div class="links">
    <p><a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a></p>
    <p>Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a></p>
  </div>
</div>

</body>
</html>