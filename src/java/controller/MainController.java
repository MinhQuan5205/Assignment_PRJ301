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

/**
 *
 * @author ACER
 */
@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {
    
    private static String WELCOME = "homePage.jsp";
    
    private boolean isLoadDataAction(String action){
        return "homeBook".equals(action) ||
                "listBook".equals(action);
    }
    
    private boolean isUserAction(String action){
        return "signin".equals(action) ||
                "signout".equals(action)||
                "signup".equals(action)||
                "forgotPass".equals(action)||
                "updateUser".equals(action);
    }
    
    private boolean isBookAction(String action){
        return "bookDetail".equals(action) ||
               "addComment".equals(action);
                
    }
    
    private boolean isCartAction(String action){
        return "addToCart".equals(action) ||
               "removeCartItem".equals(action) ||
               "updateCart".equals(action);
    }
    
    private boolean isOrderAction(String action){
        return "processOrder".equals(action) ||
                "listOrder".equals(action) ||
                "orderDetail".equals(action) ||
                "cancelOrder".equals(action)||
                "confirmReceived".equals(action);
    }
    
    
    private boolean isAdminLoadDataAction(String action){
        return "loadDataBook".equals(action) ||
                "loadDataUser".equals(action) ||
                "adminorders".equals(action);
    }
    
    private boolean isAdminAction(String action){
        return "deleteUser".equals(action) ||
                "updateStatus".equals(action) ||
                "addBook".equals(action) ||
                "editBook".equals(action) ||
                "deleteBook".equals(action);
                
    }
//    
//    private boolean isUserServlet(String action){
//        
//    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = WELCOME;
        try {
            String action = request.getParameter("action");
            if (action == null) action = "";
            if(isLoadDataAction(action)){
                url = "/LoadDataController";
            }else if(isUserAction(action)){
                url = "/UserController";
            }else if(isBookAction(action)){
                url = "/BookController";
            }else if(isCartAction(action)){
                url="/CartController";
            }else if(isOrderAction(action)){
                url="/OrderController";
            }else if(isAdminLoadDataAction(action)){
                url="/AdminLoadDataController";
            }else if(isAdminAction(action)){
                url="/AdminController";
            }
        } catch (Exception e) {
            log("error at MainController: "+ e.toString());
        } finally {
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
}
