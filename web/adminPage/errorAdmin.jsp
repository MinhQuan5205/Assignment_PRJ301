<%-- 
    Document   : errorAdmin
    Created on : Jul 21, 2025, 7:59:33 PM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>THÔNG BÁO LỖI</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #F0F0F0; /* xám sáng */
                color: #ffffff; /* chữ trắng */
                padding: 40px;
                text-align: center;
            }

            .container {
                background-color: #e0e0e0; /* xám nhạt hơn nữa */
                border: 1px solid #b0b0b0; /* viền xám trung tính */
                border-radius: 10px;
                padding: 20px;
                display: inline-block;
                color: #333333; /* chữ trong khối container nên tối lại để dễ nhìn trên nền sáng */
            }

            a {
                color: #333333; /* link màu đậm dễ đọc */
                text-decoration: none;
            }

            a:hover {
                text-decoration: underline;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>🔒 Có lỗi xảy ra!</h2>
            <p><strong>${requestScope.error}</strong></p>
            <p><a href="signin.jsp">← Quay về Trang Signin</a></p>
        </div>
    </body>
</html>
