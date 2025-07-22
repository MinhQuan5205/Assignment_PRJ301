<%-- 
    Document   : listUser
    Created on : Jul 18, 2025, 7:04:56 PM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Danh Sách Người Dùng(User)</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@100..900&family=Nunito:wght@200..1000&family=Roboto:wght@100;300;400;500;700;900&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="../adminPage/admincss/listUser.css">
    </head>
    <body>
        <header style="margin: 0;">
            <div class="container">
                <div class="header-top">
                    <div class="logo">
                        <a href="${pageContext.request.contextPath}/adminPage/adminHomePage.jsp"><img src="../assets/img/main-logo.png" alt="Logo Website"></a>
                    </div>
                    <nav style="margin: 0;" class="top-nav right-nav">
                        <ul style="margin: 0">
                            <li><a href="${pageContext.request.contextPath}/MainController?action=loadDataBook">Sản Phẩm</a></li>
                            <li><a href="${pageContext.request.contextPath}/MainController?action=loadDataUser">Người Dùng</a></li>
                            <li><a href="${pageContext.request.contextPath}/MainController?action=adminorders">Đơn Hàng</a></li>
                            <li><a href="${pageContext.request.contextPath}/MainController?action=signout">Đăng xuất</a></li>
                        </ul>
                    </nav>
                </div>
            </div>
        </header>
            <div class="container2">
                <h1>DANH SÁCH NGƯỜI DÙNG</h1>
                <form action="listUser.jsp" method="GET">
                    <label class="label" for="searchName">Tìm kiếm:</label>
                    <input class="searchname" type="text" id="searchName" name="searchName" value="${param.searchName}" placeholder="Nhập tên hoặc ID người dùng...">
                    <%--Param.searchName giúp mình giữ lại được thông tin tìm kiếm dù sau khi submit form--%>
                    <label class="label" for="role">Vai trò:</label>
                    <select name="role" id="role">
                        <option value="">Tất cả</option>
                        <option value="0" ${param.role == '0' ? 'selected' : ''}>Admin</option>
                        <option value="1" ${param.role == '1' ? 'selected' : ''}>User</option>
                    </select>
                    <button type="submit">Lọc</button>
                </form>
                <table>
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th>ID</th>
                            <th>Email</th>
                            <th>Tên</th>
                            <th>Số điện thoại</th>
                            <th>Vai trò</th>
                            <th>Chỉnh sửa</th> 
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="user" items="${sessionScope.users}" varStatus="loop">
                            <c:if test="${(empty param.role or user.role == param.role)
                                          and (empty param.searchName or fn:containsIgnoreCase(user.accID, param.searchName) or fn:containsIgnoreCase(user.name, param.searchName))
                                          and (user.isActive)
                            }"><%--tránh các trường hợp không chọn role,lọc đúng role, không tìm kiếm thì không lọc đồng thời kiểm tra chuỗi có chứa không --%>
                                  <tr>
                                      <td>${loop.index + 1}</td>
                                      <td>${user.accID}</td>
                                      <td>${user.email}</td>
                                      <td>${user.name}</td>
                                      <td>${user.phone}</td>
                                      <td>
                                          <c:choose>
                                              <c:when test="${user.role == 0}">Admin</c:when>
                                              <c:when test="${user.role == 1}">User</c:when>
                                              <c:otherwise>Không xác định</c:otherwise>
                                          </c:choose>
                                      </td>
                                      <td>
                                          <a href="${pageContext.request.contextPath}/MainController?action=deleteUser&accIDdel=${user.accID}" class="delete-btn" onclick="return confirm('Bạn có chắc chắn muốn xóa người dùng này không?');">Xóa</a>
                                      </td>
                                  </tr>
                            </c:if>
                        </c:forEach>
                    </tbody>
                </table>    
            </div>
        <jsp:include page="../footer.jsp" />
    </body>
</html>
