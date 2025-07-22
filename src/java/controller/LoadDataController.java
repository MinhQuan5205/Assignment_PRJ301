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
import java.util.Arrays;
import java.util.List;
import modelDAO.BookDAO;
import modelDTO.BookDTO;
import modelDAO.BookDAO;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 *
 * @author ACER
 */
@WebServlet(name = "LoadDataController", urlPatterns = {"/LoadDataController"})
public class LoadDataController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final String WELCOME_PAGE = "homePage.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = WELCOME_PAGE;

        try {
            String action = request.getParameter("action");
            if (action == null) {
                action = "";
            }
            switch (action) {
                case "homeBook":
                    BookDAO bdao = new BookDAO();
                    List<BookDTO> bookList = bdao.getAllBooks();

                    HttpSession session = request.getSession();
                    session.setAttribute("bookList", bookList);

                    url = "redirect:homePage.jsp";
                    break;
                case "listBook":
                    url = hanldeListProduct(request, response);
                    break;
                default:
                    request.setAttribute("message", "Hành động không hợp lệ hoặc không được hỗ trợ.");
                    url = "error.jsp";
                    break;
            }
        } catch (Exception e) {
            log("error at LoadDataController: " + e.toString());
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

    private String hanldeListProduct(HttpServletRequest request, HttpServletResponse response) {
        BookDAO bdao = new BookDAO();
        List<BookDTO> bookList = bdao.getAllBooks();
        List<BookDTO> filteredBooks = new ArrayList<>(bookList);//tạo 1 bảng sao mà không làm thay đổi danh sách gốc

        HttpSession session = request.getSession();
        String sortBy = request.getParameter("sortBy");
        String[] categoryParams = request.getParameterValues("category");
        String[] authorParams = request.getParameterValues("author");
        String[] priceParams = request.getParameterValues("price");
        String searchName = request.getParameter("searchName");

        //sắp xếp theo các điều kiện tăng dần,giảm dần,số lượng hàng tồn, đánh giá sao 
        if (sortBy != null) {
            switch (sortBy) {
                case "priceAsc":
                    filteredBooks.sort(Comparator.comparingDouble(BookDTO::getFinalPrice));
                    break;
                case "priceDesc":
                    filteredBooks.sort(Comparator.comparingDouble(BookDTO::getFinalPrice).reversed());
                    break;
                case "stockAsc":
                    filteredBooks.sort(Comparator.comparingInt(BookDTO::getStock));
                    break;
                case "ratingDesc":
                    filteredBooks.sort(Comparator.comparingDouble(BookDTO::getRating).reversed());
                    break;
            }
        }

        //chọn theo thể loại
        if (categoryParams != null) {
            List<BookDTO> tmpBooks = new ArrayList<>(filteredBooks);
            filteredBooks.clear();
            for (BookDTO b : tmpBooks) {
                for (String cadId : categoryParams) {
                    if (String.valueOf(b.getGenreID()).equals(cadId.trim())) {
                        filteredBooks.add(b);
                        break;
                    }
                }
            }
        }

        //chọn theo tác giả
        if (authorParams != null) {
            List<BookDTO> tmpBook = new ArrayList<>(filteredBooks);
            filteredBooks.clear();
            for (BookDTO b : tmpBook) {
                for (String authorId : authorParams) {
                    if (String.valueOf(b.getAuthorID()).equals(authorId.trim())) {
                        filteredBooks.add(b);
                        break;
                    }
                }
            }
        }

        //chọn theo giá tiền theo khoản
        if (priceParams != null) {
            List<BookDTO> tmpBooks = new ArrayList<>(filteredBooks);
            filteredBooks.clear();
            for (BookDTO b : tmpBooks) {
                double finalPrice = b.getFinalPrice();
                for (String priceRange : priceParams) {
                    String trimmedPriceRange = priceRange.trim();

                    if (trimmedPriceRange.equals("0-100000") && finalPrice >= 0 && finalPrice <= 100000) {
                        filteredBooks.add(b);
                        break;
                    } else if (trimmedPriceRange.equals("100000-200000") && finalPrice > 100000 && finalPrice <= 200000) {
                        filteredBooks.add(b);
                        break;
                    } else if (trimmedPriceRange.equals("200000-500000") && finalPrice > 200000 && finalPrice <= 500000) {
                        filteredBooks.add(b);
                        break;
                    } else if (trimmedPriceRange.equals("500000+") && finalPrice > 500000) {
                        filteredBooks.add(b);
                        break;
                    }
                }
            }
        }

        //tìm kiếm sản phẩm theo searchName
        if (searchName != null && !searchName.trim().isEmpty()) {
            List<BookDTO> tmpBooks = new ArrayList<>(filteredBooks);
            filteredBooks.clear();
            String lowerSearchName = searchName.trim().toLowerCase();
            for (BookDTO b : tmpBooks) {
                if (b.getTitle().toLowerCase().contains(lowerSearchName)) {
                    filteredBooks.add(b);
                }
            }
        }

        //phân loại sản phẩm theo tồn kho
        //lấy những sản phẩm hết hàng
        List<BookDTO> outOfStockBooks = filteredBooks.stream()
                .filter(b -> b.getStock() == 0)
                .collect(Collectors.toList());
        //lấy những sản phẩm còn hàng 
        List<BookDTO> inStockBooks = filteredBooks.stream()
                .filter(b -> b.getStock() > 0)
                .collect(Collectors.toList());

        int page, numperpage = 16;//hiển thị numberPage là số sản phẩm tối đa trên 1 trang còn page là số trang 
        int size = inStockBooks.size();

        //tính tổng số trang cần có dựa trên tổng số sách hiện có
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

        List<BookDTO> list = bdao.getListByPage(inStockBooks, start, end);

        session.setAttribute("page", page);
        session.setAttribute("numpage", numpage);
        session.setAttribute("books", list);
        session.setAttribute("outOfStockBooks", outOfStockBooks);

        session.setAttribute("searchName", searchName);
        session.setAttribute("sortBy", sortBy);
        session.setAttribute("categoryParams", categoryParams);
        session.setAttribute("authorParams", authorParams);
        session.setAttribute("priceParams", priceParams);
        return "redirect:shopBook.jsp";
    }

}
