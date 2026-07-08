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
        // this.transactionRepository = new InventoryTransactionRepository(); // Tạm để nguyên
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
            boolean isOldActive = OrderStatus.CANCELLED.name().equals(oldStatus);
            boolean isNewActive = OrderStatus.CANCELLED.name().equals(newStatus);

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
    public boolean placeOrder(Order order, List<OrderItem> items) {
        boolean isSuccess = false;
        try (Connection connection = ((BaseRepository) orderRepository).getConnection()) {
            connection.setAutoCommit(false);
            try {
                boolean orderSaved = orderRepository.save(connection, order);
                if (!orderSaved) {
                    throw new SQLException("Save() Order failure");
                }
                for (OrderItem item : items) {
                    item.setOrderId(order.getId());

                    Product product = productRepository.findByID(item.getProductId());
                    if (product == null || product.getQuantity() < item.getQuantity()) {
                        String productName = product != null ? product.getName() : "";

                        throw new SQLException("Số lượng mua vượt quá số lượng bán. " +
                                "Kho hiện tại chỉ còn: " +
                                (product != null ? product.getQuantity() : 0));
                    }

                    boolean itemSaved = orderItemRepository.save(connection, item);
                    if (!itemSaved) {
                        throw new SQLException("Save() OrderItem failure");
                    }
                }
                connection.commit();
                isSuccess = true;
                System.out.println("✅ Order succeed! COMMIT");
            } catch (SQLException customerEx) {
                connection.rollback();
                System.out.println("❌ Order failure! ROLLBACK " + customerEx.getMessage());
                throw new RuntimeException(customerEx.getMessage());
            }
        } catch (SQLException serverEx) {
            throw new RuntimeException("Lỗi kết nối Cơ sở dữ liệu: " + serverEx.getMessage());
        }
        return isSuccess;
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