package com.fengshui.controller.admin;

import com.fengshui.DTO.DashboardDTO;
import com.fengshui.service.DashboardService;
import com.fengshui.service.IDashboardService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/admin/dashboard")
public class DashboardController extends HttpServlet {

    private IDashboardService dashboardService;

    @Override
    public void init() throws ServletException {
        super.init();
        dashboardService = new DashboardService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DashboardDTO dashboardData = dashboardService.getDashboardData();
        request.setAttribute("dashboardData", dashboardData);

        // Chuyển đổi Map thành JSON string thủ công để truyền cho Chart.js
        String labels = dashboardData.getMonthlyRevenue().keySet().stream()
                .map(month -> "'" + month + "'")
                .collect(Collectors.joining(", "));
        String data = dashboardData.getMonthlyRevenue().values().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        request.setAttribute("chartLabels", labels);
        request.setAttribute("chartData", data);

        // Đặt tiêu đề trang
        request.setAttribute("title", "Dashboard Admin");
        // Chuyển hướng đến layout chung
        request.setAttribute("contentPage", "/WEB-INF/views/admin/dashboard.jsp");
        request.getRequestDispatcher("/WEB-INF/views/admin/admin_layout.jsp").forward(request, response);
    }
}
