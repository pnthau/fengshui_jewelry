package com.fengshui.service;

import com.fengshui.DTO.DashboardDTO;

public class DashboardService implements IDashboardService {

    private final IOrderService orderService;
    private final IProductService productService;
    private final IUserService userService;

    public DashboardService() {
        this.orderService = new OrderService();
        this.productService = new ProductService();
        this.userService = new UserService();
    }

    @Override
    public DashboardDTO getDashboardData() {
        DashboardDTO dashboardDTO = new DashboardDTO();

        // Lấy tổng doanh thu
        dashboardDTO.setTotalRevenue(orderService.getTotalRevenue());

        // Lấy số đơn hàng mới (PENDING)
        dashboardDTO.setNewOrdersCount(orderService.countNewOrders());

        // Lấy số sản phẩm sắp hết hàng (ngưỡng 5)
        int lowStockThreshold = 5;
        dashboardDTO.setLowStockProductsCount(productService.countLowStockProducts(lowStockThreshold));

        // Lấy tổng số tài khoản khách
        dashboardDTO.setTotalUsersCount(userService.countAllUsers());

        // Lấy doanh thu theo tháng cho biểu đồ
        dashboardDTO.setMonthlyRevenue(orderService.getMonthlyRevenue());

        return dashboardDTO;
    }
}
