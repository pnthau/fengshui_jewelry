package com.fengshui.controller.admin;

import com.fengshui.entity.Order;
import com.fengshui.entity.OrderItem;
import com.fengshui.service.OrderService;
import com.fengshui.repository.OrderItemRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/orders")
public class OrderAdminController extends HttpServlet {
    private final OrderService orderService = new OrderService();
    private final OrderItemRepository orderItemRepository = new OrderItemRepository();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                List<Order> orders = orderService.findAll();
                request.setAttribute("orders", orders);
                request.getRequestDispatcher("/WEB-INF/views/admin/order_list.jsp").forward(request, response);
                break;

            case "details":
                int orderId = Integer.parseInt(request.getParameter("id"));
                Order order = orderService.findByID(orderId);
                List<OrderItem> items = orderItemRepository.findByOrderID(orderId);

                request.setAttribute("order", order);
                request.setAttribute("items", items);
                // Trỏ về đúng file order_list.jsp này thay vì order_details.jsp
                request.getRequestDispatcher("/WEB-INF/views/admin/order_list.jsp").forward(request, response);
                break;

            case "updateStatus":
                int id = Integer.parseInt(request.getParameter("id"));
                String newStatus = request.getParameter("status");
                orderService.updateStatus(id, newStatus);
                response.sendRedirect(request.getContextPath() + "/admin/orders?action=list");
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/admin/orders?action=list");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("updateStatus".equals(action)) {
            // Cập nhật trạng thái đơn hàng (ví dụ: Chờ xử lý -> Đã giao)
            int id = Integer.parseInt(request.getParameter("id"));
            String status = request.getParameter("status");
            orderService.updateStatus(id, status);
            response.sendRedirect(request.getContextPath() + "/admin/orders?action=details&id=" + id);
        } else if ("delete".equals(action)) {
            // Logic xóa đơn hàng nếu cần (dựa trên phương thức đã có trong Repository)
            int id = Integer.parseInt(request.getParameter("id"));
            // orderService.delete(id); // Cần thêm vào Service nếu chưa có
            response.sendRedirect(request.getContextPath() + "/admin/orders?action=list");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/orders?action=list");
        }
    }
}