<%-- 
    Document   : checkout
    Created on : Jul 17, 2025, 5:50:12 PM
    Author     : ACER
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<%@page import="modelDAO.CartDAO"%>
<%@page import="modelDTO.CartDTO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelDTO.BookDTO"%>
<%@page import="modelDAO.BookDAO"%>


<%
    String accID = (String) session.getAttribute("accID");
    if (accID == null) {
        response.sendRedirect("signin.jsp");
        return;
    }

    CartDAO cdao = new CartDAO();
    BookDAO bdao = new BookDAO(); 
    List<CartDTO> selectedCartItems = new ArrayList<>();
    double totalPrice = 0;

    String[] selectedItems = request.getParameterValues("selectedItems");

    if (selectedItems != null && selectedItems.length > 0) {
        for (String cartIDStr : selectedItems) {
            try {
                int cartID = Integer.parseInt(cartIDStr);
                CartDTO cartItem = cdao.getCartByID(cartID);

                if (cartItem != null && cartItem.getAccID().equals(accID)) { 

                   BookDTO book = bdao.getBookById(cartItem.getBookID());
                   if(book != null){
                        cartItem.setBook(book); 
                        selectedCartItems.add(cartItem);
                        totalPrice += cartItem.getTotalPrice(); 
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid cartID: " + cartIDStr); 
            }
        }
    }

    request.setAttribute("cartItems", selectedCartItems); 
    request.setAttribute("totalPrice", totalPrice);
%>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thanh Toán</title>
        <link rel="stylesheet" href="assets/css/checkout.css"/>
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
            </c:when>
            <c:otherwise>
                <jsp:include page="guestHeader.jsp" />
            </c:otherwise>
        </c:choose>

        <h2 style="margin: 25px auto 0;">XÁC NHẬN ĐƠN HÀNG</h2>

        <div style="text-align: center; margin-top: 20px;">
            <c:if test="${empty cartItems and empty errorMessage}">
                <p style="color: black;">
                    Bạn chưa chọn sản phẩm nào để thanh toán. <a href="cartPage.jsp">Quay lại giỏ hàng</a>
                </p>
            </c:if>
            <c:if test="${not empty errorMessage}">
                <p style="color: red;">${errorMessage} <a href="cartPage.jsp">Quay lại giỏ hàng</a></p>
            </c:if>
        </div>
        <c:if test="${not empty cartItems}">
            <form action="MainController" method="post">
                <input type="hidden" name="action" value="processOrder"/>
                <label for="name">Tên người nhận:</label>
                <input type="text" id="name" name="name" value="<c:out value='${param.name}' />" required><br>

                <label for="phone">Số điện thoại:</label>
                <input type="text" id="phone" name="phone" pattern="\d{10,11}" title="Số điện thoại hợp lệ (10-11 số)" required><br>

                <label for="address">Địa chỉ giao hàng:</label>
                <textarea id="address" name="address" required><c:out value='${param.address}' /></textarea><br>

                <h3>Sản phẩm trong đơn hàng</h3>
                <table border="1">
                    <tr>
                        <th>Sản phẩm</th>
                        <th>Giá</th>
                        <th>Số lượng</th>
                        <th>Thành tiền</th>
                    </tr>
                    <c:forEach var="cartItem" items="${cartItems}">
                        <tr>
                            <td>${cartItem.book.title}</td>
                            <td><fmt:formatNumber value="${cartItem.book.getFinalPrice()}" type="number" groupingUsed="true"/>đ</td>
                            <td>${cartItem.quantity}</td>
                            <td><fmt:formatNumber value="${cartItem.getTotalPrice()}" type="number" groupingUsed="true"/>đ</td> 

                        </tr>
                    </c:forEach>
                </table>
                <p style="text-align: center;"><strong>TỔNG CỘNG: <fmt:formatNumber value="${totalPrice}" type="number" groupingUsed="true"/>đ</strong></p>

                <p style="width: 100%; text-align: center;"><button type="submit">XÁC NHẬN</button></p>
            </form>
        </c:if>    
        <jsp:include page="footer.jsp" />
    </body>
</html>
