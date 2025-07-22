<%-- 
    Document   : shopBook
    Created on : Jul 14, 2025, 5:24:04 PM
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
        <title>SÁCH</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/shopBook.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/book.css">
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
        <section class="recipes-section">
            <div class="container">
                <form action="shopBook.jsp" method="GET">
                    <div id="search-box" class="search-bar">
                        <input type="text" name="searchName" value="${param.searchName}" placeholder="Nhập tên sách mà bạn muốn tìm kiếm...">
                        <button type="submit"><i class="fas fa-search"></i></button>
                    </div>
                </form>

                <div class="recipes-grid-container">
                    <aside class="recipes-sidebar">
                        <form action="MainController" method="GET"> <%--Bộ Lọc sản phẩm --%>
                            <input type="hidden" name="action" value="listBook"/>
                            <div class="filter-group">
                                <div class="filter-header">Sắp xếp theo <i class="fas fa-chevron-down"></i></div>
                                <div class="filter-options">
                                    <select name="sortBy" > 
                                        <option value="">-- Chọn tiêu chí --</option>
                                        <option value="priceAsc" ${param.sortBy == 'priceAsc' ? 'selected' : ''}>Giá tăng dần</option>
                                        <option value="priceDesc" ${param.sortBy == 'priceDesc' ? 'selected' : ''}>Giá giảm dần</option>
                                        <option value="stockAsc" ${param.sortBy == 'stockAsc' ? 'selected' : ''}>Số lượng bán ra nhiều</option>
                                        <option value="ratingDesc" ${param.sortBy == 'ratingDesc' ? 'selected' : ''}>Đánh giá cao nhất</option>
                                    </select>
                                </div>
                            </div>

                            <div class="filter-group">
                                <div class="filter-header">Danh mục <i class="fas fa-chevron-down"></i></div>
                                <div class="filter-options">
                                    <ul>
                                        <li><label><input type="checkbox" name="category" value="1" ${fn:contains(paramValues.categoryParams, '1') ? 'checked' : ''}> Truyện Tranh</label></li>
                                        <li><label><input type="checkbox" name="category" value="2" ${fn:contains(paramValues.categoryParams, '2') ? 'checked' : ''}> Tiểu Thuyết</label></li>
                                        <li><label><input type="checkbox" name="category" value="3" ${fn:contains(paramValues.categoryParams, '3') ? 'checked' : ''}> Kinh Dị</label></li>
                                        <li><label><input type="checkbox" name="category" value="4" ${fn:contains(paramValues.categoryParams, '4') ? 'checked' : ''}> Khoa Học</label></li>
                                        <li><label><input type="checkbox" name="category" value="5" ${fn:contains(paramValues.categoryParams, '5') ? 'checked' : ''}> Thiếu Nhi</label></li>
                                    </ul>
                                </div>
                            </div>

                            <div class="filter-group">
                                <div class="filter-header">Tác Giả <i class="fas fa-chevron-down"></i></div>
                                <div class="filter-options">
                                    <ul>
                                        <li><label><input type="checkbox" name="author" value="1" ${fn:contains(paramValues.authorParams, '1') ? 'checked' : ''}> Nguyễn Nhật Ánh</label></li>
                                        <li><label><input type="checkbox" name="author" value="2" ${fn:contains(paramValues.authorParams, '2') ? 'checked' : ''}> J.K. Rowling</label></li>
                                        <li><label><input type="checkbox" name="author" value="3" ${fn:contains(paramValues.authorParams, '3') ? 'checked' : ''}> Stephen King</label></li>
                                        <li><label><input type="checkbox" name="author" value="4" ${fn:contains(paramValues.authorParams, '4') ? 'checked' : ''}> Isaac Asimov</label></li>
                                        <li><label><input type="checkbox" name="author" value="5" ${fn:contains(paramValues.authorParams, '5') ? 'checked' : ''}> Doraemon Team</label></li>
                                    </ul>
                                </div>
                            </div>

                            <div class="filter-group">
                                <div class="filter-header">Giá tiền <i class="fas fa-chevron-down"></i></div>
                                <div class="filter-options">
                                    <ul>
                                        <li><label><input type="checkbox" name="price" value="0-100000" ${fn:contains(paramValues.priceParams, '0-100000') ? 'checked' : ''}> Dưới 100.000</label></li>
                                        <li><label><input type="checkbox" name="price" value="100000-200000" ${fn:contains(paramValues.priceParams, '100000-200000') ? 'checked' : ''}> 100.000 - 200.000</label></li>
                                        <li><label><input type="checkbox" name="price" value="200000-500000" ${fn:contains(paramValues.priceParams, '200000-500000') ? 'checked' : ''}> 200.000 - 500.000</label></li>
                                        <li><label><input type="checkbox" name="price" value="500000+" ${fn:contains(paramValues.priceParams, '500000+') ? 'checked' : ''}> Trên 500.000</label></li>
                                    </ul>
                                </div>
                            </div>

                            <button class="filter-submit" type="submit">ÁP DỤNG</button>
                            <button class="filter-submit"  type="button" onclick="resetFilter()">ĐẶT LẠI</button>        
                        </form>
                    </aside>
                    <main id="sanpham" class="recipes-grid">
                        <c:forEach var="b" items="${books}" varStatus="loop"> 
                            <c:if test="${(empty param.searchName or fn:containsIgnoreCase(b.title, param.searchName))}">
                                <c:if test="${b.stock > 0}">
                                    <div style="max-height: 350px;" class="recipe-card">
                                        <a style="text-decoration: none;" href="MainController?action=bookDetail&bookID=${b.bookID}" class="recipe-card-link">
                                            <img src="${b.imageURL}" alt="${b.title}">
                                            <h3>${b.title}</h3>

                                            <c:if test="${b.discount > 0}">
                                                <p class="pprice">
                                                    <del style="color: red;font-size: 0.8rem;">
                                                        <fmt:formatNumber type="currency" currencySymbol="₫" value="${b.price}" maxFractionDigits="0"/>
                                                    </del>
                                                    <strong style="padding-left: 5px;">
                                                        <fmt:formatNumber type="currency" currencySymbol="₫" value="${b.getFinalPrice()}" maxFractionDigits="0"/>
                                                    </strong>
                                                </p>
                                            </c:if>
                                            <c:if test="${b.discount == 0}">
                                                <p  class="pprice">
                                                    <strong>
                                                        <fmt:formatNumber type="currency" currencySymbol="₫" value="${b.price}" maxFractionDigits="0"/>
                                                    </strong>
                                                </p>
                                            </c:if>

                                            <button class="add-to-cart-btn">Thêm vào giỏ hàng</button>
                                        </a>
                                    </div>
                                </c:if>
                            </c:if>
                        </c:forEach>

                        <c:if test="${empty books}">
                            <p>Không có sản phẩm nào.</p>
                        </c:if>
                    </main>                
                </div>

                <%--giúp tạo đường dẫn với những giá trị đã lựa chọn--%>
                <div class="pagination">
                    <c:forEach begin="1" end="${sessionScope.numpage}" var="i">
                        <!-- Bắt đầu với base URL -->
                        <c:set var="url" value="${pageContext.request.contextPath}/MainController?action=listBook&page=${i}" />

                        <!-- Thêm searchName nếu có -->
                        <c:if test="${not empty sessionScope.searchName}">
                            <c:set var="url" value="${url}&searchName=${sessionScope.searchName}" />
                        </c:if>

                        <!-- Thêm sortBy nếu có -->
                        <c:if test="${not empty sessionScope.sortBy}">
                            <c:set var="url" value="${url}&sortBy=${sessionScope.sortBy}" />
                        </c:if>

                        <!-- Thêm categoryParams nếu có -->
                        <c:if test="${not empty sessionScope.categoryParams}">
                            <c:forEach var="catParam" items="${sessionScope.categoryParams}">
                                <c:set var="url" value="${url}&category=${catParam}" />
                            </c:forEach>
                        </c:if>

                        <!-- Thêm authorParams nếu có -->
                        <c:if test="${not empty sessionScope.authorParams}">
                            <c:forEach var="authorParam" items="${sessionScope.authorParams}">
                                <c:set var="url" value="${url}&author=${authorParam}" />
                            </c:forEach>
                        </c:if>

                        <!-- Thêm priceParams nếu có -->
                        <c:if test="${not empty sessionScope.priceParams}">
                            <c:forEach var="priceParam" items="${sessionScope.priceParams}">
                                <c:set var="url" value="${url}&price=${priceParam}" />
                            </c:forEach>
                        </c:if>

                        <!-- Hiển thị nút trang -->
                        <a class="${i == sessionScope.page ? 'active' : ''}" href="${url}">${i}</a>
                    </c:forEach>
                </div>                    
            </div>
        </section>

        <section class="recipe-request-section">
            <div class="container">
                <div class="request-content">
                    <h2>Không tìm thấy sản phẩm bạn muốn?</h2>
                    <p>Rất tiếc, chúng tôi chưa có sản phẩm mà bạn đang tìm kiếm. Tuy nhiên, bạn có thể thử tìm kiếm với từ khóa khác hoặc liên hệ với chúng tôi để được tư vấn. Chúng tôi luôn sẵn sàng giúp bạn tìm ra sản phẩm phù hợp nhất!</p>
                    <a href="mailto:info@bookly.com" class="btn btn-primary">Mail to us</a>
                </div>
            </div>
        </section>

        <h2 style="margin: 0 auto 30px; text-align: center">SÁCH HẾT HÀNG</h2>        
        <div style="width: 80%; margin: 0 auto; margin-bottom: 30px;" class="product-container recipes-grid out-of-stock">
            <c:forEach var="b" items="${sessionScope.outOfStockBooks}">
                <div class="recipe-card out-of-stock-card">
                    <a style="text-decoration: none;" href="MainController?action=bookDetail&bookID=${b.bookID}" class="recipe-card-link">
                        <img src="${b.imageURL}" alt="${b.title}">
                        <h3>${b.title}</h3>
                        <p class="pprice">
                            <strong>
                                <fmt:formatNumber type="currency" currencySymbol="₫" value="${b.price}" maxFractionDigits="0"/>
                            </strong>
                        </p>
                        <p class="add-to-cart-btn out-of-stock-label">Tạm hết hàng</p>
                    </a>
                </div>
            </c:forEach>

            <c:if test="${empty sessionScope.outOfStockBooks}">
                <p>Không có sách nào tạm hết hàng.</p>
            </c:if>
        </div>

        <jsp:include page="footer.jsp" />
        <script src="${pageContext.request.contextPath}/assets/js/shopBook.js"></script>
    </body>
</html>
