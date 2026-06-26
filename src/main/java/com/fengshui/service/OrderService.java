package com.fengshui.service;

import com.fengshui.entity.CartItem;
import com.fengshui.entity.Order;
import com.fengshui.entity.OrderItem;
import com.fengshui.entity.Product;
import com.fengshui.repository.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public boolean updateStatus(int id, String status) {
        Order oldOrder = orderRepository.findByID(id);
        if (oldOrder == null) return false;

        String oldStatus = oldOrder.getStatus();
        if (oldStatus.equals(status)) {
            return orderRepository.updateStatus(id, status);
        }

        List<OrderItem> items = orderItemRepository.findByOrderID(id);
        boolean isSuccess = false;

        try (Connection connection = ((BaseRepository) orderRepository).getConnection()) {
            connection.setAutoCommit(false);
            try {
                // 1. Cập nhật trạng thái mới của đơn hàng
                if (!orderRepository.updateStatus(connection, id, status)) {
                    throw new SQLException("Failed to update status for order #" + id);
                }
                // TRƯỜNG HỢP 1: Đơn đang hoạt động (đã trừ kho) nay bị HỦY -> Tiến hành HOÀN KHO
                if (isDeductedStatus(oldStatus) && "Đã hủy".equals(status)) {
                    for (OrderItem item : items) {
                        boolean stockRestored = productRepository.increaseStock(connection, item.getProductId(), item.getQuantity());
                        if (!stockRestored) {
                            throw new SQLException("Failed to restore stock for product ID #" + item.getProductId());
                        }
                    }
                }
                // TRƯỜNG HỢP 2 (QUAN TRỌNG): Đơn từ trạng thái HỦY quay lại trạng thái HOẠT ĐỘNG -> Bắt buộc phải KIỂM KHO & TÁI TRỪ KHO
                else if ("Đã hủy".equals(oldStatus) && isDeductedStatus(status)) {
                    for (OrderItem item : items) {
                        // Thử trừ kho, nếu hàm trả về false tức là hàng đã hết hoặc không đủ cung cấp
                        boolean stockReduced = productRepository.reduceStock(connection, item.getProductId(), item.getQuantity());
                        if (!stockReduced) {
                            Product p = productRepository.findByID(item.getProductId());
                            String productName = (p != null) ? p.getName() : "#" + item.getProductId();
                            int currentStock = (p != null) ? p.getQuantity() : 0;

                            // Ném lỗi chặn đứng hành động duyệt đơn, kích hoạt Rollback tự động
                            throw new SQLException("Product '" + productName + "' is out of stock (Available: "
                                    + currentStock + ", Required: " + item.getQuantity() + "). Cannot reactivate this cancelled order!");
                        }
                    }
                }

                connection.commit();
                isSuccess = true;
            } catch (SQLException innerEx) {
                connection.rollback();
                throw new RuntimeException(innerEx.getMessage());
            }
        } catch (SQLException outerEx) {
            outerEx.printStackTrace();
        }
        return isSuccess;
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

                        throw new SQLException("Not enough stock for product : " + productName
                                + " (Required: " + item.getQuantity() + ", Available: "
                                + (product != null ? product.getQuantity() : 0) + ")");
                    }

                    // 2. save item (Database trigger trg_after_insert_order_items sẽ tự động trừ
                    // kho)
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
                throw new RuntimeException(innerEx.getMessage());
            }
        } catch (SQLException outerEx) {
            outerEx.printStackTrace();
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
    private boolean isPendingStatus(String status) {
        return "Chờ xử lý".equals(status) || "PENDING".equalsIgnoreCase(status);
    }

    private boolean isDeliveryStatus(String status) {
        return "Đang giao".equals(status) || "Đã giao".equals(status) || "DELIVERED".equalsIgnoreCase(status) || "SHIPPED".equalsIgnoreCase(status);
    }
    private boolean isDeductedStatus(String status) {
        return isPendingStatus(status) || isDeliveryStatus(status);
    }
}