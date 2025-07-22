<%-- 
    Document   : editBook
    Created on : Jul 21, 2025, 1:05:36 PM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chỉnh Sửa Sản Phẩm</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@100..900&family=Nunito:wght@200..1000&family=Roboto:wght@100;300;400;500;700;900&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="../assets/css/home.css">
        <link rel="stylesheet" href="../adminPage/admincss/add.css">
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

        <div style="margin-top: 30px;" class="login-container container">
            <div style="width: 100%;" class="form-section">
                <h1>CHỈNH SỬA SẢN PHẨM</h1>
                <form action="${pageContext.request.contextPath}/MainController?action=editBook" method="post" class="form-part">
                    <input type="hidden" name="bookID" value="${book.bookID}" readonly>
                    <input type="text" name="bookTitle" value="${book.title}" placeholder="Tên Sách" required>
                    <input type="text" name="price" value="${book.price}" placeholder="Giá tiền" required>
                    <textarea name="description" placeholder="Mô tả sản phẩm" value="${book.description}" required>${book.description}</textarea>
                    <select id="category" name="category" required>
                        <option value="" disabled hidden>Chọn danh mục</option>
                        <c:forEach var="c" items="${sessionScope.categories}">
                            <option value="${c.genreID}" ${book.genreID == c.genreID ? 'selected' : ''}>${c.genreName}</option>
                        </c:forEach>
                    </select>

                    <select id="author" name="author" required>
                        <option value="" disabled selected hidden>Chọn tác giả</option>
                        <c:forEach var="a" items="${sessionScope.authors}">
                            <option value="${a.authorID}" ${book.authorID == a.authorID ? 'selected' : ''}>${a.authorName}</option>
                        </c:forEach>
                    </select>
                    <input type="text" name="discount" value="${book.discount}" placeholder="Chọn % giảm giá" required>
                    <input type="text" name="stock" value="${book.stock}" placeholder="Số lượng tồn kho" required>
                    <label class="file-label">Chèn url hình ảnh mới (nếu có)</label>
                    <input type="text" name="imageurl" value="${book.imageURL}" placeholder="Nhập URL hình ảnh">
                    <p>Hình ảnh hiện tại: <img src="${book.imageURL}" alt="Hình ảnh sản phẩm" width="100"></p>
                    <button type="submit" class="login-btnn">CẬP NHẬT SẢN PHẨM</button>
                </form>
                <c:if test="${sessionScope.errorep != null}">
                    <p style="color: red; margin-top: 1rem;">${sessionScope.errorep}</p>
                </c:if>    
            </div>
        </div> 
        <jsp:include page="../footer.jsp" />    
        <c:remove var="errorep" scope="session" /> 
    </body>
</html>
