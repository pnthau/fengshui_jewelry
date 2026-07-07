package com.fengshui.controller.admin;

import com.fengshui.DTO.OrderItemDTO; // Import DTO mới
import com.fengshui.entity.Order;
import com.fengshui.entity.OrderItem;
import com.fengshui.entity.Product; // Import Product entity
import com.fengshui.enums.OrderStatus;
import com.fengshui.service.IOrderService;
import com.fengshui.service.IProductService; // Import ProductService interface
import com.fengshui.service.OrderService;
import com.fengshui.service.ProductService; // Import ProductService implementation
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList; // Import ArrayList
import java.util.List;

@WebServlet("/admin/orders")
public class OrderAdminController extends HttpServlet {
    private final IOrderService orderService = new OrderService();
    private final IProductService productService = new ProductService(); // Inject ProductService

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
        response.setCharacterEncoding("UTF-8"); // Đảm bảo response cũng được mã hóa UTF-8
        response.setContentType("text/html;charset=UTF-8"); // Đảm bảo response cũng được mã hóa UTF-8

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
        request.setAttribute("title", "Quản lý đơn hàng");
        request.setAttribute("contentPage", "/WEB-INF/views/admin/order_list.jsp");
        request.getRequestDispatcher("/WEB-INF/views/admin/admin_layout.jsp").forward(request, response);
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

            List<OrderItem> orderItems = orderService.findItemsByOrderID(orderId);
            List<OrderItemDTO> itemDTOs = new ArrayList<>();

            for (OrderItem item : orderItems) {
                Product product = productService.findByID(item.getProductId());
                OrderItemDTO dto = new OrderItemDTO(
                        item.getId(),
                        item.getOrderId(),
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPriceAtPurchase(),
                        item.getProductName(),
                        product != null ? product.getImageURL() : "https://via.placeholder.com/40x40?text=No+Image" // Gán imageURL
                );
                itemDTOs.add(dto);
            }

            request.setAttribute("order", order);
            request.setAttribute("items", itemDTOs); // Truyền List<OrderItemDTO> thay vì List<OrderItem>
            request.setAttribute("title", "Chi tiết đơn hàng");
            request.setAttribute("contentPage", "/WEB-INF/views/admin/order_details.jsp");
            request.getRequestDispatcher("/WEB-INF/views/admin/admin_layout.jsp").forward(request, response);
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
            OrderStatus enumStatus = OrderStatus.fromString(status);

            int id = Integer.parseInt(idParam);
            orderService.updateStatus(id, enumStatus.name());
            // Chuyển hướng về trang danh sách đơn hàng sau khi cập nhật thành công
            response.sendRedirect(request.getContextPath() + "/admin/orders?action=" + ACTION_LIST + "&success=1");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/orders?action=" + ACTION_LIST);
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();

            int id = Integer.parseInt(idParam);
            Order order = orderService.findByID(id);
            // Cần lấy lại items dưới dạng DTO để hiển thị lại form với lỗi
            List<OrderItem> orderItems = orderService.findItemsByOrderID(id);
            List<OrderItemDTO> itemDTOs = new ArrayList<>();
            IProductService productService = new ProductService(); // Tạm thời khởi tạo lại để tránh lỗi nếu chưa inject ở đây

            for (OrderItem item : orderItems) {
                Product product = productService.findByID(item.getProductId());
                OrderItemDTO dto = new OrderItemDTO(
                        item.getId(),
                        item.getOrderId(),
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPriceAtPurchase(),
                        item.getProductName(),
                        product != null ? product.getImageURL() : "https://via.placeholder.com/40x40?text=No+Image"
                );
                itemDTOs.add(dto);
            }


            request.setAttribute("order", order);
            request.setAttribute("items", itemDTOs); // Truyền List<OrderItemDTO>
            request.setAttribute("error", errorMessage);
            request.setAttribute("title", "Chi tiết đơn hàng");
            request.setAttribute("contentPage", "/WEB-INF/views/admin/order_details.jsp");
            request.getRequestDispatcher("/WEB-INF/views/admin/admin_layout.jsp").forward(request, response);
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
            orderService.delete(id);
            response.sendRedirect(request.getContextPath() + "/admin/orders?action=" + ACTION_LIST);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/orders?action=" + ACTION_LIST);
        }
    }
}