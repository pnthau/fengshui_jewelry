package com.fengshui.service;

import com.fengshui.entity.CartItem;
import com.fengshui.entity.Order;
import com.fengshui.entity.OrderItem;
import com.fengshui.repository.BaseRepository;
import com.fengshui.repository.IOrderItemRepository;
import com.fengshui.repository.IOrderRepository;
import com.fengshui.repository.IProductRepository;
import com.fengshui.repository.OrderItemRepository;
import com.fengshui.repository.OrderRepository;
import com.fengshui.repository.ProductRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OrderService implements IOrderService {
    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;
    private final IOrderItemRepository orderItemRepository;

    // Constructor mặc định (dùng cho môi trường chạy thật)
    public OrderService() {
        this.orderRepository = new OrderRepository();
        this.productRepository = new ProductRepository();
        this.orderItemRepository = new OrderItemRepository();
    }

    // Constructor để inject các dependency (dùng cho Unit Test)
    public OrderService(IOrderRepository orderRepository, IOrderItemRepository orderItemRepository, IProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
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

            if (!orderRepository.save(connection, order)) {
                connection.rollback();
                return false;
            }

            for (OrderItem item : items) {
                item.setOrderId(order.getId());
                if (!orderItemRepository.save(connection, item)) {
                    connection.rollback();
                    return false;
                }
                if (!productRepository.reduceStock(connection, item.getProductId(), item.getQuantity())) {
                    connection.rollback();
                    throw new RuntimeException("Không đủ số lượng sản phẩm " + item.getProductId() + " trong kho.");
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
            return false;
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
        Connection connection = null;
        try {
            connection = ((BaseRepository) orderRepository).getConnection();
            connection.setAutoCommit(false);

            if (!orderRepository.save(connection, order)) {
                connection.rollback();
                return false;
            }

            for (CartItem cartItem : items) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getId());
                orderItem.setProductId(cartItem.getProduct().getId());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPriceAtPurchase(cartItem.getProduct().getPrice());

                if (!orderItemRepository.save(connection, orderItem)) {
                    connection.rollback();
                    return false;
                }
                if (!productRepository.reduceStock(connection, orderItem.getProductId(), orderItem.getQuantity())) {
                    connection.rollback();
                    throw new RuntimeException("Không đủ số lượng sản phẩm " + orderItem.getProductId() + " trong kho.");
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
            return false;
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

            // 1. Cập nhật trạng thái đơn hàng
            if (!orderRepository.updateStatus(connection, orderId, newStatus)) {
                connection.rollback();
                return false;
            }

            // 2. Xử lý tồn kho dựa trên sự thay đổi trạng thái
            List<OrderItem> orderItems = orderItemRepository.findByOrderID(orderId);

            // Quy định: "CANCELLED" là trạng thái DUY NHẤT mà hàng hóa được trả lại kho.
            // Các trạng thái còn lại (PENDING, SHIPPING, SUCCESS) đều là trạng thái "Active" (Đã bị trừ kho lúc đặt hàng).
            boolean isOldActive = !"CANCELLED".equals(oldStatus);
            boolean isNewActive = !"CANCELLED".equals(newStatus);

            if (isOldActive && !isNewActive) {
                // Trường hợp 1: HỦY ĐƠN HÀNG -> Hoàn trả lại kho
                for (OrderItem item : orderItems) {
                    if (!productRepository.increaseStock(connection, item.getProductId(), item.getQuantity())) {
                        connection.rollback();
                        throw new RuntimeException("Lỗi khi hoàn tác kho: " + item.getProductName() + " cho đơn hàng bị hủy.");
                    }
                }
            } else if (!isOldActive && isNewActive) {
                // Trường hợp 2: KHÔI PHỤC ĐƠN HÀNG (Từ Hủy -> Chờ xử lý) -> Phải trừ kho lại
                for (OrderItem item : orderItems) {
                    if (!productRepository.reduceStock(connection, item.getProductId(), item.getQuantity())) {
                        connection.rollback();
                        throw new RuntimeException("Không đủ " + item.getProductName() + " trong kho để phục hồi đơn hàng.");
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
        return orderRepository.countOrdersByStatus("PENDING");
    }

    @Override
    public Map<String, Double> getMonthlyRevenue() {
        return orderRepository.getMonthlyRevenue();
    }
}
