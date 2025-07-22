/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package filter;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

/**
 *
 * @author ACER
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {
    
    private static final Set<String> LOGIN_REQUIRED = new HashSet<>(Arrays.asList(
            "/BookController", "/CartController", "/UpdateCartServlet","/OrderController",
            "/cartPage.jsp", "/checkout.jsp", "/edituser.jsp", "/listOrder.jsp", "/orderDetail.jsp", "/userPage.jsp"
    ));
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Init filter if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        // Your filter logic here, e.g.:
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath(); // ví dụ: /BOOKLY
        String path = uri.substring(contextPath.length());
        
        if (LOGIN_REQUIRED.contains(path)) {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                // Chưa đăng nhập => chuyển về trang login
                res.sendRedirect("signin.jsp");
                return;
            }
        }
        // Continue the chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }

}
