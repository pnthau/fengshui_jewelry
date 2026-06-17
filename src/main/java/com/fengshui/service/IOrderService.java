package com.fengshui.service;

import com.fengshui.entity.Order;
import com.fengshui.entity.OrderItem;

import java.util.List;

public interface IOrderService {
    List<Order> findAll();
    Order findByID(int id);
    boolean placeOrder(Order order, List<OrderItem> items);
    boolean updateStatus(int id, String status);
}
