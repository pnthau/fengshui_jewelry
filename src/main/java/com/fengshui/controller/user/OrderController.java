package com.fengshui.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fengshui.entity.*;
import com.fengshui.service.OrderService;
import com.fengshui.websocket.OrderNotificationEndpoint;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderController", value = "/order")
public class OrderController extends HttpServlet {
    private final OrderService orderService = new OrderService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String name = req.getParameter("customerName");
        String phone = req.getParameter("customerPhone");
        String address = req.getParameter("customerAddress");

        HttpSession session = req.getSession();

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Giỏ hàng đang trống");
            return;
        }
        List<CartItem> items = cart.getItems().values().stream().toList();


        Order order = new Order();
        order.setCustomerName(name);
        order.setCustomerPhone(phone);
        order.setCustomerAddress(address);
        order.setStatus("PENDING");
        order.setTotalPrice(cart.getTotalCartPrice());

        try {
            boolean isSuccess = orderService.placeOrderFromCart(order, items);

            // --- THÊM MỚI: Bắn thông báo realtime qua WebSocket ---
            // JSON by jackson
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode notifData = mapper.createObjectNode();

            notifData.put("orderId", order.getId());
            notifData.put("customerName", order.getCustomerName());
            notifData.put("customerPhone", order.getCustomerPhone() != null ? order.getCustomerPhone() : "");
            notifData.put("customerAddress", order.getCustomerAddress() != null ? order.getCustomerAddress() : "");
            notifData.put("totalPrice", order.getTotalPrice().toString());
            notifData.put("status", order.getStatus());

            //send to sale.
            String notifJson = notifData.toString();
            OrderNotificationEndpoint.broadcast(notifJson);

            session.removeAttribute("cart");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"orderId\":" + order.getId() + ",\"totalPrice\":" + order.getTotalPrice() + "}");
        } catch (RuntimeException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(ex.getMessage());
        }
    }
}
