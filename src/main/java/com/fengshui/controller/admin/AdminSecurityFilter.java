package com.fengshui.controller.admin;

import com.fengshui.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/admin/*")
public class AdminSecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Khởi tạo filter (nếu cần)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false); // Không tạo session mới nếu chưa có

        boolean isLoggedIn = (session != null && session.getAttribute("currentUser") != null);
        User currentUser = isLoggedIn ? (User) session.getAttribute("currentUser") : null;
        boolean isAdmin = (currentUser != null && "admin".equalsIgnoreCase(currentUser.getRole()));

        // Nếu không phải admin và đang cố gắng truy cập vào /admin/*
        if (!isAdmin) {
            // Chuyển hướng về trang đăng nhập hoặc trang lỗi
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?error=unauthorized");
        } else {
            // Nếu là admin, cho phép request tiếp tục
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // Hủy filter (nếu cần)
    }
}
