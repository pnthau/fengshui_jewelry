package com.fengshui.service;

import com.fengshui.entity.Order;
import com.fengshui.entity.OrderItem;
import com.fengshui.repository.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderService implements IOrderService {
    private final IOrderRepository orderRepository;
    private final IOrderItemRepository orderItemRepository;
    private final IProductRepository productRepository;

    public OrderService() {
        this.orderRepository = new OrderRepository();
        this.orderItemRepository = new OrderItemRepository();
        this.productRepository = new ProductRepository();
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findByID(int id) {
        return orderRepository.findByID(id);
    }

    @Override
    public boolean updateStatus(int id, String status) {
        return orderRepository.updateStatus(id, status);
    }

    @Override
    public boolean placeOrder(Order order, List<OrderItem> items) {
        boolean isSuccess = false;
        try (Connection connection = ((BaseRepository) orderRepository).getConnection()) {
            connection.setAutoCommit(false);
            try {
                // 1. save order
                boolean orderSaved = orderRepository.save(connection, order);
                if (!orderSaved) {
                    throw new SQLException("Save() Order failure");
                }
                for (OrderItem item : items) {
                    item.setOrderId(order.getId());
                    //2. save item
                    boolean itemSaved = orderItemRepository.save(connection, item);
                    if (!itemSaved) {
                        throw new SQLException("Save() OrderItem failure");
                    }
                }
                connection.commit();
                isSuccess = true;
                System.out.println("✅ Order succeed! COMMIT");
            } catch (SQLException innerEx) {
                connection.rollback();
                System.out.println("❌ Order failure! ROLLBACK " + innerEx.getMessage());
            }
        } catch (SQLException outerEx) {
            outerEx.printStackTrace();
        }
        return isSuccess;
    }
}
