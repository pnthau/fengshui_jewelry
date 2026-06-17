package com.fengshui.controller.user;

import com.fengshui.entity.Order;
import com.fengshui.entity.OrderItem;
import com.fengshui.entity.Product;
import com.fengshui.service.IOrderService;
import com.fengshui.service.OrderService;
import com.fengshui.service.IProductService;
import com.fengshui.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;

@WebServlet(name = "QuickOrderController", value = "/quick-order")
public class QuickOrderController extends HttpServlet {
    private final IOrderService orderService = new OrderService();
    private final IProductService productService = new ProductService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String productIdStr = request.getParameter("productId");
        String customerName = request.getParameter("customerName");
        String customerPhone = request.getParameter("customerPhone");
        String customerAddress = request.getParameter("customerAddress");
        String quantityStr = request.getParameter("quantity");

        // Simple validation
        if (productIdStr == null || customerName == null || customerPhone == null || customerAddress == null) {
            sendError(response, "Dữ liệu gửi lên không đầy đủ.");
            return;
        }

        customerName = customerName.trim();
        customerPhone = customerPhone.trim();
        customerAddress = customerAddress.trim();

        if (customerName.isEmpty() || customerPhone.isEmpty() || customerAddress.isEmpty()) {
            sendError(response, "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        int productId;
        int quantity = 1;
        try {
            productId = Integer.parseInt(productIdStr);
            if (quantityStr != null && !quantityStr.isEmpty()) {
                quantity = Integer.parseInt(quantityStr);
            }
        } catch (NumberFormatException e) {
            sendError(response, "Dữ liệu đầu vào không đúng định dạng.");
            return;
        }

        if (quantity <= 0) {
            sendError(response, "Số lượng đặt hàng phải lớn hơn 0.");
            return;
        }

        // Fetch product to verify and get current price
        Product product = productService.findByID(productId);
        if (product == null) {
            sendError(response, "Sản phẩm không tồn tại trên hệ thống.");
            return;
        }

        // Check if there is enough inventory
        if (product.getQuantity() < quantity) {
            sendError(response, "Sản phẩm \"" + product.getName() + "\" hiện tại chỉ còn " + product.getQuantity() + " sản phẩm trong kho.");
            return;
        }

        // Calculate total price
        BigDecimal priceAtPurchase = product.getPrice();
        BigDecimal totalPrice = priceAtPurchase.multiply(new BigDecimal(quantity));

        // Create Order
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setCustomerPhone(customerPhone);
        order.setCustomerAddress(customerAddress);
        order.setTotalPrice(totalPrice);
        order.setStatus("PENDING"); // Pending approval

        // Create OrderItem
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(productId);
        orderItem.setQuantity(quantity);
        orderItem.setPriceAtPurchase(priceAtPurchase);

        // Place order (transacted)
        boolean isSuccess = orderService.placeOrder(order, Collections.singletonList(orderItem));

        if (isSuccess) {
            response.getWriter().write("{\"status\": \"success\"}");
        } else {
            sendError(response, "Đã xảy ra lỗi hệ thống khi đặt hàng. Vui lòng liên hệ Hotline.");
        }
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        String escapedMessage = escapeJson(message);
        response.getWriter().write("{\"status\": \"error\", \"message\": \"" + escapedMessage + "\"}");
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\b", "\\b")
                    .replace("\f", "\\f")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}
