package com.fengshui.controller.admin;

import com.fengshui.entity.Order;
import com.fengshui.entity.OrderItem;
import com.fengshui.service.IOrderService;
import com.fengshui.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/orders")
public class OrderAdminController extends HttpServlet {
    private final IOrderService orderService = new OrderService();

    // Định nghĩa các hằng số hành động nhằm tránh lỗi gõ sai chính tả (Anti-typo)
    private static final String ACTION_LIST = "list";
    private static final String ACTION_DETAILS = "details";
    private static final String ACTION_UPDATE_STATUS = "updateStatus";
    private static final String ACTION_DELETE = "delete";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = ACTION_LIST;
        }

        switch (action) {
            case ACTION_LIST:
                handleList(request, response);
                break;
            case ACTION_DETAILS:
                handleDetails(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/admin/orders?action=" + ACTION_LIST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/admin/orders?action=" + ACTION_LIST);
            return;
        }

        switch (action) {
            case ACTION_UPDATE_STATUS:
                handleUpdateStatus(request, response);
                break;
            case ACTION_DELETE:
                handleDelete(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/admin/orders?action=" + ACTION_LIST);
        }
    }

    // --- Các hàm xử lý logic chi tiết (Helper Methods) ---

    /**
     * Hiển thị danh sách toàn bộ các đơn hàng hiện có
     */
    private void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Order> orders = orderService.findAll();
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/WEB-INF/views/admin/order_list.jsp").forward(request, response);
    }

    /**
     * Xem thông tin chi tiết của một đơn hàng cụ thể kèm theo danh sách sản phẩm đã đặt
     */
    private void handleDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/admin/orders?action=" + ACTION_LIST);
                return;
            }

            int orderId = Integer.parseInt(idParam);
            Order order = orderService.findByID(orderId);

            if (order == null) {
                response.sendRedirect(request.getContextPath() + "/admin/orders?action=" + ACTION_LIST);
                return;
            }

            // Sử dụng chính phương thức của OrderService như bạn đã chỉ ra
            List<OrderItem> items = orderService.findItemsByOrderID(orderId);

            request.setAttribute("order", order);
            request.setAttribute("items", items);
            request.getRequestDispatcher("/WEB-INF/views/admin/order_list.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/orders?action=" + ACTION_LIST);
        }
    }

    /**
     * Cập nhật trạng thái đơn hàng (ví dụ: Chờ duyệt, Đã duyệt, Đã giao...)
     */
    private void handleUpdateStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

            String idParam = request.getParameter("id");
            String status = request.getParameter("status");

            if (idParam == null || idParam.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/admin/orders?action=" + ACTION_LIST);
                return;
            }
        try {
            int id = Integer.parseInt(idParam);
            orderService.updateStatus(id, status);
            response.sendRedirect(request.getContextPath() + "/admin/orders?action=details&id=" + id);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/orders?action=" + ACTION_LIST);
        } catch (RuntimeException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("orderId", idParam);

            request.getRequestDispatcher("/WEB-INF/views/admin/order_list.jsp").forward(request, response);
        }
    }

    /**
     * Xử lý xóa đơn hàng
     */
    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/admin/orders?action=" + ACTION_LIST);
                return;
            }

            int id = Integer.parseInt(idParam);
            // Cần bổ sung phương thức delete() trong interface và Service nếu bạn muốn dùng
            orderService.delete(id);
            response.sendRedirect(request.getContextPath() + "/admin/orders?action=" + ACTION_LIST);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/orders?action=" + ACTION_LIST);
        }
    }
}