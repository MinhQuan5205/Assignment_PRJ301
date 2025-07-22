<%-- 
    Document   : bookDetail
    Created on : Jul 17, 2025, 2:22:52 PM
    Author     : ACER
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="modelDTO.BookDTO" %>
<%@ page import="modelDAO.ReviewDAO" %>
<%@ page import="modelDAO.GenreDAO" %>
<%@ page import="modelDTO.ReviewDTO" %>
<%@ page import="modelDTO.GenreDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="modelDAO.BookDAO" %> 
<%
    BookDTO book = (BookDTO) session.getAttribute("book");
    if (book == null) {
        response.getWriter().println("Không tìm thấy sản phẩm!");
        return;
    }
    int genreID = book.getGenreID();
    ReviewDAO rdao = new ReviewDAO();
    List<ReviewDTO> reviews = rdao.getReviewsByBookID(book.getBookID());
    request.setAttribute("comments", reviews);

    GenreDAO gdao = new GenreDAO();
    GenreDTO genre = gdao.getGenreById(genreID);

    BookDAO bdao = new BookDAO();
    List<BookDTO> similarProducts = bdao.getSimilarBooks(book.getGenreID(), book.getAuthorID(), book.getBookID(), 4); 
// Thêm bookID để loại trừ sách hiện tại và lấy giới hạn 4 sách ra 
    request.setAttribute("similarProducts", similarProducts);
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><c:out value="${book.getTitle()}"/></title>
        <link rel="stylesheet" href="assets/css/home.css">
        <link rel="stylesheet" href="assets/css/book.css">
        <link rel="stylesheet" href="assets/css/bookDetail.css">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&family=Nunito:ital,wght@0,200..1000;1,200..1000&family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
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
        <div class="breadcrumb">
            <a href="${pageContext.request.contextPath}/MainController?action=homeBook">Trang chủ</a>
            <span>/</span>
            <a href="${pageContext.request.contextPath}/MainController?action=listBook">Sản phẩm</a>
            <span>/</span>
            <span class="pr-name">${book.getTitle()}</span>
        </div>

        <div class="product-page2">
            <div class="product-page">
                <div class="product-card" data-base-price="<c:out value="${book.getPrice()}"/>" data-discount="<c:out value="${book.getDiscount()}"/>">
                    <div class="product-image">
                        <img src="<c:out value="${book.getImageURL()}"/>" alt="<c:out value="${book.getTitle()}"/>">
                    </div>
                    <div class="product-info">
                        <%-- Tên sản phẩm --%>
                        <div class="product-header">
                            <h1 class="product-name"><c:out value="${book.getTitle()}"/></h1>
                        </div>
                        <%-- giá tiền kèm theo đánh giá --%>
                        <div class="price-rating">
                            <%-- giá tiền --%>
                            <p class="ppprice">
                                <c:if test="${book.getDiscount() > 0}">
                                    <del style="color: red; font-size: 0.8em; margin-right: 5px;"><c:out value="${book.getPrice()}đ"/></del>
                                </c:if>
                                <span class="discounted-price"></span>
                            </p>

                            <%-- hiển thị đánh giá bằng sao --%>
                            <div class="rating">
                                <c:set var="rating" value="${book.getRating()}" />
                                <c:set var="fullStars" value="${rating - (rating % 1)}" />  <!-- Tương đương Math.floor() -->
                                <c:set var="hasHalfStar" value="${(rating % 1) >= 0.5}" />  <!-- Kiểm tra có nửa sao không -->
                                <c:set var="emptyStars" value="${5 - fullStars - (hasHalfStar ? 1 : 0)}" />

                                <!-- Hiển thị sao đầy -->
                                <c:forEach var="i" begin="1" end="${fullStars}">
                                    <i class="fas fa-star"></i> <!-- Ngôi sao đầy -->
                                </c:forEach>

                                <!-- Hiển thị nửa sao nếu có -->
                                <c:if test="${hasHalfStar}">
                                    <i class="fas fa-star-half-alt"></i> <!-- Ngôi sao nửa -->
                                </c:if>

                                <!-- Hiển thị sao rỗng -->
                                <c:forEach var="i" begin="1" end="${emptyStars}">
                                    <i class="far fa-star"></i> <!-- Ngôi sao rỗng -->
                                </c:forEach>
                            </div>
                        </div>
                        <div class="order-section">
                            <div class="color-quantity">
                                <div class="total-price">
                                    <p class="section-title">TỔNG GIÁ TIỀN</p>
                                    <p class="total-amount">0.00đ</p>
                                </div>
                            </div>
                            <p id="stock-error" style="color: red; display: none;">
                                Số lượng hàng chỉ còn <span id="stock-amount">${book.getStock()}</span> sản phẩm!
                            </p>
                            <div class="order-actions">
                                <div class="order-actions">
                                    <form class="order-actions" action="MainController" method="get" onsubmit="return validateStock(event)">
                                        <input type="hidden" name="action" value="addToCart">
                                        <input type="hidden" name="bookID" value="${book.getBookID()}">
                                        <div class="quantity-control">
                                            <button type="button" class="qty-btn minus">-</button>
                                            <input type="number" name="quantity" id="quantity" class="qty-input" value="1" min="1">
                                            <button type="button" class="qty-btn plus">+</button>
                                        </div>
                                        <div class="add-to-cart">
                                            <button type="submit" class="btn btn-primary">THÊM VÀO GIỎ HÀNG</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>        
                    </div>        
                </div>
            </div>
            <div style="margin-top: 30px;" class="destitle"><h3>MÔ TẢ SÁCH</h3></div>
            <div class="tab-content active" id="description">
                <p><c:out value="${book.getDescription()}"/><a href="#"></a></p>
            </div> 
            <div class="product-comments">
                <div class="destitle"><h3>BÌNH LUẬN SẢN PHẨM</h3></div>
                <ul class="comment-list">
                    <c:forEach var="comment" items="${comments}">
                        <li class="comment-item">
                            <div class="comment-header">
                                <span class="comment-author"><c:out value="${comment.getAccID()}"/></span>

                                <div class="comment-rating">
                                    <c:forEach begin="1" end="${comment.getRating()}">
                                        <i class="fas fa-star"></i>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="comment-header">
                                <span class="comment-date"><c:out value="${comment.getDate()}"/></span>
                            </div>
                            <div class="comment-body">
                                <p><c:out value="${comment.getComment()}"/></p>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
                <div class="comment-form">
                    <div class="destitle"><h3>THÊM BÌNH LUẬN</h3></div>
                    <form action="MainController?action=addComment" method="post"> 
                        <input type="hidden" name="bookID" value="${book.getBookID()}"> 
                        <div class="form-group">
                            <label for="commentText">Bình luận của bạn:</label>
                            <textarea id="commentText" name="commentText" rows="4" required></textarea>
                        </div>
                        <div class="form-group">
                            <label for="rating">Đánh giá:</label>
                            <select id="rating" name="rating">
                                <option value="5">5 Sao</option>
                                <option value="4">4 Sao</option>
                                <option value="3">3 Sao</option>
                                <option value="2">2 Sao</option>
                                <option value="1">1 Sao</option>
                            </select>
                        </div>
                        <button style="margin: 15px auto; width: 200px;" type="submit" class="btn btn-primary">GỬI BÌNH LUẬN</button>
                    </form>
                </div>
            </div>
        </div>
        <div style="width:100%;" class="similar-products">
            <div class="destitle"><h3>SÁCH TƯƠNG TỰ</h3></div>
            <div class="product-list"> 
                <c:forEach var="similarProduct" items="${similarProducts}">
                    <div class="product-item">
                        <a style="text-decoration: none;" href="${pageContext.request.contextPath}/MainController?action=bookDetail&bookID=${similarProduct.getBookID()}">
                            <div class="smproduct-image">
                                <img src="${similarProduct.getImageURL()}" alt="${similarProduct.getTitle()}">
                            </div>
                            <div class="product-info">
                                <h4 class="product-name">${similarProduct.getTitle()}</h4>
                                <p class="price">
                                    <c:if test="${similarProduct.getDiscount() > 0}">
                                        <del style="color: red; font-size: 0.8em; margin-right: 5px;">
                                            <fmt:formatNumber value="${similarProduct.price - (similarProduct.price % 1)}" pattern="#,##0"/>₫
                                        </del>
                                    </c:if>
                                    <fmt:formatNumber  value="${similarProduct.getPrice() * (1 - similarProduct.getDiscount()/100)}" pattern="#,##0"/>đ
                                </p>
                            </div>
                        </a>
                    </div>
                </c:forEach>
            </div>                
        </div>
        <jsp:include page="footer.jsp" />
        
        <script src="assets/js/detail.js"></script>
        <script src="assets/js/formhidden.js"></script>
        <script>
            function validateStock(event) {
                var stock = ${book.getStock()};
                var quantity = document.getElementById("quantity").value;
                var stockError = document.getElementById("stock-error");

                if (quantity > stock) {
                    stockError.style.display = "block";
                    stockError.innerHTML = "Số lượng hàng chỉ còn " + stock + " sản phẩm!";
                    event.preventDefault();
                    return false;
                } else {
                    stockError.style.display = "none";
                    return true;
                }
            }
        </script>
</body>
</html>
