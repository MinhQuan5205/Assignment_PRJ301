<%-- 
    Document   : homePage
    Created on : Jul 7, 2025, 9:32:08 PM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home Page - BookLy</title>
        <link href="${pageContext.request.contextPath}/assets/css/home.css" rel="stylesheet">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&family=Nunito:ital,wght@0,200..1000;1,200..1000&family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" integrity="sha512-9usAa10IRO0HhonpyAIVpjrylPvoDwiPUiKdWk5t3PyolY1cOd4DSE0Ga+ri4AuTroPR5aQvXU9xC6qOPnzFeg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <style>
            .heroimagehome {
                background-image: url('assets/img/post-item2.jpg');
                background-size: cover;
                background-position: center;
                background-repeat: no-repeat;
                width: 100%;
                aspect-ratio: 16/7.5;
            }
        </style>
    </head>
    <body>
        <header>
            <div class="container">
                <div class="header-top">
                    <nav class="top-nav">
                        <ul>
                            <li><a href="${pageContext.request.contextPath}/MainController?action=homeBook">Trang chủ</a></li>
                            <li><a href="mailto:info@bookly.com">Liên hệ</a></li>
                        </ul>
                    </nav>
                    <div class="logo">
                        <a href="${pageContext.request.contextPath}/MainController?action=homeBook"><img src="assets/img/main-logo.png" alt="Logo"></a>
                    </div>
                    <nav class="top-nav right-nav">
                        <ul>
                            <li><a href="${pageContext.request.contextPath}/MainController?action=listBook">Sản phẩm</a></li>
                                <c:choose>
                                    <c:when test="${not empty sessionScope.acc}">
                                    <li><a href="cartPage.jsp"><i class="fas fa-shopping-bag"></i></a></li>
                                    <li><a href="userPage.jsp"><i class="far fa-user"></i></a></li>
                                    <li><a href="${pageContext.request.contextPath}/MainController?action=signout">Đăng xuất</a></li>
                                    </c:when>
                                    <c:otherwise>
                                    <li><a href="signin.jsp">Đăng nhập</a></li>
                                    <li><a href="signup.jsp">Đăng ký</a></li>
                                    </c:otherwise>
                                </c:choose>
                        </ul>
                    </nav>
                </div>
            </div>
        </header>

        <section class="heroimagehome hero">
            <div class="container">
                <div class="hero-content">
                    <p>"Bookly – Khơi nguồn tri thức, chạm đến tương lai - Mang trí thức đến cho mọi người."</p>
                    <a href="${pageContext.request.contextPath}/MainController?action=listBook" class="btn btn-primary">Tìm kiếm thêm</a>
                </div>
            </div>
        </section>

        <section class="smooth-foam-section">
            <div class="container">
                <div class="foam-image">
                    <img src="${pageContext.request.contextPath}/assets/img/post-item1.jpg" alt="Home Image">
                </div>
                <div class="foam-content">
                    <h2>BOOKLY</h2>
                    <p>Chúng tôi cung cấp đa dạng các thể loại sách, từ văn học, kỹ năng đến truyện thiếu nhi, giúp bạn nuôi dưỡng tri thức và cảm hứng mỗi ngày. Đồng hành cùng Bookly, bạn không chỉ đọc sách tốt hơn mà còn tạo nên những khoảnh khắc ý nghĩa bên những trang sách tuyệt vời! 📚✨</p>
                    <div class="foam-actions">
                        <a href="${pageContext.request.contextPath}/MainController?action=listBook" class="view-ingredients">Xem sản phẩm ></a>
                    </div>
                </div>
            </div>
        </section>

        <section class="popular-products">
            <div class="container">
                <center><h2>SÁCH MỚI</h2></center>
                <div class="products-slider">
                    <!-- Hiển thị 3 sản phẩm cuối cùng trong danh sách đã có  -->
                    <c:if test="${not empty sessionScope.bookList}">
                        <c:forEach var="b" items="${sessionScope.bookList}" varStatus="loop" begin="${fn:length(sessionScope.bookList) - 3}" end="${fn:length(sessionScope.bookList) - 1}"> 
                            <div class="product-card">
                                <!-- tạo khi nhấn vào hình ảnh của sản phẩm sẽ đưa mình đến trang chi tiết của sản phẩm đó  -->
                                <a style="text-decoration: none;" href="MainController?action=bookDetail&bookID=${b.bookID}" class="recipe-card-link">
                                    <img src="${b.imageURL}" alt="${b.title}">
                                    <h3 style="height: 70px;">${b.title}</h3>
                                    <p class="price">
                                        <strong>
                                            <fmt:formatNumber type="currency" currencySymbol="₫" value="${b.price}" maxFractionDigits="0" />
                                        </strong>
                                    </p>
                                </a>
                            </div>
                        </c:forEach>
                    </c:if>
                </div>
            </div>
        </section>            

        <section class="ice-cream-lovers">
            <div class="container">
                <center><h2>MỘT SỐ ĐÁNH GIÁ TỪ NGƯỜI MUA</h2></center>
                <div class="testimonials-grid">
                    <div class="testimonial-card">
                        <img src="assets/img/insta-item4.jpg" alt="cmt">
                        <p class="quote">Sách đọc khá hay và cho mình nhiều cảm xúc</p>
                        <p class="author">Nguyễn Mạnh Bảo</p>
                        <div class="stars"><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i></div>
                    </div>
                    <div class="testimonial-card">
                        <img src="assets/img/insta-item5.jpg" alt="cmt">
                        <p class="quote">Quyển sách này mình đọc qua thấy khá nhiều chi tiết thú vị!</p>
                        <p class="author">Thúy Hằng</p>
                        <div class="stars"><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i></div>
                    </div>
                    <div class="testimonial-card">
                        <img src="assets/img/insta-item6.jpg" alt="cmt">
                        <p class="quote">Sách này giúp mình có thể tưởng tượng ra nhiều thứ mới mẻ và nhiều chi tiết độc đáo.</p>
                        <p class="author">Phan Thanh</p>
                        <div class="stars"><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i></div>
                    </div>
                </div>
            </div>
        </section>

        <jsp:include page="footer.jsp" />
        <script src="${pageContext.request.contextPath}/assets/js/home.js"></script>
    </body>
</html>
