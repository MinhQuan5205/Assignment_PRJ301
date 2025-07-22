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
import modelDAO.BookDAO;
import modelDAO.CartDAO;
import modelDTO.BookDTO;

/**
 *
 * @author ACER
 */
@WebServlet(name = "CartController", urlPatterns = {"/CartController"})
public class CartController extends HttpServlet {

    private static final String WELCOME_PAGE = "homePage.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = WELCOME_PAGE;
        try {
            String action = request.getParameter("action");
            switch (action) {
                case "addToCart":
                    url = handleAddtoCart(request, response);;
                    break;
                case "removeCartItem":
                    url = handleRemoveCart(request,response);
                    break;
                default:
                    request.setAttribute("error", "Hành động không hợp lệ hoặc không được hỗ trợ.");
                    url = "error.jsp";
                    break;
            }
        } catch (Exception e) {
            log("error at CartController: " + e.toString());
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

    private String handleAddtoCart(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String accID = (String) session.getAttribute("accID");

        if (accID == null) {//nhưng do để vào sesstion nên nó sẽ tồn tại mãi nên khi đăng nhập không thành công nó vẫn hiện lỗi này
            session.setAttribute("alert", "Bạn cần đăng nhập");
            return "redirect:signin.jsp";
        }

        String bookIDStr = request.getParameter("bookID");
        String quantityStr = request.getParameter("quantity");

        if (bookIDStr == null || quantityStr == null || bookIDStr.isEmpty() || quantityStr.isEmpty()) {
            request.setAttribute("error", "Lỗi: Thiếu thông tin sản phẩm hoặc số lượng.");
            return "error.jsp";
        }

        try {
            int bookID = Integer.parseInt(bookIDStr);
            int quantity = Integer.parseInt(quantityStr);
            BookDAO bdao = new BookDAO();
            BookDTO book = bdao.getBookById(bookID);
            
            if (quantity <= 0) {
                request.setAttribute("error","Lỗi: Số lượng phải lớn hơn 0.");
                return "error.jsp";
            }
            
            if (quantity > book.getStock()) {
                request.setAttribute("error", "Số lượng không đủ hàng!");
                String url = request.getContextPath() + "/MainController?action=bookDetail&bookID=" + bookID;
                return url;
            }
            
            CartDAO cdao = new CartDAO();
            cdao.addItemToCart(accID, bookID, quantity);
            
            return "redirect:cartPage.jsp";
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID sản phẩm hoặc số lượng không hợp lệ.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi server khi thêm vào giỏ hàng.");
        }
        return "error.jsp";
    }

    private String handleRemoveCart(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String accID = (String) session.getAttribute("accID");
        
        if (accID == null) {
            session.setAttribute("removeError", "Bạn chưa đăng nhập.");
            return "redirect:cartPage.jsp";
        }
        
        String cartIDStr = request.getParameter("cartID"); 
        if (cartIDStr == null || cartIDStr.isEmpty()) {
            session.setAttribute("removeError", "Cart ID không hợp lệ.");
            return "redirect:cartPage.jsp";
            
        }
        
        try {
            int cartID = Integer.parseInt(cartIDStr);
            CartDAO cdao = new CartDAO();
            boolean removed = cdao.removeCartItem(cartID, accID);

            if (removed) {
                session.setAttribute("removeMessage", "Sản phẩm đã được xóa khỏi giỏ hàng.");
                return "redirect:cartPage.jsp";
                
            } else {
                session.setAttribute("removeError", "Không thể xóa sản phẩm. Vui lòng thử lại.");
                return "redirect:cartpage.jsp";
                
            }
        } catch (NumberFormatException e) {
            session.setAttribute("removeError", "Cart ID không hợp lệ.");
        } catch (Exception e) {
            session.setAttribute("removeError", "Lỗi server: " + e.getMessage());
            System.out.println(e);
        }
        
        return "cartPage.jsp";
    }

}
