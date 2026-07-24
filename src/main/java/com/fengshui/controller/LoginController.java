package com.fengshui.controller;

import com.fengshui.entity.User;
import com.fengshui.service.IUserService;
import com.fengshui.service.UserService;
import com.fengshui.controller.filter.RoleBasedSecurityFilter;
import com.fengshui.enums.UserRole;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    private IUserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService(); // Khởi tạo UserService
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // Kiểm tra xem có bị SecurityFilter "đá" văng ra không
        String error = request.getParameter("error");
        if (RoleBasedSecurityFilter.ERR_UNAUTHORIZED.equals(error)) {
            request.setAttribute("errorMessage", "Cảnh báo: Bạn chưa đăng nhập!");
        } else if ("forbidden".equals(error)) {
            request.setAttribute("errorMessage", "Cảnh báo: Bạn không có quyền truy cập khu vực này!");
        }

        // Hiển thị trang đăng nhập
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Bước 4: Kiểm tra phòng thủ (Defensive)
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Tên đăng nhập và mật khẩu không được để trống.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        // Bước 3: Tiếp tân (Controller) - Gọi hàm Service
        User user = userService.login(username, password);

        if (user != null) {
            System.out.println("=== LOGIN SUCCESS ===");
            System.out.println("User: " + user.getUsername());
            System.out.println("Role: " + user.getRole());
            
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);
            System.out.println("Session ID: " + session.getId());
            System.out.println("CurrentUser in session: " + session.getAttribute("currentUser"));

            // Redirect all authenticated users to the common admin dashboard
            String redirectUrl = request.getContextPath() + "/admin/dashboard";
            System.out.println("Redirecting to: " + redirectUrl);
            response.sendRedirect(redirectUrl);
        } else {
            System.out.println("=== LOGIN FAILED ===");
            System.out.println("User not found or password mismatch");
            request.setAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
