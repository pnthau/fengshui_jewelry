package com.fengshui.controller.user;

import com.fengshui.entity.*;
import com.fengshui.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
            session.removeAttribute("cart");
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (RuntimeException ex) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
}
