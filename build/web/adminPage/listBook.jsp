<%-- 
    Document   : listBook
    Created on : Jul 19, 2025, 12:11:28 PM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Danh Sách Sản Phẩm</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@100..900&family=Nunito:wght@200..1000&family=Roboto:wght@100;300;400;500;700;900&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="../adminPage/admincss/listBook.css">
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

        <div style="margin-top: 30px; background-color: white; padding: 20px;" class="container">
            <h1>DANH SÁCH SẢN PHẨM</h1>
            <div class="button-container" style="text-align: right; margin-bottom: 20px;">
                <a style="text-decoration: none;" href="${pageContext.request.contextPath}/MainController?action=addBook" class="add-product-btn">Thêm Sách</a>
            </div>
            <form action="${pageContext.request.contextPath}/MainController" method="GET">
                <input type="hidden" name="action" value="loadDataBook"/>
                <label class="label" for="searchName">Tìm kiếm:</label>
                <input class="searchname" type="text" id="searchName" name="searchName" value="${sessionScope.searchName}" placeholder="Nhập Tên Sách...">
                <label class="label" for="category">Danh mục:</label>
                <select name="category" id="category">
                    <option value="">Tất cả</option>
                    <option value="1" ${sessionScope.category == '1' ? 'selected' : ''}>Truyện Tranh</option>
                    <option value="2" ${sessionScope.category == '2' ? 'selected' : ''}>Tiểu Thuyết</option>
                    <option value="3" ${sessionScope.category == '3' ? 'selected' : ''}>Kinh Dị</option>
                    <option value="4" ${sessionScope.category == '4' ? 'selected' : ''}>Khoa Học</option>
                    <option value="5" ${sessionScope.category == '5' ? 'selected' : ''}>Thiếu Nhi</option>
                </select>

                <label class="label" for="author">Tác Giả:</label>
                <select name="author" id="pet">
                    <option value="">Tất cả</option>
                    <option value="1" ${sessionScope.author == '1' ? 'selected' : ''}> Nguyễn Nhật Ánh</option>
                    <option value="2" ${sessionScope.author == '2' ? 'selected' : ''}> J.K. Rowling</option>
                    <option value="3" ${sessionScope.author == '3' ? 'selected' : ''}> Stephen King</option>
                    <option value="4" ${sessionScope.author == '4' ? 'selected' : ''}> Isaac Asimovc</option>
                    <option value="5" ${sessionScope.author == '5' ? 'selected' : ''}> Doraemon Team</option>
                </select>

                <label class="label" for="stock">Số lượng tồn kho:</label>
                <select name="stock" id="stock">
                    <option value="">Tất cả</option>
                    <option value="0-10" ${sessionScope.stock == '0-10' ? 'selected' : ''}>0 - 10</option>
                    <option value="10-50" ${sessionScope.stock == '10-50' ? 'selected' : ''}>10 - 50</option>
                    <option value="50-100" ${sessionScope.stock == '50-100' ? 'selected' : ''}>50 - 100</option>
                    <option value="100+" ${sessionScope.stock == '100+' ? 'selected' : ''}>>= 100</option>
                </select>
                <button type="submit">Lọc</button>
            </form>
            <table>
                <thead>
                    <tr>
                        <th>STT</th>
                        <th>Tên sản phẩm</th>
                        <th>Giá tiền</th>
                        <th>Hình ảnh</th>
                        <th>Mô tả</th>
                        <th>Danh mục</th>
                        <th>Tác Giả</th>
                        <th>Giảm giá (%)</th>
                        <th>Số lượng</th>
                        <th>Chỉnh sửa</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="book" items="${sessionScope.books}" varStatus="loop">
                        <tr>
                            <td>${loop.index + 1}</td>
                            <td>${book.title}</td>
                            <td>${book.price}</td>
                            <td><img src="${book.imageURL}" alt="Hình ảnh" class="product-img"></td>
                            <td class="description-cell">${book.description}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${book.genreID == 1}">Truyện Tranh</c:when>
                                    <c:when test="${book.genreID == 2}">Tiểu Thuyết</c:when>
                                    <c:when test="${book.genreID == 3}">Kinh Dị</c:when>
                                    <c:when test="${book.genreID == 4}">Khoa Học</c:when>
                                    <c:when test="${book.genreID == 5}">Thiếu Nhi</c:when>
                                    <c:otherwise>Không xác định</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${book.authorID == 1}">Nguyễn Nhật Ánh</c:when>
                                    <c:when test="${book.authorID == 2}">J.K. Rowling</c:when>
                                    <c:when test="${book.authorID == 3}">Stephen King</c:when>
                                    <c:when test="${book.authorID == 4}">Isaac Asimovc</c:when>
                                    <c:when test="${book.authorID == 5}">Doraemon Team</c:when>
                                    <c:otherwise>Không xác định</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${book.discount}%</td>
                            <td>${book.stock}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/MainController?action=editBook&bookID=${book.bookID}" class="edit-btn">Sửa</a>
                                <div class="distance"></div>
                                <a href="${pageContext.request.contextPath}/MainController?action=deleteBook&bookID=${book.bookID}" class="delete-btn" onclick="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này không?');">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <div class="pagination">
                <c:forEach begin="${1}" end="${sessionScope.numpage}" var="i">
                    <a class="${i==sessionScope.page?"active":""}" href="${pageContext.request.contextPath}/MainController?action=loadDataBook&page=${i}
                       <c:if test="${not empty sessionScope.searchName}">&searchName=${sessionScope.searchName}</c:if>
                       <c:if test="${not empty sessionScope.category}">&category=${sessionScope.category}</c:if>
                       <c:if test="${not empty sessionScope.author}">&author=${sessionScope.author}</c:if>
                       <c:if test="${not empty sessionScope.stock}">&stock=${sessionScope.stock}</c:if>
                       ">${i}</a>
                </c:forEach>
            </div>    
        </div>
        <jsp:include page="../footer.jsp" />                
    </body>
</html>
