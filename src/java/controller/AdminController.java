/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelDAO.AuthorDAO;
import modelDAO.BookDAO;
import modelDAO.GenreDAO;
import modelDAO.OrderDAO;
import modelDAO.UserDAO;
import modelDTO.AuthorDTO;
import modelDTO.BookDTO;
import modelDTO.GenreDTO;
import modelDTO.OrderDTO;
import modelDTO.UserDTO;
import utils.Email;

/**
 *
 * @author ACER
 */
@WebServlet(name = "AdminController", urlPatterns = {"/AdminController"})
public class AdminController extends HttpServlet {

    private static final String WELCOME_PAGE_ADMIN = "adminHomePage.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = WELCOME_PAGE_ADMIN;
        try {
            String action = request.getParameter("action");
            switch (action) {
                case "deleteUser":
                    url = handleDeleteUser(request, response);
                    break;
                case "updateStatus":
                    url = handleUpdateStatusOrder(request, response);
                    break;
                case "addBook":
                    url = handleAddBook(request, response);
                    break;
                case "editBook":
                    url = handleEditBook(request, response);
                    break;
                case "deleteBook":
                    url = handleDeleteBook(request, response);
                    break;
                default:
                    request.setAttribute("error", "Hành động không hợp lệ hoặc không được hỗ trợ.");
                    url = "adminPage/errorAdmin.jsp";
                    break;
            }
        } catch (Exception e) {
            log("error at AdminController: " + e.toString());
        }

        if (url.startsWith("redirect:")) {
            response.sendRedirect(url.substring(9)); // bỏ "redirect:"
        } else {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private String handleDeleteUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserDTO loggedInUser = (UserDTO) session.getAttribute("acc");

        if (loggedInUser == null || loggedInUser.getRole() != 0) {//chặn điều kiện
            request.setAttribute("error", "Bạn Không Có Quyền Xóa Người dùng");
            return "adminPage/errorAdmin.jsp";
        }

        String accID = request.getParameter("accIDdel");

        if (accID == null || accID.isEmpty()) {
            request.setAttribute("error", "Không tìm thấy ID người dùng.");
            return "adminPage/errorAdmin.jsp";
        }

        UserDAO udao = new UserDAO();
        boolean success = udao.changeAvtiveUser(accID);

        if (success) {
            session.removeAttribute("users");
            return "redirect:MainController?action=loadDataUser";
        } else {
            request.setAttribute("error", "Xóa người dùng thất bại. Vui lòng thử lại.");
            return "adminPage/errorAdmin.jsp";

        }
    }

    private boolean isValidStatus(String status) {
        return status.equals("pending") || status.equals("confirmed")
                || status.equals("shipped") || status.equals("delivered")
                || status.equals("cancelled");
    }

    private String handleUpdateStatusOrder(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserDTO account = (UserDTO) session.getAttribute("acc");
        String contextPath = request.getContextPath();
        if (account == null || account.getRole() != 0) {//kiểm tra phân quyền
            request.setAttribute("error", "Access denied");
            return "adminPage/errorAdmin.jsp";
        }

        int orderId = Integer.parseInt(request.getParameter("orderID"));
        String status = request.getParameter("status");

        if (!isValidStatus(status)) {
            request.setAttribute("error", "Invalid status");
            return "adminPage/errorAdmin.jsp";
        }

        OrderDAO ordao = new OrderDAO();
        OrderDTO order = ordao.getOrderById(orderId);
        if (order != null && !order.getStatus().equals("cancelled")) {
            boolean success = ordao.updateOrderStatus(orderId, status);
            if (success) {
                String subject = "Xác Nhận Thay Đổi Tình Trạng Đơn Hàng - BOOKLY";

                String content = "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "  <meta charset='UTF-8'>"
                        + "  <style>"
                        + "    body { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; }"
                        + "    .container { background-color: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }"
                        + "    h2 { color: #333; }"
                        + "    p { color: #555; line-height: 1.6; }"
                        + "    .button { display: inline-block; padding: 10px 20px; background-color: #007bff; color: #fff;"
                        + "              text-decoration: none; border-radius: 5px; margin-top: 20px; }"
                        + "  </style>"
                        + "</head>"
                        + "<body>"
                        + "  <div class='container'>"
                        + "    <h2>Chào mừng bạn đến với BOOKLY!</h2>"
                        + "    <p>Đơn Hàng Của Bạn Đã Được Thay Đổi Trạng Thái.</p>"
                        + "    <p>Thông tin Trạng Thái Đơn Hàng Của Bạn:</p>"
                        + "    <ul>"
                        + "      <li><strong>Mã Đơn Hàng</strong> " + order.getOrderID() + "</li>"
                        + "      <li><strong>Trạng Thái Đã Được Cập Nhật:</strong> " + order.getStatus() + "</li>"
                        + "    </ul>"
                        + "    <p>Trân trọng,<br/>Đội ngũ BOOKLY</p>"
                        + "  </div>"
                        + "</body>"
                        + "</html>";
                UserDAO udao = new UserDAO();
                UserDTO userRecieved = udao.getUserById(order.getUserID());
                Email.sendEmail(userRecieved.getEmail(), subject, content);
                return "redirect:" + contextPath + "/MainController?action=adminorders";
            } else {
                request.setAttribute("message", "Cập nhật trạng thái không thành công.");
                return contextPath + "/MainController?action=adminorders";
            }
        }

        request.setAttribute("error", "Không Thể Cập Nhật Đơn Hàng");
        return contextPath + "/MainController?action=adminorders";
    }

    private String handleAddBook(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        if ("GET".equalsIgnoreCase(request.getMethod())) {//để phân biệt method nào là get thì chỉ có loadCategory và lòa authors mà thôi
            GenreDAO gdao = new GenreDAO();
            AuthorDAO adao = new AuthorDAO();

            List<GenreDTO> categories = gdao.getAllGenre();
            List<AuthorDTO> authors = adao.getAllAuthors();

            session.setAttribute("categories", categories);
            session.setAttribute("authors", authors);
            return "redirect:adminPage/addBook.jsp";
        }

        boolean hasError = false;
        Map<String, String> errors = new HashMap<>();

        String bookTitle = request.getParameter("bookTitle");
        String priceStr = request.getParameter("price");
        String description = request.getParameter("description");
        String genreIdStr = request.getParameter("category");
        String authorIdStr = request.getParameter("author");
        String discountStr = request.getParameter("discount");
        String stockStr = request.getParameter("stock");
        String imageURL = request.getParameter("imageurl");

        double price = 0;
        int genreID = 0;
        int authorID = 0;
        double discount = 0;
        int stock = 0;

        if (bookTitle == null || bookTitle.trim().isEmpty()) {
            errors.put("bookTitle", "Tên sách không được để trống.");
            hasError = true;
        }

        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) {
                errors.put("price", "Giá phải lớn hơn hoặc bằng 0.");
                hasError = true;
            }
        } catch (NumberFormatException e) {
            errors.put("price", "Giá không hợp lệ.");
            hasError = true;
        }

        if (description == null || description.trim().isEmpty()) {
            errors.put("description", "Mô tả không được để trống.");
            hasError = true;
        }

        try {
            genreID = Integer.parseInt(genreIdStr);
        } catch (NumberFormatException e) {
            errors.put("category", "Thể loại không hợp lệ.");
            hasError = true;
        }

        try {
            authorID = Integer.parseInt(authorIdStr);
        } catch (NumberFormatException e) {
            errors.put("author", "Tác giả không hợp lệ.");
            hasError = true;
        }

        try {
            discount = Double.parseDouble(discountStr);
            if (discount < 0 || discount > 100) {
                errors.put("discount", "Giảm giá phải từ 0 đến 100.");
                hasError = true;
            }
        } catch (NumberFormatException e) {
            errors.put("discount", "Giảm giá không hợp lệ.");
            hasError = true;
        }

        try {
            stock = Integer.parseInt(stockStr);
            if (stock < 0) {
                errors.put("stock", "Số lượng tồn kho phải >= 0.");
                hasError = true;
            }
        } catch (NumberFormatException e) {
            errors.put("stock", "Tồn kho không hợp lệ.");
            hasError = true;
        }

        if (imageURL == null || imageURL.trim().isEmpty()) {
            errors.put("imageurl", "Đường dẫn ảnh không được để trống.");
            hasError = true;
        }

        if (hasError) {
            // Gửi lại dữ liệu người dùng nhập và thông báo lỗi
            session.setAttribute("errors", errors);
            session.setAttribute("bookInput", new BookDTO(0, bookTitle, price, description, genreID, authorID, imageURL, discount, stock));
            return "redirect:adminPage/addBook.jsp";
        }

        BookDTO newBook = new BookDTO(0, bookTitle, price, description, genreID, authorID, imageURL, discount, stock);

        BookDAO bdao = new BookDAO();
        bdao.addBook(newBook);

        session.setAttribute("successMessage", "Thêm sách thành công!");
        return "redirect:MainController?action=loadDataBook";

    }

    private String handleEditBook(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        if ("GET".equalsIgnoreCase(request.getMethod())) {//để phân biệt method nào là get thì chỉ có loadCategory và lòa authors mà thôi
            GenreDAO gdao = new GenreDAO();
            AuthorDAO adao = new AuthorDAO();

            List<GenreDTO> categories = gdao.getAllGenre();
            List<AuthorDTO> authors = adao.getAllAuthors();

            int bookID = Integer.parseInt(request.getParameter("bookID"));
            BookDAO bdao = new BookDAO();
            BookDTO book = bdao.getBookById(bookID);

            session.setAttribute("book", book);
            session.setAttribute("categories", categories);
            session.setAttribute("authors", authors);
            return "redirect:adminPage/editBook.jsp";
        }

        try {
            int bookID = Integer.parseInt(request.getParameter("bookID"));
            String bookTitle = request.getParameter("bookTitle");
            double price = Double.parseDouble(request.getParameter("price"));
            String description = request.getParameter("description");
            int genreID = Integer.parseInt(request.getParameter("category"));
            int authorID = Integer.parseInt(request.getParameter("author"));
            double discount = Double.parseDouble(request.getParameter("discount"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            String imageURL = request.getParameter("imageurl");

            if (bookTitle == null || bookTitle.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên sản phẩm không được để trống!");
            }
            if (description == null || description.trim().isEmpty()) {
                throw new IllegalArgumentException("Mô tả không được để trống!");
            }

            if (imageURL == null || imageURL.isEmpty()) {
                imageURL = request.getParameter("currentImage");
            }

            BookDTO updateBook = new BookDTO(bookID, bookTitle, price, description, genreID, authorID, imageURL, discount, stock);
            BookDAO bdao = new BookDAO();

            boolean isUpdated = bdao.updateBook(updateBook);

            if (isUpdated) {
                session.setAttribute("success", "Cập nhật sản phẩm thành công!");
                return "redirect:MainController?action=loadDataBook";
            } else {
                session.setAttribute("errorep", "Cập nhật sản phẩm thất bại!");
                session.setAttribute("book", updateBook);
                return "redirect:adminPage/editBook.jsp";
            }

        } catch (NumberFormatException e) {
            System.err.println("Lỗi chuyển đổi số: " + e.getMessage());
            session.setAttribute("errorep", "Dữ liệu không hợp lệ! Vui lòng kiểm tra lại.");
            return "redirect:adminPage/editBook.jsp";
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi dữ liệu: " + e.getMessage());
            session.setAttribute("errorep", e.getMessage());
            return "redirect:adminPage/editBook.jsp";
        }
    }

    private String handleDeleteBook(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        try {
            int bookId = Integer.parseInt(request.getParameter("bookID"));
            BookDAO bdao = new BookDAO();
            boolean deleted = bdao.updateStatusBookById(bookId);

            if (deleted) {
                session.setAttribute("successMessage", "Xóa sản phẩm thành công!");
            } else {
                session.setAttribute("errorMessage", "Không thể xóa sản phẩm!");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "ID sản phẩm không hợp lệ!");
        }
        return "redirect:MainController?action=loadDataBook";
    }

}
