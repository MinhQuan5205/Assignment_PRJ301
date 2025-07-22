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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import modelDAO.BookDAO;
import modelDAO.CartDAO;
import modelDAO.OrderDAO;
import modelDAO.OrderDetailDAO;
import modelDAO.UserDAO;
import modelDTO.BookDTO;
import modelDTO.CartDTO;
import modelDTO.OrderDTO;
import modelDTO.OrderDetailDTO;
import modelDTO.UserDTO;
import utils.Email;

/**
 *
 * @author ACER
 */
@WebServlet(name = "OrderController", urlPatterns = {"/OrderController"})
public class OrderController extends HttpServlet {

    private static final String WELCOME_PAGE = "homePage.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = WELCOME_PAGE;

        try {
            String action = request.getParameter("action");
            switch (action) {
                case "processOrder":
                    url = handleProcessOrder(request, response);
                    break;
                case "listOrder":
                    url = handleListOrder(request, response);
                    break;
                case "orderDetail":
                    url = handleOrderDetail(request, response);
                    break;
                case "cancelOrder":
                    url = handleCancelOrder(request, response);
                    break;
                case "confirmReceived":
                    url = handleConfirmReceived(request, response);
                    break;
                default:
                    request.setAttribute("error", "Hành động không hợp lệ hoặc không được hỗ trợ.");
                    url = "error.jsp";
                    break;
            }
        } catch (Exception e) {
            log("error at OrderController: " + e.toString());
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

    private String handleProcessOrder(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String accID = (String) session.getAttribute("accID");
        
        if (accID == null) {
            return "redirect:signin.jsp";
        }

        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        
        UserDAO udao = new UserDAO();
        UserDTO user = udao.getUserById(accID);
        CartDAO cdao = new CartDAO();
        List<CartDTO> cartItems = cdao.getCartByAccID(accID);

        //kiểm tra đủ hàng trước
        for (CartDTO cartItem : cartItems) {
            int bookID = cartItem.getBook().getBookID();
            int currentStock = new BookDAO().getStockByBookID(bookID);
            if (currentStock < cartItem.getQuantity()) {
                request.setAttribute("errorMessage", "Sản phẩm '" + cartItem.getBook().getTitle() + "' không còn đủ hàng trong kho.");
                return "checkout.jsp";
            }
        }
        //tính tổng tiền 
        double totalPrice = 0;
        for (CartDTO cartItem : cartItems) {
            totalPrice += cartItem.getTotalPrice();
        }

        //tạo đơn hàng 
        OrderDTO order = new OrderDTO();

        order.setUserID(accID);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        order.setCreate_at(now.format(formatter));
        order.setTotalPrice(totalPrice);
        order.setAddress(address);
        order.setReceiver_name(name);
        order.setReceiver_phone(phone);
        order.setStatus("pending");

        OrderDAO odao = new OrderDAO();
        int orderID = odao.createOrder(order);

        if (orderID != -1) {
            OrderDetailDAO ordao = new OrderDetailDAO();

            for (CartDTO cartItem : cartItems) {
                OrderDetailDTO orderdetail = new OrderDetailDTO();
                orderdetail.setOrderID(orderID);
                orderdetail.setBookID(cartItem.getBook().getBookID());
                orderdetail.setQuantity(cartItem.getQuantity());

                ordao.createOrderDetail(orderdetail);
                cdao.reduceStock(cartItem.getBook().getBookID(), cartItem.getQuantity());
                odao.removeCartItem(cartItem.getCartID(), accID);
            }

            String subject = "Xác Nhận Đặt Hàng Thành Công - BOOKLY";

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
                    + "    <h2>Bạn Đã Đặt Hàng Thành Công Ở BOOKLY!</h2>"
                    + "    <p>Thông tin Đơn Hàng Mà Bạn Đã Đặt:</p>"
                    + "    <ul>"
                    + "      <li><strong>Tổng Tiền:</strong> " + order.getTotalPrice() + "</li>"
                    + "      <li><strong>Địa Chịa Người Nhận:</strong> " + order.getAddress() + "</li>"
                    + "      <li><strong>Số Điện Thoại:</strong> " + order.getReceiver_phone() + "</li>"
                    + "      <li><strong>Thời Gian Đặt Hàng:</strong> " + order.getCreate_at() + "</li>"
                    + "      <li><strong>Tình Trạng Đơn Hàng:</strong> " + order.getStatus() + "</li>"
                    + "    </ul>"
                    + "    <p>Trân trọng,<br/>Đội ngũ BOOKLY</p>"
                    + "  </div>"
                    + "</body>"
                    + "</html>";
            
            Email.sendEmail(user.getEmail(), subject, content);
            return "redirect:MainController?action=listBook";
        } else {
            request.setAttribute("errorMessage", "Đặt hàng không thành công. Vui lòng thử lại.");
            return "checkout.jsp";
        }
    }

    private String handleListOrder(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserDTO account = (UserDTO) session.getAttribute("acc");

        String accID = account.getAccID();
        OrderDAO odao = new OrderDAO();
        List<OrderDTO> orders = odao.getOrdersByAccID(accID);

        session.setAttribute("orders", orders);
        return "listOrder.jsp";
    }

    private String handleOrderDetail(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserDTO account = (UserDTO) session.getAttribute("acc");

        String orderIdStr = request.getParameter("orderID");
        int orderId;

        try {
            orderId = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            session.setAttribute("alertMessage", "Mã đơn hàng không hợp lệ!");
            return "redirect:MainController?action=listOrder";
        }

        OrderDAO odao = new OrderDAO();
        OrderDTO order = odao.getOrderById(orderId);

        if (order == null) {
            session.setAttribute("alertMessage", "Đơn hàng không tồn tại!");
            return "redirect:MainController?action=listOrder";
        }

        if (!order.getUserID().equals(account.getAccID()) && account.getRole() != 0) {
            session.setAttribute("alertMessage", "Bạn không có quyền xem đơn hàng này!");
            return "redirect:MainController?action=listOrder";
        }

        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
        List<OrderDetailDTO> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(orderId);

        BookDAO bdao = new BookDAO();
        for (OrderDetailDTO detail : orderDetails) {
            BookDTO book = bdao.getBookById(detail.getBookID());
            detail.setBook(book);
        }

        request.setAttribute("order", order);
        request.setAttribute("orderDetails", orderDetails);
        return "orderDetail.jsp";
    }

    private String handleCancelOrder(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserDTO account = (UserDTO) session.getAttribute("acc");

        String orderIdStr = request.getParameter("orderID");
        int orderId;
        try {
            orderId = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Mã đơn hàng không hợp lệ");
            return "error.jsp";
        }

        String accID = account.getAccID();
        OrderDAO odao = new OrderDAO();
        OrderDTO order = odao.getOrderById(orderId);

        if (order == null) {
            request.setAttribute("error", "Không tìm thấy đơn hàng");
            return "error.jsp";
        }

        if (!order.getUserID().equals(accID)) {
            request.setAttribute("error", "Bạn không có quyền hủy đơn hàng này");
            return "error.jsp";
        }

        //chỉ có thể hủy với những đơn hàng đang chờ xử lý
        if (!order.getStatus().equals("pending")) {
            request.setAttribute("message", "Chỉ có thể hủy đơn hàng đang chờ xử lý.");
            return ("listOrder.jsp");
        }

        boolean success = odao.updateOrderStatus(orderId, "cancelled");

        if (success) {
            request.setAttribute("message", "Hủy đơn hàng thành công!");
        } else {
            request.setAttribute("message", "Có lỗi xảy ra khi hủy đơn hàng.");
        }
        return "redirect:MainController?action=listOrder";
    }

    private String handleConfirmReceived(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserDTO account = (UserDTO) session.getAttribute("acc");

        String orderIdStr = request.getParameter("orderID");
        int orderId;
        try {
            orderId = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            session.setAttribute("alertMessage", "Mã đơn hàng không hợp lệ!");
            return "redirect:MainController?action=listOrder";
        }

        OrderDAO odao = new OrderDAO();
        boolean success = odao.updateOrderStatus(orderId, "delivered");

        if (success) {
            session.setAttribute("alertMessage", "Xác nhận đã nhận hàng thành công!");
        } else {
            session.setAttribute("alertMessage", "Xác nhận không thành công, vui lòng thử lại!");
        }
        return "redirect:MainController?action=listOrder";
    }
}
