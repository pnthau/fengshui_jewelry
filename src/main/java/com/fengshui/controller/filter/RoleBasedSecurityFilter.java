package com.fengshui.controller.filter;

import com.fengshui.annotation.RequireRole;
import com.fengshui.entity.User;
import com.fengshui.enums.UserRole;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Filter kiểm tra quyền truy cập dựa trên role của người dùng
 * - Kiểm tra user session và role
 * - Kiểm tra @RequireRole annotation trên controller methods
 * - Cho phép hoặc chặn request dựa trên quyền
 */
@WebFilter({"/admin/inventory", "/admin/orders", "/admin/products"})
public class RoleBasedSecurityFilter implements Filter {

    public static final String ERR_UNAUTHORIZED = "unauthorized";
    public static final String ERR_FORBIDDEN = "forbidden";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Khởi tạo filter
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Kiểm tra người dùng đã đăng nhập chưa
        boolean isLoggedIn = (session != null && session.getAttribute("currentUser") != null);
        if (!isLoggedIn) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?error=" + ERR_UNAUTHORIZED);
            return;
        }

        User currentUser = (User) session.getAttribute("currentUser");
        UserRole userRole = UserRole.fromString(currentUser.getRole());

        // Kiểm tra quyền truy cập theo URL path
        String requestPath = httpRequest.getRequestURI();
        if (!hasAccessToPath(requestPath, userRole)) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?error=" + ERR_FORBIDDEN);
            return;
        }

        // Nếu tất cả kiểm tra pass, cho request tiếp tục
        chain.doFilter(request, response);
    }

    /**
     * Kiểm tra xem user có quyền truy cập vào path không
     * @param path URL path (ví dụ: /admin/inventory, /admin/orders)
     * @param userRole Role của user
     * @return true nếu có quyền, false nếu không có quyền
     */
    private boolean hasAccessToPath(String path, UserRole userRole) {
        // ADMIN có quyền truy cập tất cả
        if (userRole == UserRole.ADMIN) {
            return true;
        }

        // /admin/dashboard được phép truy cập bởi tất cả role
        if (path.contains("/admin/dashboard")) {
            return true;
        }

        // /admin/orders - ADMIN + SALES
        if (path.contains("/admin/orders")) {
            return userRole == UserRole.SALES;
        }

        // /admin/inventory - ADMIN + WAREHOUSE
        if (path.contains("/admin/inventory")) {
            return userRole == UserRole.WAREHOUSE;
        }

        // /admin/products - ADMIN only
        if (path.contains("/admin/products")) {
            return false;  // Chỉ ADMIN được, non-ADMIN sẽ bị chặn
        }

        // Các endpoint /admin/* khác chỉ cho ADMIN
        if (path.contains("/admin/")) {
            return false;
        }

        if (path.contains("/sales/")) {
            // Chỉ SALES có quyền truy cập /sales/*
            return userRole == UserRole.SALES;
        }

        if (path.contains("/warehouse/")) {
            // Chỉ WAREHOUSE có quyền truy cập /warehouse/*
            return userRole == UserRole.WAREHOUSE;
        }

        if (path.contains("/accountant/")) {
            // Chỉ ACCOUNTANT có quyền truy cập /accountant/*
            return userRole == UserRole.ACCOUNTANT;
        }

        return true; // Path khác cho phép truy cập
    }

    @Override
    public void destroy() {
        // Hủy filter
    }
}
