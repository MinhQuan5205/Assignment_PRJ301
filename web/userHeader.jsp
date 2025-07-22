<%-- 
    Document   : userHeader
    Created on : Jul 9, 2025, 2:09:07 PM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<header style="padding: 5px 0;">
    <div style="width: 100%; margin: 0 auto;" class="container">
        <div style="width: 100%; display: flex; justify-content: space-between;" class="header-top">
            <div class="logo">
                <a href="${pageContext.request.contextPath}/MainController?action=homeBook"><img src="assets/img/main-logo.png" alt="Logo Website"></a>
            </div>
            <nav class="top-nav right-nav">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/MainController?action=homeBook">Trang Chủ</a></li>
                    <li><a href="${pageContext.request.contextPath}/MainController?action=listBook">Sản Phẩm</a></li>
                    <li><a href="cartPage.jsp"><i class="fas fa-shopping-bag"></i></a></li>
                    <li><a href="userPage.jsp"><i class="far fa-user"></i></a></li>
                    <li><a href="${pageContext.request.contextPath}/MainController?action=signout">Đăng Xuất</a></li>
                </ul>
            </nav>
        </div>
    </div>
</header>
