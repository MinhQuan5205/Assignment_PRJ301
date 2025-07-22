<%-- 
    Document   : userPage
    Created on : Jul 14, 2025, 8:45:32 PM
    Author     : ACER
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="modelDAO.UserDAO" %>
<%@ page import="modelDTO.UserDTO" %>

<%
    String accID = (String) session.getAttribute("accID");

    if (accID == null) {
        response.sendRedirect("signin.jsp"); 
        return;
    }

    UserDAO udao = new UserDAO();
    UserDTO account = udao.getUserById(accID); 

    if (account == null) {
        String errorMessage = "Không tìm thấy thông tin tài khoản.";
        request.setAttribute("errorMessage", errorMessage);
    } else {
        request.setAttribute("account", account);
    }
%>



<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Trang Cá Nhân</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css"/>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&family=Nunito:ital,wght@0,200..1000;1,200..1000&family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" integrity="sha512-9usAa10IRO0HhonpyAIVpjrylPvoDwiPUiKdWk5t3PyolY1cOd4DSE0Ga+ri4AuTroPR5aQvXU9xC6qOPnzFeg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    </head>
    <body>
        <c:choose>
            <c:when test="${empty sessionScope.acc}">
                <c:redirect url="MainController?action=homeBook"/>
            </c:when>
            <c:otherwise>
                <jsp:include page="userHeader.jsp" />
                <section id="profile" class="smooth-foam-section">
                    <div class="container">
                        <div class="foam-content">
                            <h2>THÔNG TIN CÁ NHÂN</h2>
                            <c:if test="${not empty errorMessage}">
                                <p style="color:red;">${errorMessage}</p>
                            </c:if>
                            <c:if test="${not empty account}">
                                <p><strong>Tên khách hàng:</strong> ${account.name}</p>
                                <p><strong>Email:</strong> ${account.email}</p> 
                                <p><strong>Số điện thoại:</strong> ${account.phone}</p>
                                <div class="foam-actions">
                                    <a href="${pageContext.request.contextPath}/MainController?action=listOrder" class="btn btn-secondary">Xem đơn hàng</a>
                                    <a href="edituser.jsp" class="view-ingredients">Chỉnh sửa thông tin ></a>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </section>
                <jsp:include page="footer.jsp" />
                <script src="${pageContext.request.contextPath}/assets/js/home.js"></script>
            </c:otherwise>
        </c:choose>
    </body>
</html>
