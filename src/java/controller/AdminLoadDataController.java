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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import modelDAO.BookDAO;
import modelDAO.OrderDAO;
import modelDAO.UserDAO;
import modelDTO.BookDTO;
import modelDTO.OrderDTO;
import modelDTO.UserDTO;

/**
 *
 * @author ACER
 */
@WebServlet(name = "AdminLoadDataController", urlPatterns = {"/AdminLoadDataController"})
public class AdminLoadDataController extends HttpServlet {

    private static final String WELCOME_PAGE_ADMIN = "adminHomePage.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = WELCOME_PAGE_ADMIN;

        try {
            String action = request.getParameter("action");
            switch (action) {
                case "loadDataBook":
                    url = handleloadDataBook(request, response);
                    break;
                case "loadDataUser":
                    url = handleloadDataUser(request, response);
                    break;
                case "adminorders":
                    url = handleaAminorders(request, response);
                    break;
                default:
                    request.setAttribute("error", "Hành động không hợp lệ hoặc không được hỗ trợ.");
                    url = "adminPage/errorAdmin.jsp";
                    break;
            }
        } catch (Exception e) {
            log("error at AdminLoadDataController: " + e.toString());
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

    private String handleloadDataBook(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        BookDAO bdao = new BookDAO();

        List<BookDTO> bookList = bdao.getAllBooks();
        List<BookDTO> filteredBooks = new ArrayList<>();

        String searchName = request.getParameter("searchName");
        String category = request.getParameter("category");
        String author = request.getParameter("author");
        String stockRange = request.getParameter("stock");

        for (BookDTO book : bookList) {
            boolean matchesSearchName = true;
            if (searchName != null && !searchName.isEmpty()) {
                if (!book.getTitle().toLowerCase().contains(searchName.toLowerCase())) {
                    matchesSearchName = false;
                }
            }

            boolean matchesCategory = true;
            if (category != null && !category.isEmpty()) {
                if (book.getGenreID() != Integer.parseInt(category.trim())) {
                    matchesCategory = false;
                }
            }

            boolean matchesAuthor = true;
            if (author != null && !author.isEmpty()) {
                if (book.getAuthorID() != Integer.parseInt(author.trim())) {
                    matchesAuthor = false;
                }
            }

            boolean matchesStock = true;
            if (stockRange != null && !stockRange.isEmpty()) {
                int stock = book.getStock();
                switch (stockRange) {
                    case "0-10":
                        if (!(stock >= 0 && stock <= 10)) {
                            matchesStock = false;
                        }
                        break;
                    case "10-50":
                        if (!(stock > 10 && stock <= 50)) {
                            matchesStock = false;
                        }
                        break;
                    case "50-100":
                        if (!(stock > 50 && stock <= 100)) {
                            matchesStock = false;
                        }
                        break;
                    case "100+":
                        if (!(stock >= 100)) {
                            matchesStock = false;
                        }
                        break;
                }
            }

            if (matchesSearchName && matchesCategory && matchesAuthor && matchesStock) {
                filteredBooks.add(book);
            }
        }

        int page, numperpage = 10;
        int size = filteredBooks.size();
        int numpage = (size % numperpage == 0 ? (size / numperpage) : ((size / numperpage)) + 1);
        String xpage = request.getParameter("page");

        if (xpage == null) {
            page = 1;
        } else {
            page = Integer.parseInt(xpage.trim());
        }

        int start, end;
        start = (page - 1) * numperpage;
        end = Math.min(page * numperpage, size);

        List<BookDTO> listByPage = new ArrayList<>();
        if (start < size) {
            listByPage = filteredBooks.subList(start, end);
        }

        session.setAttribute("page", page);
        session.setAttribute("numpage", numpage);
        session.setAttribute("books", listByPage);

        session.setAttribute("searchName", searchName);
        session.setAttribute("category", category);
        session.setAttribute("author", author);
        session.setAttribute("stock", stockRange);
        return ("redirect:adminPage/listBook.jsp");
    }

    private String handleloadDataUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserDTO loggedInUser = (UserDTO) session.getAttribute("acc");
        
        
        if (loggedInUser == null || loggedInUser.getRole() != 0) {
            request.setAttribute("error", "lỗi do bạn không có quyền truy cập vào trang này");
            return "adminPage/errorAdmin.jsp";
        }
        
        String searchName = request.getParameter("searchName");
        String roleParam = request.getParameter("role");
        Integer role = null;
        
        if (roleParam != null && !roleParam.isEmpty()) {
            try {
                role = Integer.parseInt(roleParam);
            } catch (NumberFormatException e) {
                role = null;
            }
        }
        
        UserDAO udao = new UserDAO();
        List<UserDTO> users;
        
        if((searchName == null || searchName.isEmpty()) && role == null){
            users = udao.getAllUser();
        }else{
            users = udao.getUsersByFilter(searchName, role);
        }
        session.setAttribute("users", users);
        return "redirect:adminPage/listUser.jsp";
    }

    private String handleaAminorders(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserDTO account = (UserDTO) session.getAttribute("acc");
        String contextPath = request.getContextPath();
        
        if (account == null || account.getRole() != 0) {
            request.setAttribute("error", "Access denied");
            return "adminPage/errorAdmin.jsp";
        }
        
        String sort = request.getParameter("sort");
        
        OrderDAO odao = new OrderDAO();
        if(sort == null){
            sort="date_desc";//sấp xếp theo ngày giảm dần
        }
        
        List<OrderDTO> orders = odao.getAllOrders(sort);
        
        int page, numperpage = 10;
        int size = orders.size();
        
        int numpage = (size % numperpage == 0 ? (size/numperpage) : ((size/numperpage)) + 1);
        String xpage = request.getParameter("page");
        if(xpage == null) {
            page = 1;
        } else {
            page = Integer.parseInt(xpage);
        }
        
        int start, end;
        start = (page - 1) * numperpage;
        end = Math.min(page * numperpage, size);
        
        List<OrderDTO> list = odao.getListByPage(orders, start, end);
        session.setAttribute("page", page);
        session.setAttribute("numpage", numpage);
        
        if (list == null) {
            list = Collections.emptyList();//trả về 1 danh sách rỗng không có khả năng thêm xóa sửa
        }
        
        session.setAttribute("sort", sort); 
        session.setAttribute("orders", list);
        return ("redirect:adminPage/adminOrders.jsp");
    }

}
