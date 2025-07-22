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
import modelDTO.UserDTO;
/**
 *
 * @author ACER
 */
@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {"/*"})
public class AuthorizationFilter implements Filter {
    
    private static final Set<String> ADMIN_PAGES = new HashSet<>(Arrays.asList(
            "/AdminController", "/AdminLoadDataController",
            "/adminPage/addBook.jsp", "/adminPage/adminHomePage.jsp", "/adminPage/adminOrders.jsp",
            "/adminPage/editBook.jsp", "/adminPage/listBook.jsp", "/adminPage/listUser.jsp", "/adminPage/errorAdmin.jsp"
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
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());
        
        HttpSession session = req.getSession(false);
        
        // Nếu yêu cầu truy cập trang quản trị
        if (ADMIN_PAGES.contains(path)) {
            //nếu không có đăng nhập thì sẽ quay trở về trang homePage.jsp
            if (session == null || session.getAttribute("acc") == null) {
                res.sendRedirect(contextPath + "/MainController?action=homeBook");
                return;
            }
            
            // Kiểm tra quyền của người dùng
            Object accObj = session.getAttribute("acc");
            if (accObj != null && accObj instanceof UserDTO) {
                 UserDTO user = (UserDTO) accObj;
                if (user.getRole() != 0) {
                    // Không phải admin => cấm truy cập
                    res.sendRedirect(contextPath + "/error.jsp"); 
                    return;
                }
            } else {
                // Không đúng kiểu hoặc lỗi => redirect về homePage
                res.sendRedirect(contextPath + "/MainController?action=homeBook");
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
