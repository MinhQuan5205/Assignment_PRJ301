<%-- 
    Document   : cartPage
    Created on : Jul 17, 2025, 5:18:30 PM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="modelDAO.CartDAO"%>
<%@page import="modelDAO.BookDAO"%>
<%@page import="modelDTO.CartDTO"%>
<%@page import="modelDTO.BookDTO"%>
<%@page import="java.util.List"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>

<%
    String accID = (String) session.getAttribute("accID");
    if (accID == null) {
        response.sendRedirect("signin.jsp"); 
        return; 
    }
    String removeCartIDStr = request.getParameter("cartID"); 
    if (removeCartIDStr != null && !removeCartIDStr.isEmpty()) {
        if (accID != null) {
            try {
                int removeCartID = Integer.parseInt(removeCartIDStr);
                CartDAO cdao = new CartDAO();
                boolean removed = cdao.removeCartItem(removeCartID, accID);
                if (removed) {
                    request.setAttribute("removeMessage", "Sản phẩm đã được xóa khỏi giỏ hàng.");
                } else {
                    request.setAttribute("removeError", "Không thể xóa sản phẩm. Vui lòng thử lại.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("removeError", "Cart ID không hợp lệ.");
            } catch (Exception e) {
                request.setAttribute("removeError", "Lỗi server: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            request.setAttribute("removeError", "Bạn chưa đăng nhập.");
        }
    }
    List<CartDTO> cartItems = null;
    double totalPrice = 0;
    if (accID != null) {
        CartDAO cdao = new CartDAO();
        BookDAO bdao = new BookDAO();
        cartItems = cdao.getCartByAccID(accID);
        if (cartItems != null) {
            for (CartDTO cartItem : cartItems) {
                BookDTO book = bdao.getBookById(cartItem.getBookID());
                if (book != null) {
                    cartItem.setBook(book);
                    totalPrice += book.getFinalPrice() * cartItem.getQuantity();
                }
            }
        }
    }
    request.setAttribute("cartItems", cartItems);
    request.setAttribute("totalPrice", totalPrice);
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
    request.setAttribute("currencyVN", currencyVN);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Giỏ Hàng</title>
        <link rel="stylesheet" href="assets/css/cartPage.css"/>
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
        <h1 style="text-align: center; margin: 0 auto; padding-top: 20px;">Giỏ hàng của bạn</h1>
        <div style="min-height: 80vh; display: flex; flex-direction: column; justify-content: center;">
            <section style="padding: 10px;" class="smooth-foam-section">
                <div class="container">
                    <c:if test="${empty cartItems}">
                        <p>Giỏ hàng của bạn hiện đang trống.</p>
                        <a href="${pageContext.request.contextPath}/MainController?action=listBook" class="view-ingredients">Tiếp tục mua sắm ></a>
                    </c:if>
                    <c:if test="${not empty cartItems}">
                        <form style="width: 80%; margin: 0 auto;" action="checkout.jsp" method="get" id="checkout-form">
                            <table class="cart-table">
                                <thead>
                                    <tr>
                                        <th>Chọn</th>
                                        <th>Sản phẩm</th>
                                        <th>Giá</th>
                                        <th>Số lượng</th>
                                        <th>Tổng cộng</th>
                                        <th>Xóa</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="cartItem" items="${cartItems}">
                                        <tr>
                                            <td style="text-align: center; color: #014576;">
                                                <input style="color: #014576;" type="checkbox" class="cart-checkbox" name="selectedItems" value="${cartItem.cartID}" checked>
                                            </td>
                                            <td>
                                                <div style="display: flex; align-items: center;">
                                                    <img src="${cartItem.book.imageURL}" alt="${cartItem.book.title}" class="cart-item-image">
                                                    <span style="margin-left: 10px;">${cartItem.book.title}</span>
                                                </div>
                                            </td>
                                            <td style="text-align: center;">${currencyVN.format(cartItem.book.getFinalPrice())}</td>
                                            <td style="text-align: center;">
                                                <input type="number" class="quantity-input" value="${cartItem.quantity}" min="1"
                                                       data-cart-id="${cartItem.cartID}" data-product-id="${cartItem.bookID}">
                                            </td>
                                            <td style="text-align: center;" class="item-total">${currencyVN.format(cartItem.book.getFinalPrice()* cartItem.quantity)}</td>
                                            <td style="text-align: center;">
                                                <a href="${pageContext.request.contextPath}/MainController?action=removeCartItem&cartID=${cartItem.cartID}" class="remove-item-btn" onclick="return confirmRemove();">
                                                    <i style="color: #014576;" class="fas fa-trash-alt"></i>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>

                            <div style="padding-right: 60px;" class="cart-total">
                                Tổng cộng: <strong> ${currencyVN.format(totalPrice)}</strong>
                            </div>
                            <div class="cart-actions">
                                <button type="button" class="btn btn-secondary"><a style="text-decoration: none; color: #014576;" href="${pageContext.request.contextPath}/MainController?action=listBook">Tiếp tục mua sắm</a></button>
                                <button type="submit" class="btn btn-secondary" id="checkout-button">Tiếp tục thanh toán</button>
                            </div>
                        </form>    
                    </c:if>
                </div>
            </section>
        </div>
        <jsp:include page="footer.jsp" />
        <script src="${pageContext.request.contextPath}/assets/js/home.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/cartWithDelete.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/cartChange.js"></script>
    </body>
</html>
