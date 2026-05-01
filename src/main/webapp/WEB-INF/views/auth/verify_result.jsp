<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 4/29/2026
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác thực Email</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth/register.css">
    <style>
        .verify-container {
            max-width: 400px;
            margin: 100px auto;
            padding: 30px;
            border: 1px solid #ddd;
            border-radius: 8px;
            text-align: center;
            background-color: #f9f9f9;
        }

        .verify-container h2 {
            margin-bottom: 20px;
        }

        .alert {
            padding: 12px;
            margin-bottom: 20px;
            border-radius: 4px;
        }

        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .alert-error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .btn-login {
            display: inline-block;
            padding: 10px 30px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            margin-top: 20px;
        }

        .btn-login:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

<div class="verify-container">
    <h2>Xác thực Email</h2>

    <c:if test="${not empty success}">
        <div class="alert alert-success">
            <c:out value="${success}" />
        </div>
        <a href="${pageContext.request.contextPath}/login" class="btn-login">Đăng nhập</a>
    </c:if>

    <c:if test="${not empty error}">
        <div class="alert alert-error">
            <c:out value="${error}" />
        </div>
        <p>
            <a href="${pageContext.request.contextPath}/register">Quay lại đăng ký</a>
        </p>
    </c:if>
</div>

</body>
</html>

