package com.fengshui.repository;

import com.fengshui.entity.OrderItem;
import java.sql.Connection;
import java.util.List;

public interface IOrderItemRepository {
    List<OrderItem> findByOrderID(int orderId);
    boolean save(OrderItem orderItem);
    
    // Phương thức transactional dùng chung Connection
    boolean save(Connection connection, OrderItem orderItem);
}
