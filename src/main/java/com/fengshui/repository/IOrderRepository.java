package com.fengshui.repository;

import com.fengshui.entity.Order;
import java.sql.Connection;
import java.util.List;

public interface IOrderRepository {
    List<Order> findAll();
    Order findByID(int id);
    boolean save(Order order);
    boolean updateStatus(int id, String status);
    boolean delete(int id);
    
    // Phương thức transactional dùng chung Connection
    boolean save(Connection connection, Order order);
}
