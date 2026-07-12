package com.fengshui.service;

import com.fengshui.entity.CartItem;
import com.fengshui.entity.Order;
import com.fengshui.entity.OrderItem;
import com.fengshui.entity.Product;
import com.fengshui.enums.OrderStatus;
import com.fengshui.repository.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderService implements IOrderService {
    private final IOrderRepository orderRepository;
    private final IOrderItemRepository orderItemRepository;
    private final IProductRepository productRepository;

    public OrderService() {
        this.orderRepository = new OrderRepository();
        this.orderItemRepository = new OrderItemRepository();
        this.productRepository = new ProductRepository();
    }

    public OrderService(IOrderRepository orderRepo, IOrderItemRepository orderItemRepo, IProductRepository prodRepo) {
        this.orderRepository = orderRepo;
        this.orderItemRepository = orderItemRepo;
        this.productRepository = prodRepo;
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
    public boolean placeOrder(Order order, List<OrderItem> items) {
        Connection connection = null;
        try {
            connection = ((BaseRepository) orderRepository).getConnection();
            connection.setAutoCommit(false);

            // 1. Lưu Order
            order.setStatus(OrderStatus.PENDING.name());
            if (!orderRepository.save(connection, order)) {
                connection.rollback();
                return false;
            }

            // 2. Lưu OrderItems và kiểm tra/trừ số lượng sản phẩm
            for (OrderItem item : items) {
                item.setOrderId(order.getId());

                Product product = productRepository.findByID(item.getProductId());
                if (product == null) {
                    throw new RuntimeException("Sản phẩm #" + item.getProductId() + " không tồn tại.");
                }
                if (product.getQuantity() < item.getQuantity()) {
                    connection.rollback(); // Rollback nếu không đủ hàng ngay cả khi chưa trừ kho
                    throw new RuntimeException("Không đủ số lượng sản phẩm " + product.getName() + " trong kho. Yêu cầu: " + item.getQuantity() + ", Hiện có: " + product.getQuantity());
                }

                if (!orderItemRepository.save(connection, item)) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi SQL khi đặt hàng: " + e.getMessage());
        } catch (RuntimeException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw e; // Ném lại RuntimeException để Controller bắt và hiển thị lỗi
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean placeOrderFromCart(Order order, List<CartItem> items) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : items) {
            OrderItem item = new OrderItem();
            item.setProductId(cartItem.getProduct().getId());
            item.setQuantity(cartItem.getQuantity());
            item.setPriceAtPurchase(cartItem.getProduct().getPrice());
            orderItems.add(item);
        }
        return this.placeOrder(order, orderItems);
    }

    @Override
    public boolean updateStatus(int orderId, String newStatus) {
        Connection connection = null;
        try {
            connection = ((BaseRepository) orderRepository).getConnection();
            connection.setAutoCommit(false);

            Order oldOrder = orderRepository.findByID(orderId);
            if (oldOrder == null) {
                throw new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId);
            }
            String oldStatusStr = oldOrder.getStatus();

            // Chuyển đổi trạng thái sang Enum để dễ so sánh
            OrderStatus oldStatus = OrderStatus.fromString(oldStatusStr);
            OrderStatus newStatusEnum = OrderStatus.fromString(newStatus);

            // Nếu trạng thái mới giống trạng thái cũ, không làm gì cả
            if (oldStatus == newStatusEnum) {
                connection.commit();
                return true;
            }

            // 1. Cập nhật trạng thái đơn hàng
            if (!orderRepository.updateStatus(connection, orderId, newStatus)) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi SQL khi cập nhật trạng thái đơn hàng: " + e.getMessage());
        } catch (RuntimeException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw e; // Ném lại RuntimeException để Controller bắt và hiển thị lỗi
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<OrderItem> findItemsByOrderID(int orderId) {
        return orderItemRepository.findByOrderID(orderId);
    }

    @Override
    public boolean delete(int id) {
        return orderRepository.delete(id);
    }

    @Override
    public double getTotalRevenue() {
        return orderRepository.getTotalRevenue();
    }

    @Override
    public int countNewOrders() {
        return orderRepository.countOrdersByStatus(OrderStatus.PENDING.name()); // Sử dụng enum
    }

    @Override
    public Map<String, Double> getMonthlyRevenue() {
        return orderRepository.getMonthlyRevenue();
    }
}
