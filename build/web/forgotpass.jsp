<%-- 
    Document   : forgotpass
    Created on : Jul 9, 2025, 3:09:40 PM
    Author     : ACER
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&family=Nunito:ital,wght@0,200..1000;1,200..1000&family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/signup.css">

    </head>
    <body>
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <c:redirect url="MainController?action=homeBook"/>
            </c:when>
            <c:otherwise>
                <div style="width: 600px; aspect-ratio: 11/9.5;" class="login-container container">
                    <div style="width: 100%;" class="form-section">
                        <h1>ĐỔI MẬT KHẨU</h1>
                        <form action="MainController" method="post" class="form-part">
                            <input type="hidden" name="action" value="forgotPass">
                            <input type="text" name="accID" placeholder="Tên tài khoản" required>
                            <input type="email" name="email" placeholder="Email của bạn" required>
                            <input type="text" name="phone" placeholder="Số điện thoại của bạn" required>
                            <input type="password" name="password" placeholder="Mật khẩu mới" required>
                            <input type="password" name="password2" placeholder="Nhập lại mật khẩu" required>
                            <button type="submit" class="login-btnn">ĐỔI MẬT KHẨU</button>
                        </form>
                        <c:if test="${sessionScope.errorfp != null}">
                            <p style="color: red; margin-bottom: 0px; margin-top: 1rem;">${sessionScope.errorfp}</p>
                        </c:if>
                        <p style="margin-bottom: 0.3rem;">Chưa có tài khoản? <a href="signup.jsp" class="signup register-btn">Đăng Ký</a></p>
                        <p style="margin-top: 0px;">Đã có tài khoản? <a href="signin.jsp" class="signup login-btn-signup">Đăng Nhập</a></p>
                    </div>
                </div>
                <script src="${pageContext.request.contextPath}/assets/js/signupscript.js"></script>
            </c:otherwise>
        </c:choose>
    </body>
</html>
