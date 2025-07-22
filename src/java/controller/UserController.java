/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelDAO.UserDAO;
import modelDTO.UserDTO;
import utils.Email;
import utils.PasswordUtils;

/**
 *
 * @author ACER
 */
@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {

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
                case "signin":
                    url = handleSignin(request, response);
                    break;
                case "signup":
                    url = handleSignup(request, response);
                    break;
                case "signout":
                    url = handleSignout(request, response);
                    HttpSession session = request.getSession(false);//chỉ kiểm tra xem session đã tồn tại hay chưa

                    if (session != null) {
                        session.invalidate();
                    }
                    url = "MainController?action=homeBook";
                    break;
                case "forgotPass":
                    url = handleForgotPass(request, response);
                    break;
                case "updateUser":
                    url = handleupdateUser(request, response);
                    break;
                default:
                    request.setAttribute("message", "Hành động không hợp lệ hoặc không được hỗ trợ.");
                    url = "error.jsp";
                    break;
            }
        } catch (Exception e) {
            log("error at UserController: " + e.toString());
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

    private void deleteRememberMeCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user")
                        || cookie.getName().equals("pass")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    private String handleSignin(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserDAO udao = new UserDAO();

        //kiểm tra method nếu là GET thì tự động đặng nhập
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            Cookie[] cookies = request.getCookies();
            String user = null;
            String pass = null;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("user")) {
                        user = cookie.getValue();
                    } else if (cookie.getName().equals("pass")) {
                        pass = cookie.getValue();
                    }
                }
            }

            if (user != null && pass != null) {
                UserDTO ac = udao.getUserById(user);
                if (ac != null && ac.getPassword().equals(PasswordUtils.encryptSHA25(pass))) {
                    if (ac.isIsActive()) {
                        session.setAttribute("errorsi", null);
                        session.setAttribute("isAdmin", ac.getRole() == 0);
                        session.setAttribute("accID", user);
                        session.setAttribute("acc", ac);
                        session.setAttribute("user", ac);

                        if (ac.getRole() == 0) {
                            return "redirect:adminPage/adminHomePage.jsp";
                        } else {
                            return "redirect:MainController?action=homeBook";
                        }
                    } else {
                        session.setAttribute("errorsi", "Tài Khoản đã bị vô hiệu hóa");
                        return "signin.jsp";
                    }
                } else {
                    session.setAttribute("errorsi", "Sai tên tài khoản hoặc mật khẩu");
                    return "redirect:signin.jsp";
                }
            }
        }

        String user = request.getParameter("accID");
        String pass = request.getParameter("password");
        String remember = request.getParameter("checklogin");
        String changePass = PasswordUtils.encryptSHA25(pass);

        UserDTO ac = udao.getUserById(user);
        if (ac != null && ac.getPassword().equals(changePass)) {
            if (ac.isIsActive()) {
                session.setAttribute("errorsi", null);
                session.setAttribute("isAdmin", ac.getRole() == 0);
                session.setAttribute("accID", user);
                session.setAttribute("acc", ac);
                session.setAttribute("user", ac);

                if (remember != null && remember.equals("rem")) {
                    Cookie userCookie = new Cookie("user", user);
                    Cookie passCookie = new Cookie("pass", pass);

                    userCookie.setMaxAge(60 * 60 * 24 * 30);//cookie này tồn tại trong 30 ngày
                    passCookie.setMaxAge(60 * 60 * 24 * 30);//cookie này tồn tại trong 30 ngày

                    response.addCookie(userCookie);
                    response.addCookie(passCookie);
                } else {
                    deleteRememberMeCookies(request, response);
                }

                String url = ac.getRole() == 0 ? "redirect:adminPage/adminHomePage.jsp" : "redirect:MainController?action=homeBook";
                return url;
            } else {
                session.setAttribute("errorsi", "Tài Khoản đã bị vô hiệu hóa");
                return "redirect:signin.jsp";
            }
        } else {
            session.setAttribute("errorsi", "Sai tên tài khoản hoặc mật khẩu");
            return "redirect:signin.jsp";
        }
    }

    private String handleSignup(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserDAO udao = new UserDAO();
        String accID = request.getParameter("accID").trim();
        String pass = request.getParameter("password").trim();
        String name = request.getParameter("name").trim();
        String email = request.getParameter("email").trim();
        String phone = request.getParameter("phone").trim();

        boolean hasError = false;

        // Kiểm tra accID không rỗng
        if (accID.isEmpty()) {
            session.setAttribute("error_accID", "Tên tài khoản không được để trống!");
            hasError = true;
        } else if (!accID.matches("^[a-zA-Z0-9_]{4,}$")) {//không chứa kí tự đặt biệt và phải có ít nhất 4 ký tự
            session.setAttribute("error_accID", "Tên tài khoản phải có ít nhất 4 ký tự và không chứa ký tự đặc biệt!");
            hasError = true;
        } else if (udao.isUsernameExist(accID)) {
            session.setAttribute("error_accID", "Tên tài khoản đã tồn tại!");
            hasError = true;
        } else {
            session.setAttribute("error_accID", null);
        }

        // Kiểm tra mật khẩu
        if (pass.isEmpty()) {
            session.setAttribute("error_password", "Mật khẩu không được để trống!");
            hasError = true;
        } else if (pass.length() < 6) {
            session.setAttribute("error_password", "Mật khẩu phải có ít nhất 6 ký tự!");
            hasError = true;
        } else {
            session.setAttribute("error_password", null);
        }

        // Kiểm tra họ tên
        if (name.isEmpty()) {
            session.setAttribute("error_name", "Họ tên không được để trống!");
            hasError = true;
        } else if (!name.matches("^[\\p{L} .'-]+$")) {
            session.setAttribute("error_name", "Họ tên chỉ được chứa chữ cái!");
            hasError = true;
        } else {
            session.setAttribute("error_name", null);
        }

        // Kiểm tra email
        if (email.isEmpty()) {
            session.setAttribute("error_email", "Email không được để trống!");
            hasError = true;
        } else if (!email.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")) {
            session.setAttribute("error_email", "Email không hợp lệ!");
            hasError = true;
        } else if (udao.isEmailExist(email)) {
            session.setAttribute("error_email", "Email đã được sử dụng!");
            hasError = true;
        } else {
            session.setAttribute("error_email", null);
        }

        // Kiểm tra số điện thoại
        if (phone.isEmpty()) {
            session.setAttribute("error_phone", "Số điện thoại không được để trống!");
            hasError = true;
        } else if (!phone.matches("^\\d{10}$")) {
            session.setAttribute("error_phone", "Số điện thoại phải gồm đúng 10 chữ số!");
            hasError = true;
        } else if (udao.isPhoneExist(phone)) {
            session.setAttribute("error_phone", "Số điện thoại đã được sử dụng!");
            hasError = true;
        } else {
            session.setAttribute("error_phone", null);
        }

        if (hasError) {
            session.setAttribute("old_accID", accID);
            session.setAttribute("old_name", name);
            session.setAttribute("old_email", email);
            session.setAttribute("old_phone", phone);
            return "redirect:signup.jsp";
        }

        if (udao.isUserExist(accID, email, phone)) {
            session.setAttribute("errorsu", "Tài khoản, email hoặc số điện thoại đã tồn tại!");
            return "redirect:signup.jsp";
        }
        String changePassword = PasswordUtils.encryptSHA25(pass);
        UserDTO ac = new UserDTO(accID, email, changePassword, name, 1, phone, true);
        udao.addUser(ac);
        session.setAttribute("errorsu", null);
        session.setAttribute("success", "Đăng ký thành công! Hãy đăng nhập.");

        String subject = "Xác Nhận Đăng Ký Tài Khoản - BOOKLY";

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
                + "    <p>Cảm ơn bạn đã đăng ký tài khoản tại hệ thống bán sách của chúng tôi.</p>"
                + "    <p>Thông tin đăng ký:</p>"
                + "    <ul>"
                + "      <li><strong>Tài khoản:</strong> " + accID + "</li>"
                + "      <li><strong>Họ tên:</strong> " + name + "</li>"
                + "      <li><strong>Email:</strong> " + email + "</li>"
                + "      <li><strong>Số điện thoại:</strong> " + phone + "</li>"
                + "    </ul>"
                + "    <p>Trân trọng,<br/>Đội ngũ BOOKLY</p>"
                + "  </div>"
                + "</body>"
                + "</html>";

        Email.sendEmail(email, subject, content);
        return "redirect:signin.jsp";
    }

    private String handleForgotPass(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserDAO udao = new UserDAO();
        String userId = request.getParameter("accID");
        String newPass = request.getParameter("password");
        String newPass2 = request.getParameter("password2");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        UserDTO ac = udao.getUserById(userId);
        if (ac != null) {
            if (ac.getEmail().equals(email)
                    && ac.getPhone().equals(phone)) {
                if (newPass.equals(newPass2) && newPass.length() >= 6) {
                    boolean isUpdated = udao.changePassword(userId, PasswordUtils.encryptSHA25(newPass));
                    if (isUpdated) {
                        session.setAttribute("errorfp", null);
                        session.setAttribute("success", "Đổi mật khẩu thành công!");
                        String subject = "Xác Nhận Đổi Mật Khẩu Tài Khoản Thành Công - BOOKLY";

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
                                + "    <p>Bạn đã đổi mật khẩu tại hệ thống bán sách của chúng tôi.</p>"
                                + "    <p>Thông tin Tài Khoản và Mật Khẩu mới:</p>"
                                + "    <ul>"
                                + "      <li><strong>Tài khoản:</strong> " + ac.getAccID() + "</li>"
                                + "      <li><strong>Họ tên:</strong> " + ac.getName() + "</li>"
                                + "      <li><strong>Email:</strong> " + ac.getEmail() + "</li>"
                                + "      <li><strong>Số Điện Thoại:</strong> " + ac.getPhone() + "</li>"
                                + "      <li><strong>Mật Khẩu Mới:</strong> " + newPass + "</li>"
                                + "    </ul>"
                                + "    <p>Trân trọng,<br/>Đội ngũ BOOKLY</p>"
                                + "  </div>"
                                + "</body>"
                                + "</html>";
                        Email.sendEmail(ac.getEmail(), subject, content);
                        return "redirect:signin.jsp";
                    } else {
                        session.setAttribute("errorfp", "Lỗi khi cập nhật mật khẩu.");
                        return "redirect:forgotpass.jsp";
                    }
                } else {
                    session.setAttribute("errorfp", "Mật khẩu nhập lại không khớp hoặc Mật Khẩu phải chứa 6 ký tự trở lên");
                    return "redirect:forgotpass.jsp";
                }
            } else {
                session.setAttribute("errorfp", "Email hoặc số điện thoại không khớp.");
                return "redirect:forgotpass.jsp";
            }
        } else {
            session.setAttribute("errorfp", "Tài khoản không tồn tại.");
            return "redirect:forgotpass.jsp";
        }
    }

    private String handleupdateUser(HttpServletRequest request, HttpServletResponse response) {
        String accID = request.getParameter("accID");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        UserDAO udao = new UserDAO();
        UserDTO oldAccount = udao.getUserById(accID);

        if (udao.isUserExist(accID, email, phone)
                && (!email.equals(oldAccount.getEmail()) || !phone.equals(oldAccount.getPhone()))) {
            request.setAttribute("errorMessage", "Email hoặc số điện thoại đã tồn tại.");
            request.setAttribute("account", oldAccount);
            return "edituser.jsp";
        }

        UserDTO updatedAccount = new UserDTO(accID, email, oldAccount.getPassword(), name, oldAccount.getRole(), phone, oldAccount.isIsActive());
        boolean success = udao.updateUser(updatedAccount);

        if (success) {
            request.setAttribute("updateMessage", "Cập nhật thông tin thành công!");
            request.setAttribute("account", updatedAccount);
            return "edituser.jsp";
        } else {
            request.setAttribute("errorMessage", "Cập nhật thông tin thất bại. Vui lòng thử lại.");
            request.setAttribute("account", oldAccount);
            return "edituser.jsp";
        }
    }

    private String handleSignout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return "redirect:MainController?action=homeBook";
    }
}
