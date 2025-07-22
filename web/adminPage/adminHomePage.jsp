<%-- 
    Document   : adminHome.jsp
    Created on : Jul 16, 2025, 3:10:09 PM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản Lý BOOKLY</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@100..900&family=Nunito:wght@200..1000&family=Roboto:wght@100;300;400;500;700;900&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" integrity="sha512-9usAa10IRO0HhonpyAIVpjrylPvoDwiPUiKdWk5t3PyolY1cOd4DSE0Ga+ri4AuTroPR5aQvXU9xC6qOPnzFeg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link rel="stylesheet" href="../adminPage/admincss/adminHomePage.css"/>
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
        <section style="height: 300px; margin: 30px auto;" id="services" class="py-5 bg-light">
            <div style="width: 100%; margin: 0 auto;" style="padding-top: 50px;" class="container">
                <div class="row g-10">
                    <div class="col-md-4">
                        <a href="${pageContext.request.contextPath}/MainController?action=loadDataBook" class="card-link">
                            <div class="card h-100">
                                <div class="card-body text-center">
                                    <i class="bi bi-box fs-1"></i> 
                                    <h3 class="card-title">QUẢN LÝ SẢN PHẨM</h3>
                                </div>
                            </div>
                        </a>
                    </div>
                    <div class="col-md-4">
                        <a href="${pageContext.request.contextPath}/MainController?action=loadDataUser" class="card-link"> 
                            <div class="card h-100">
                                <div class="card-body text-center">
                                    <i class="bi bi-file-earmark-person-fill fs-1"></i> 
                                    <h3 class="card-title">QUẢN LÝ NGƯỜI DÙNG</h3>
                                </div>
                            </div>
                        </a>
                    </div>
                    <div class="col-md-4">
                        <a href="${pageContext.request.contextPath}/MainController?action=adminorders" class="card-link"> 
                            <div class="card h-100">
                                <div class="card-body text-center">
                                    <i class="bi bi-archive-fill fs-1"></i> 
                                    <h3 class="card-title">QUẢN LÝ ĐƠN HÀNG</h3>
                                </div>
                            </div>
                        </a>
                    </div>
                </div>
            </div>
        </section>
        <jsp:include page="../footer.jsp" />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
