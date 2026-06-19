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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "QuickOrderController", value = "/quick-order")
public class QuickOrderController extends HttpServlet {
    private final IOrderService orderService = new OrderService();
    private final IProductService productService = new ProductService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");


        String productIdStr = req.getParameter("productId");
        int productId = Integer.parseInt(productIdStr);

        Product product = productService.findByID(productId);
        if (product == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "not found product");
            return;
        }

        String quantityStr = req.getParameter("quantity");
        int quantity = Integer.parseInt(quantityStr);
        BigDecimal currPrice = product.getPrice();
        BigDecimal totalPrice = currPrice.multiply(BigDecimal.valueOf(quantity));

        String customerName = req.getParameter("customerName");
        String customerPhone = req.getParameter("customerPhone");
        String customerAddress = req.getParameter("customerAddress");

        Order order = new Order();
        order.setCustomerName(customerName);
        order.setCustomerPhone(customerPhone);
        order.setCustomerAddress(customerAddress);
        order.setTotalPrice(totalPrice);
        order.setStatus("PENDING");

        OrderItem item = new OrderItem();
        item.setQuantity(quantity);
        item.setProductId(productId);
        item.setPriceAtPurchase(currPrice);

        List<OrderItem> itemList = new ArrayList<>();
        itemList.add(item);
        orderService.placeOrder(order, itemList);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
