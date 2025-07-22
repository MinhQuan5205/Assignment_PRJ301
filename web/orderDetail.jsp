<%-- 
    Document   : orderDetail
    Created on : Jul 18, 2025, 12:45:33 AM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi Tiết Đơn Hàng</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/orderDetail.css">
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

                <div style="margin-top: 20px;" class="container">
                    <h2 class="order-detail-heading">Chi tiết đơn hàng</h2>

                    <div class="order-info">
                        <p><strong>Ngày đặt:</strong> ${order.create_at}</p>
                        <p><strong>Trạng thái:</strong> <span class="status status-${order.status}">${order.status}</span></p>
                        <p><strong>Người nhận:</strong> ${order.receiver_name}</p>
                        <p><strong>Địa chỉ:</strong> ${order.address}</p>
                        <p><strong>Số điện thoại:</strong> ${order.receiver_phone}</p>
                    </div>

                    <h3 style="margin-top: 20px; margin-bottom: 10px;" class = "order-detail-heading">Sản phẩm</h3>
                    <table class="cart-table">
                        <thead>
                            <tr>
                                <th>Sản phẩm</th>
                                <th>Hình ảnh</th>
                                <th>Đơn giá</th>
                                <th>Số lượng</th>
                                <th>Thành tiền</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${orderDetails}" var="detail">
                                <tr>
                                    <td>${detail.book.title}</td>
                                    <td><img style="width: 60px;" src="${detail.book.imageURL}" alt="${detail.book.title}" class="cart-item-image"></td> 
                                    <td><fmt:formatNumber value="${detail.book.getFinalPrice()}" type="number" groupingUsed="true"/>đ</td>
                                    <td>${detail.quantity}</td>
                                    <td><fmt:formatNumber value="${detail.book.getFinalPrice() * detail.quantity}" type="number" groupingUsed="true"/>đ</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                        <tfoot>
                            <tr>
                                <td colspan="4"><strong>Tổng cộng:</strong></td>
                                <td><fmt:formatNumber value="${order.totalPrice}" type="number" groupingUsed="true"/>đ</td>
                            </tr>
                        </tfoot>
                    </table>

                    <c:choose>
                        <c:when test="${sessionScope.acc.role == 0}">
                            <a style="margin: 30px auto;" href="${pageContext.request.contextPath}/MainController?action=adminorders" class="btn btn-secondary">
                                Quay lại danh sách đơn hàng
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a style="margin: 30px auto;" href="${pageContext.request.contextPath}/MainController?action=listOrder" class="btn btn-secondary">
                                Quay lại danh sách đơn hàng
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            <jsp:include page="footer.jsp" />
            </c:when>
            <c:otherwise>
                <c:redirect url="signin.jsp"/>
            </c:otherwise>
        </c:choose>
        <script src="${pageContext.request.contextPath}/assets/js/home.js"></script>
        <c:if test="${not empty sessionScope.alertMessage}">
            <script>
                alert("${sessionScope.alertMessage}");
            </script>
            <c:remove var="alertMessage" scope="session" />
        </c:if>
    </body>
</html>
