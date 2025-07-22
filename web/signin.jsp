<%-- 
    Document   : signin
    Created on : Jul 9, 2025, 12:42:04 PM
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
        <title>Page Login</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/signin.css"/>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&family=Nunito:ital,wght@0,200..1000;1,200..1000&family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap" rel="stylesheet">
    </head>
    <body>
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <c:redirect url="MainController?action=homeBook"/>
            </c:when>
            <c:otherwise>
                <div class="login-container container">
                    <div class="form-section">
                        <h1>ĐĂNG NHẬP</h1>
                        <form action="MainController" method="post" class="form-part">
                            <!--Dòng này giúp sau khi gửi form submit thì giúp biết được họ sẽ đi về đâu-->
                            <% String redirect = request.getParameter("redirect") != null ? request.getParameter("redirect") : ""; %>
                            <input type="hidden" name="redirect" value="<%= redirect %>">

                            <input type="hidden" name="action" value="signin">

                            <input type="text" name="accID" placeholder="Tên tài khoản" required>
                            <input type="password" name="password" placeholder="Mật khẩu" required>
                            <label style="display: inline-flex; align-items: center; cursor: pointer;">
                                <input type="checkbox" name="checklogin" value="rem" style="margin-right: 5px;">
                                <small>Ghi nhớ tôi</small>
                            </label>
                            <div class="options">
                                <a href="forgotpass.jsp" class="forgot">Quên mật khẩu?</a>
                            </div>
                            <button type="submit" class="login-btnn">ĐĂNG NHẬP</button>
                        </form>
                        <c:if test="${sessionScope.errorsi != null}">
                            <p style="color: red; margin-bottom: 0px; margin-top: 1rem;">${sessionScope.errorsi}</p>
                        </c:if>
                        <c:if test="${sessionScope.success != null}">
                            <p style="color: green; margin-bottom: 0px; margin-top: 1rem;">${sessionScope.success}</p>
                        </c:if>
                        <p>Chưa có tài khoản? <a href="signup.jsp" class="signup register-btn">Đăng Ký</a></p>
                    </div>
                    <div class="image-section">
                        <img src="https://i.pinimg.com/736x/84/da/ed/84daed30323885c90895698abcb1cae3.jpg" alt="bookImg">
                    </div>
                </div>
                <c:if test="${not empty sessionScope.alert}">
                    <script>
                        alert("${sessionScope.alert}");
                    </script>
                </c:if>
                <c:remove var="errorsi" scope="session"/>
                <c:remove var="success" scope="session"/>
                <script src="${pageContext.request.contextPath}/assets/js/signinscript.js"></script>
            </c:otherwise>
        </c:choose>
    </body>
</html>
