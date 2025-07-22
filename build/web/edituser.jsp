<%-- 
    Document   : edituser
    Created on : Jul 14, 2025, 3:37:47 PM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chỉnh sửa thông tin cá nhân</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/edituser.css">
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
            <c:when test="${not empty sessionScope.acc}">
                <jsp:include page="userHeader.jsp" />
                <section>
                    <div style="margin-top: 30px;" class="container2">
                        <h2>CHỈNH SỬA THÔNG TIN CÁ NHÂN</h2>
                        <c:if test="${not empty errorMessage}">
                            <p style="color:red;" class="error-message">${errorMessage}</p>
                        </c:if>
                        <c:if test="${not empty updateMessage}">
                            <p style="color:green;">${updateMessage}</p>
                        </c:if>

                        <form action="MainController" method="post">
                            <input type="hidden" name="action" value="updateUser">
                            <input type="hidden" name="accID" value="${account.accID}"/>
                            <div class="form-group">
                                <label for="name">Tên:</label>
                                <input type="text" id="name" name="name" value="${account.name}" required>
                            </div>
                            <div class="form-group">
                                <label for="email">Email:</label>
                                <input type="email" id="email" name="email" value="${account.email}" required>
                            </div>
                            <div class="form-group">
                                <label for="phone">Số điện thoại:</label>
                                <input type="tel" id="phone" name="phone" value="${account.phone}" required>
                            </div>
                            <button type="submit">CẬP NHẬT</button>
                        </form>
                        <div style="margin-top: 30px;" class="foam-actions">
                            <a href="userPage.jsp" class="view-ingredients">< Trở về trang cá nhân</a>
                        </div>    
                    </div>
                </section>
                <jsp:include page="footer.jsp" />
            </c:when>
            <c:otherwise>
                <c:redirect url="MainController?action=homeBook"/>
            </c:otherwise>
        </c:choose>
    </body>
</html>
