<%-- 
    Document   : addBook
    Created on : Jul 21, 2025, 1:05:25 PM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="errors" value="${sessionScope.errors}" />
<c:set var="bookInput" value="${sessionScope.bookInput}" />
<c:remove var="errors" scope="session" />
<c:remove var="bookInput" scope="session" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thêm Sách</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&family=Nunito:ital,wght@0,200..1000;1,200..1000&family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap" rel="stylesheet">
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
                <h1>THÊM SẢN PHẨM</h1>

                <form action="${pageContext.request.contextPath}/MainController?action=addBook" method="post" class="form-part">
                    <c:if test="${not empty errors.bookTitle}">
                        <span style="color: red;">${errors.bookTitle}</span>
                    </c:if>
                    <input type="text" name="bookTitle" placeholder="Tên sản phẩm" value="${bookInput.title}" required/>
                    
                    <c:if test="${not empty errors.price}">
                        <span style="color: red;">${errors.price}</span>
                    </c:if>
                    <input type="text" name="price" placeholder="Giá tiền" value="${bookInput.price}" required/>
                    
                    <c:if test="${not empty errors.description}">
                        <span style="color: red;">${errors.description}</span>
                    </c:if>
                    <textarea name="description" placeholder="Mô tả Sách" required>${bookInput.description}</textarea>
                    
                    <c:if test="${not empty errors.genreID}">
                        <span style="color: red;">${errors.genreID}</span>
                    </c:if>
                    <select id="category" name="category" required>
                        <option value="" disabled hidden ${empty bookInput.genreID ? 'selected' : ''}>Chọn danh mục</option>
                        <c:forEach var="c" items="${sessionScope.categories}">
                            <option value="${c.genreID}" ${c.genreID == bookInput.genreID ? 'selected' : ''}>${c.genreName}</option>
                        </c:forEach>
                    </select>
                    
                    <c:if test="${not empty errors.authorID}">
                        <span style="color: red;">${errors.authorID}</span>
                    </c:if>
                    <select id="author" name="author" required>
                        <option value="" disabled hidden ${empty bookInput.authorID ? 'selected' : ''}>Chọn tác giả</option>
                        <c:forEach var="a" items="${sessionScope.authors}">
                            <option value="${a.authorID}" ${a.authorID == bookInput.authorID ? 'selected' : ''}>${a.authorName}</option>
                        </c:forEach>
                    </select>
                    
                    <c:if test="${not empty errors.stock}">
                        <span style="color: red;">${errors.stock}</span>
                    </c:if>   
                    <input type="number" name="stock" placeholder="Số lượng sản phẩm"  min="1"  value="${bookInput.stock}" required />
                    
                    <c:if test="${not empty errors.discount}">
                        <span style="color: red;">${errors.discount}</span>
                    </c:if>
                    <input type="text" name="discount" placeholder="Chọn % giảm giá" value="${bookInput.discount}" required>
                    
                    
                    <label for="imageurl" id="fileLabel" class="file-label">Nhập hình ảnh của sách</label>
                    <c:if test="${not empty errors.imageURL}">
                        <span style="color: red;">${errors.imageURL}</span>
                    </c:if>
                    <input type="text" id="imageurl" name="imageurl" 
                           value="${bookInput.imageURL}" placeholder="Dán URL hình ảnh vào đây" required>
               
                    <button type="submit" class="login-btnn">XÁC NHẬN THÊM</button>
                </form>
                <c:if test="${sessionScope.errorap != null}">
                    <p style="color: red; margin-bottom: 0px; margin-top: 1rem;">${sessionScope.errorap}</p>
                </c:if>
            </div>
        </div> 
        <jsp:include page="../footer.jsp" />
    </body>
</html>
