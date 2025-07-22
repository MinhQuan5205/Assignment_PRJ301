<%-- 
    Document   : guestHeader
    Created on : Jul 9, 2025, 2:09:19 PM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<header>
    <div style="width: 100%; margin: 0 auto;" class="container">
        <div style="width: 100%; display: flex; justify-content: space-between;" class="header-top">
            <div class="logo">
                <a href="${pageContext.request.contextPath}/MainController?action=homeBook"><img src="assets/img/main-logo.png" alt="Logo Website"></a>
            </div>
            <nav class="top-nav right-nav">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/MainController?action=homeBook">Trang chủ</a></li>
                    <li><a href="${pageContext.request.contextPath}/MainController?action=listBook">Sản phẩm</a></li>
                    <li><a href="signin.jsp">Đăng nhập</a></li>
                    <li><a href="signup.jsp">Đăng ký</a></li>
                </ul>
            </nav>
        </div>
    </div>
</header>
