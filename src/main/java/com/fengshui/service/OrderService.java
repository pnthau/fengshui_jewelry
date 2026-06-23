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
        return orderRepository.updateStatus(id, status);
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
}