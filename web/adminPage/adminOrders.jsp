<%-- 
    Document   : adminOrders
    Created on : Jul 20, 2025, 10:31:06 PM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý đơn hàng</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&family=Nunito:ital,wght@0,200..1000;1,200..1000&family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" integrity="sha512-9usAa10IRO0HhonpyAIVpjrylPvoDwiPUiKdWk5t3PyolY1cOd4DSE0Ga+ri4AuTroPR5aQvXU9xC6qOPnzFeg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="../assets/css/home.css">
        <link rel="stylesheet" href="../adminPage/admincss/adminOrders.css">
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

        <div style="margin-top: 30px; padding-bottom: 30px;" class = "container2">
            <h2 class = "order-detail-heading">QUẢN LÝ ĐƠN HÀNG</h2>
            <div class="sort-options" style = "padding-bottom: 20px;">
                <label class = "label"> Sắp xếp theo:</label>
                <a class = "view-ingredients" href="${pageContext.request.contextPath}/MainController?action=adminorders&sort=date_asc">Ngày tăng dần</a> 
                <a class = "view-ingredients" href="${pageContext.request.contextPath}/MainController?action=adminorders&sort=date_desc">Ngày giảm dần</a> 
                <a class = "view-ingredients" href="${pageContext.request.contextPath}/MainController?action=adminorders&sort=total_asc">Tổng tiền tăng dần</a> 
                <a class = "view-ingredients" href="${pageContext.request.contextPath}/MainController?action=adminorders&sort=total_desc">Tổng tiền giảm dần</a>
            </div>
            <c:choose>
                <c:when test="${empty sessionScope.orders}">
                    <p>Không có đơn hàng nào.</p>
                </c:when>
                <c:otherwise>
                    <table class = "cart-table">
                        <thead>
                            <tr>
                                <th>Mã đơn hàng</th>
                                <th>Ngày đặt</th>
                                <th>Tổng tiền</th>
                                <th>Trạng thái</th>
                                <th>Khách hàng</th>
                                <th>Địa chỉ</th>
                                <th>Người nhận</th>
                                <th>Số điện thoại</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${sessionScope.orders}" var="order">
                                <tr>
                                    <td>${order.orderID}</td>
                                    <td>${order.create_at}</td>
                                    <td><fmt:formatNumber value="${order.totalPrice}" type="number" groupingUsed="true"/>đ</td>
                                    <td class="status status-${order.status.toLowerCase()}">${order.status}</td>
                                    <td>${order.userID}</td>
                                    <td>${order.address}</td>
                                    <td>${order.receiver_name}</td>
                                    <td>${order.receiver_phone}</td>

                                    <td>
                                        <a class = "view-ingredients" href="${pageContext.request.contextPath}/MainController?action=orderDetail&orderID=${order.orderID}">Xem chi tiết</a>
                                        <c:if test="${order.status != 'cancelled'}">
                                            <form action="${pageContext.request.contextPath}/MainController?action=updateStatus" method="post" class="update-status-form">
                                                <input type="hidden" name="orderID" value="${order.orderID}">
                                                <select name="status">
                                                    <option value="pending" ${order.status == 'pending' ? 'selected' : ''}>Pending</option>
                                                    <option value="confirmed" ${order.status == 'confirmed' ? 'selected' : ''}>Confirmed</option>
                                                    <option value="shipped" ${order.status == 'shipped' ? 'selected' : ''}>Shipped</option>
                                                    <option value="delivered" ${order.status == 'delivered' ? 'selected' : ''}>Delivered</option>
                                                    <option value="cancelled" ${order.status == 'cancelled' ? 'selected' : ''}>Cancelled</option>
                                                </select>
                                                <button type="submit" class = "btn btn-secondary" style = "font-size: 14px; margin-bottom: 0;">Cập nhật</button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
            <div class="pagination">
                <c:forEach begin="${1}" end="${sessionScope.numpage}" var="i">
                    <a class="${i==page?"active":""}" href="${pageContext.request.contextPath}/MainController?action=adminorders&page=${i}&sort=${sessionScope.sort}">${i}</a>
                </c:forEach>
            </div>        
        </div> 
        <jsp:include page="../footer.jsp" />    
    </body>
</html>
