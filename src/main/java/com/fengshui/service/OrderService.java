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
            if (!orderRepository.save(connection, order)) {
                connection.rollback();
                return false;
            }

            // 2. Lưu OrderItems và kiểm tra/trừ số lượng sản phẩm
            for (OrderItem item : items) {
                item.setOrderId(order.getId());

                // Kiểm tra tồn kho trước khi lưu và trừ
                Product product = productRepository.findByID(item.getProductId());
                if (product == null) {
                    throw new RuntimeException("Sản phẩm #" + item.getProductId() + " không tồn tại.");
                }
                if (product.getQuantity() < item.getQuantity()) {
                    throw new RuntimeException("Không đủ số lượng sản phẩm " + product.getName() + " trong kho. Yêu cầu: " + item.getQuantity() + ", Hiện có: " + product.getQuantity());
                }

                if (!orderItemRepository.save(connection, item)) {
                    connection.rollback();
                    return false;
                }
                // Trừ kho
                if (!productRepository.reduceStock(connection, item.getProductId(), item.getQuantity())) {
                    connection.rollback();
                    throw new RuntimeException("Lỗi khi trừ số lượng sản phẩm " + product.getName() + " trong kho.");
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
            String oldStatus = oldOrder.getStatus();

            // Nếu trạng thái mới giống trạng thái cũ, không làm gì cả
            if (oldStatus.equals(newStatus)) {
                connection.commit();
                return true;
            }

            // 1. Cập nhật trạng thái đơn hàng
            if (!orderRepository.updateStatus(connection, orderId, newStatus)) {
                connection.rollback();
                return false;
            }

            // 2. Xử lý tồn kho dựa trên sự thay đổi trạng thái
            List<OrderItem> orderItems = orderItemRepository.findByOrderID(orderId);

            // Định nghĩa các trạng thái "đã hủy" và "đã giao"
            boolean oldStatusIsCancelled = OrderStatus.CANCELLED.name().equals(oldStatus);
            boolean newStatusIsCancelled = OrderStatus.CANCELLED.name().equals(newStatus);
            boolean oldStatusIsDelivered = OrderStatus.DELIVERED.name().equals(oldStatus);
            boolean newStatusIsDelivered = OrderStatus.DELIVERED.name().equals(newStatus);


            // Trường hợp 1: Chuyển từ trạng thái KHÔNG HỦY sang HỦY -> Hoàn kho
            if (!oldStatusIsCancelled && newStatusIsCancelled) {
                for (OrderItem item : orderItems) {
                    if (!productRepository.increaseStock(connection, item.getProductId(), item.getQuantity())) {
                        connection.rollback();
                        throw new RuntimeException("Lỗi khi hoàn kho sản phẩm " + item.getProductName() + " cho đơn hàng bị hủy.");
                    }
                }
            }
            // Trường hợp 2: Chuyển từ trạng thái HỦY sang KHÔNG HỦY -> Trừ kho lại
            else if (oldStatusIsCancelled && !newStatusIsCancelled) {
                for (OrderItem item : orderItems) {
                    Product product = productRepository.findByID(item.getProductId());
                    if (product == null || product.getQuantity() < item.getQuantity()) {
                        connection.rollback();
                        throw new RuntimeException("Không đủ số lượng sản phẩm " + item.getProductName() + " trong kho để khôi phục đơn hàng. Yêu cầu: " + item.getQuantity() + ", Hiện có: " + (product != null ? product.getQuantity() : 0));
                    }
                    if (!productRepository.reduceStock(connection, item.getProductId(), item.getQuantity())) {
                        connection.rollback();
                        throw new RuntimeException("Lỗi khi trừ kho sản phẩm " + item.getProductName() + " để khôi phục đơn hàng.");
                    }
                }
            }
            // Trường hợp 3: Chuyển từ DELIVERED sang trạng thái khác KHÔNG PHẢI CANCELLED -> Hoàn kho
            // Điều này xảy ra nếu admin muốn "đảo ngược" một đơn hàng đã thành công
            else if (oldStatusIsDelivered && !newStatusIsDelivered && !newStatusIsCancelled) {
                for (OrderItem item : orderItems) {
                    if (!productRepository.increaseStock(connection, item.getProductId(), item.getQuantity())) {
                        connection.rollback();
                        throw new RuntimeException("Lỗi khi hoàn kho sản phẩm " + item.getProductName() + " do thay đổi trạng thái đơn hàng từ DELIVERED.");
                    }
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