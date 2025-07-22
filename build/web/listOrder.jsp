<%-- 
    Document   : listOder
    Created on : Jul 17, 2025, 9:26:43 PM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Danh Sách Đơn Hàng</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/listOrder.css"/>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&family=Nunito:ital,wght@0,200..1000;1,200..1000&family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" integrity="sha512-9usAa10IRO0HhonpyAIVpjrylPvoDwiPUiKdWk5t3PyolY1cOd4DSE0Ga+ri4AuTroPR5aQvXU9xC6qOPnzFeg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    </head>
    <body>
        <c:choose>
            <c:when test="${not empty sessionScope.acc}">
                <jsp:include page="userHeader.jsp" />

                <div style="margin-top: 20px;" class="container">
                    <h2 style="margin-bottom: 20px;" class = "order-detail-heading">Danh sách đơn hàng của bạn</h2>
                    <c:choose>
                        <c:when test="${empty orders}">
                            <p>Bạn chưa có đơn hàng nào.</p>
                        </c:when>
                        <c:otherwise>
                            <table class="cart-table">
                                <thead>
                                    <tr>
                                        <th>Ngày đặt</th>
                                        <th>Tổng tiền</th>
                                        <th>Trạng thái</th>
                                        <th>Địa chỉ</th>
                                        <th>Người nhận</th>
                                        <th>Số điện thoại</th>
                                        <th>Xem chi tiết</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${orders}" var="order">
                                        <tr>
                                            <td>${order.create_at}</td>
                                            <td><fmt:formatNumber value="${order.totalPrice}" type="number" groupingUsed="true"/>đ</td>
                                            <td class="status status-${order.status.toLowerCase()}">${order.status}</td>
                                            <td>${order.address}</td>
                                            <td>${order.receiver_name}</td>
                                            <td>${order.receiver_phone}</td>
                                            <td style="text-align: center;">
                                                <a href="${pageContext.request.contextPath}/MainController?action=orderDetail&orderID=${order.orderID}" class = "view-ingredients">Xem</a>
                                            </td>
                                            <td style="text-align: center;">
                                                <c:if test="${order.status == 'pending'}">
                                                    <a href="${pageContext.request.contextPath}/MainController?action=cancelOrder&orderID=${order.orderID}" class="btn btn-secondary" onclick="return confirm('Bạn có chắc chắn muốn hủy đơn hàng này?')">Hủy đơn</a>
                                                </c:if>
                                                <c:if test="${order.status == 'shipped'}">
                                                    <a href="${pageContext.request.contextPath}/MainController?action=confirmReceived&orderID=${order.orderID}" class="confirm-received-button" onclick="return confirm('Bạn xác nhận đã nhận được đơn hàng này?')">Đã nhận</a>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
                <jsp:include page="footer.jsp" />
            </c:when>
            <c:otherwise>
                <c:redirect url="signin.jsp"/>
            </c:otherwise>
        </c:choose>


    </body>
</html>
