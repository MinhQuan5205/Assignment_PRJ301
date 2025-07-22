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
import modelDAO.CartDAO;

/**
 *
 * @author ACER
 */
@WebServlet(name = "UpdateCartServlet", urlPatterns = {"/UpdateCartServlet"})
public class UpdateCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String cartIDStr = request.getParameter("cartID");
            String quantityStr = request.getParameter("quantity");
            
            System.out.println("Received cartID: " + cartIDStr);
            System.out.println("Received quantity: " + quantityStr);
            
            if (cartIDStr == null || quantityStr == null || cartIDStr.trim().isEmpty() || quantityStr.trim().isEmpty()) {
                response.getWriter().write("error: Missing parameters");
                return;
            }
            
            int cartID = Integer.parseInt(cartIDStr);
            int quantity = Integer.parseInt(quantityStr);
            
            if (quantity < 1) {
                response.getWriter().write("error: Invalid quantity");
                return;
            }
            
            CartDAO cartDAO = new CartDAO();
            boolean success = cartDAO.updateCartQuantity(cartID, quantity);
            
            if (success) {
                response.getWriter().write("success");
            } else {
                response.getWriter().write("error: Database update failed");
            }
        } catch (NumberFormatException e) {
            response.getWriter().write("error: Invalid input format");
            System.out.println(e);
        } catch (Exception e) {
           response.getWriter().write("error: " + e.getMessage());
            System.out.println(e);        
        }
    }
}
