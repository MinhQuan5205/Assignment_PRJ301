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
import modelDAO.ReviewDAO;
import modelDTO.BookDTO;
import modelDTO.CartDTO;
import modelDTO.OrderDTO;
import modelDTO.OrderDetailDTO;
import modelDTO.ReviewDTO;

/**
 *
 * @author ACER
 */
@WebServlet(name = "BookController", urlPatterns = {"/BookController"})
public class BookController extends HttpServlet {

    private static final String WELCOME_PAGE = "homePage.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = WELCOME_PAGE;
        boolean isRedirect = false;

        try {
            String action = request.getParameter("action");
            switch (action) {
                case "bookDetail":
                    isRedirect = true;
                    url = handleBookDetail(request, response);
                    break;
                case "addComment":
                    isRedirect = true;
                    url = handleAddcomment(request, response);
                    break;
                default:
                    request.setAttribute("message", "Hành động không hợp lệ hoặc không được hỗ trợ.");
                    url = "error.jsp";
                    break;
            }
        } catch (Exception e) {
            log("error at BookController: " + e.toString());
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

    private String handleBookDetail(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String bookID_raw = request.getParameter("bookID");//

        if (bookID_raw == null || bookID_raw.isEmpty()) {
            request.setAttribute("error", "Book ID is missing.");
            return "error.jsp";
        }

        try {
            int bookID = Integer.parseInt(bookID_raw);
            BookDAO bdao = new BookDAO();
            BookDTO book = bdao.getBookById(bookID);

            if (book == null) {
                request.setAttribute("error", "Book not found.");
                return "error.jsp";
            }

            session.setAttribute("book", book);
            return "redirect:bookDetail.jsp";
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid Book ID format.");
            return "error.jsp";
        }
    }

    private String handleAddcomment(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String accID = (String) session.getAttribute("accID");

        if (accID == null) {
            session.setAttribute("alert", "Bạn Cần phải đăng nhập trước");
            return "redirect:signin.jsp";
        }

        try {
            int bookID = Integer.parseInt(request.getParameter("bookID"));
            String commentText = request.getParameter("commentText");
            int rating = Integer.parseInt(request.getParameter("rating"));

            ReviewDTO newComment = new ReviewDTO();
            newComment.setBookID(bookID);
            newComment.setAccID(accID);
            newComment.setComment(commentText);
            newComment.setRating(rating);

            ReviewDAO cdao = new ReviewDAO();
            cdao.addReview(newComment);

            return "/BookController?action=bookDetail&bookID=" + bookID;
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Lỗi: Dữ liệu không hợp lệ.");
            return "error.jsp";
        }
    }

}
