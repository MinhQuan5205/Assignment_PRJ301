<%-- 
    Document   : signup
    Created on : Jul 8, 2025, 4:47:09 PM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Page Signup</title>
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
                <div class="login-container container">
                    <div class="image-section">
                        <img src="https://i.pinimg.com/736x/84/da/ed/84daed30323885c90895698abcb1cae3.jpg" alt="bookImg">
                    </div>
                    <div class="form-section">
                        <h1>TẠO TÀI KHOẢN</h1>
                        <form action="MainController" method="post" class="form-part">
                            <input type="hidden" name="action" value="signup"/>

                            <span style="color:red;">${sessionScope.error_name}</span>
                            <input name="name" type="text" placeholder="Tên của bạn"
                                   value="${sessionScope.old_name != null ? sessionScope.old_name : ''}" required>

                            <span style="color:red;">${sessionScope.error_accID}</span>
                            <input name="accID" type="text" placeholder="Tên tài khoản"
                                   value="${sessionScope.old_accID != null ? sessionScope.old_accID : ''}" required>

                            <span style="color:red;">${sessionScope.error_email}</span>
                            <input name="email" type="text" placeholder="Email"
                                   value="${sessionScope.old_email != null ? sessionScope.old_email : ''}" required>

                            <span style="color:red;">${sessionScope.error_password}</span>
                            <input name="password" type="password" placeholder="Mật khẩu" required>

                            <span style="color:red;">${sessionScope.error_phone}</span>
                            <input name="phone" type="text" placeholder="Số điện thoại"
                                   value="${sessionScope.old_phone != null ? sessionScope.old_phone : ''}" required>

                            <button style="margin-top: 0" type="submit" class="login-btnn">ĐĂNG KÝ</button>
                        </form>
                        <c:if test="${sessionScope.errorsu != null}">
                            <p style="color: red; margin-bottom: 0px; margin-top: 1rem;">${sessionScope.errorsu}</p>
                        </c:if>
                        <p>Đã có tài khoản ? <a href="signin.jsp" class="signup login-btnn-signup">Đăng Nhập</a></p>
                    </div>
                </div>
                <c:remove var="error_name" scope="session"/>
                <c:remove var="error_accID" scope="session"/>
                <c:remove var="error_email" scope="session"/>
                <c:remove var="error_password" scope="session"/>
                <c:remove var="error_phone" scope="session"/>

                <c:remove var="old_name" scope="session"/>
                <c:remove var="old_accID" scope="session"/>
                <c:remove var="old_email" scope="session"/>
                <c:remove var="old_phone" scope="session"/>

                <script src="${pageContext.request.contextPath}/assets/js/signupscript.js"></script>
            </c:otherwise>
        </c:choose>
    </body>
</html>
