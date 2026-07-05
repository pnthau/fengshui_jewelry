package com.fengshui.repository;

import com.fengshui.entity.Order;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface IOrderRepository {
    List<Order> findAll();
    Order findByID(int id);
    boolean save(Order order);
    boolean update(Order order);
    boolean delete(int id);
    boolean updateStatus(int orderId, String status);
    boolean updateStatus(Connection connection, int orderId, String status);
    boolean save(Connection connection, Order order);

    double getTotalRevenue();
    int countOrdersByStatus(String status);
    Map<String, Double> getMonthlyRevenue();
}