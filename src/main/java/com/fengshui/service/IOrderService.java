package com.fengshui.service;

import com.fengshui.entity.CartItem;
import com.fengshui.entity.Order;
import com.fengshui.entity.OrderItem;

import java.util.List;
import java.util.Map;

public interface IOrderService {
    List<Order> findAll();

    Order findByID(int id);

    boolean placeOrder(Order order, List<OrderItem> items);

    boolean placeOrderFromCart(Order order, List<CartItem> items);

    boolean updateStatus(int id, String status);

    List<OrderItem> findItemsByOrderID(int orderId);

    boolean delete(int id);

    // New methods for Dashboard (re-added)
    double getTotalRevenue();
    int countNewOrders();
    Map<String, Double> getMonthlyRevenue();
}