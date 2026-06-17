package com.fengshui.service;

import com.fengshui.entity.Order;
import com.fengshui.entity.OrderItem;
import com.fengshui.entity.Product;
import com.fengshui.repository.OrderRepository;
import com.fengshui.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {
    private OrderService orderService;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    private int testProductId = 0;
    private int testOrderId = 0;

    @BeforeEach
    public void setUp() {
        orderService = new OrderService();
        productRepository = new ProductRepository();
        orderRepository = new OrderRepository();
    }

    @AfterEach
    public void tearDown() {
        cleanUpTestData(testOrderId, testProductId);
        System.out.println("🧹 Dọn dẹp dữ liệu test xong!");
    }

    @Test
    public void testPlaceOrderSuccessCommit() {
        assertDoesNotThrow(() -> {
            // 1. Tạo sản phẩm test (Tồn kho = 10)
            Product product = new Product();
            product.setName("Vòng Tay Test Transaction Commit");
            product.setPrice(new BigDecimal("300000.00"));
            product.setQuantity(10);
            product.setMaterial("Đá");
            product.setStatus("ACTIVE");
            productRepository.save(product);
            testProductId = product.getId();
            assertTrue(product.getId() > 0);

            // 2. Tạo đơn hàng và chi tiết sản phẩm mua (Số lượng = 3)
            Order order = new Order();
            order.setCustomerName("Khách Commit");
            order.setCustomerPhone("0123456789");
            order.setCustomerAddress("Địa chỉ Commit");
            order.setTotalPrice(new BigDecimal("900000.00"));
            order.setStatus("PENDING");

            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setQuantity(3);
            item.setPriceAtPurchase(product.getPrice());

            List<OrderItem> items = new ArrayList<>();
            items.add(item);

            // 3. Thực hiện đặt hàng
            boolean isPlaced = orderService.placeOrder(order, items);
            testOrderId = order.getId();
            assertTrue(isPlaced, "Đặt hàng thành công thì hàm placeOrder phải trả về true!");

            // 4. Kiểm tra xem tồn kho đã giảm từ 10 xuống 7 hay chưa
            Product updatedProduct = productRepository.findByID(product.getId());
            assertEquals(7, updatedProduct.getQuantity(), "Số tồn kho phải giảm từ 10 xuống 7!");

            // 5. Kiểm tra đơn hàng có thực sự được lưu
            Order savedOrder = orderRepository.findByID(order.getId());
            assertNotNull(savedOrder, "Đơn hàng phải tồn tại trong DB!");

            System.out.println("✅ Test Commit Transaction thành công! Đã trừ kho và lưu đơn hàng.");
        });
    }

    @Test
    public void testPlaceOrderFailureRollback() {
        assertDoesNotThrow(() -> {
            // 1. Tạo sản phẩm test (Tồn kho = 2)
            Product product = new Product();
            product.setName("Vòng Tay Test Transaction Rollback");
            product.setPrice(new BigDecimal("300000.00"));
            product.setQuantity(2);
            product.setMaterial("Đá");
            product.setStatus("ACTIVE");
            productRepository.save(product);
            assertTrue(product.getId() > 0);

            // 2. Tạo đơn hàng mua (Số lượng = 5 -> Vượt quá tồn kho!)
            Order order = new Order();
            order.setCustomerName("Khách Rollback");
            order.setCustomerPhone("0999999999");
            order.setCustomerAddress("Địa chỉ Rollback");
            order.setTotalPrice(new BigDecimal("1500000.00"));
            order.setStatus("PENDING");

            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setQuantity(5); // 5 > 2
            item.setPriceAtPurchase(product.getPrice());

            List<OrderItem> items = new ArrayList<>();
            items.add(item);

            // 3. Thực hiện đặt hàng -> Kỳ vọng THẤT BẠI
            boolean isPlaced = orderService.placeOrder(order, items);
            assertFalse(isPlaced, "Đặt hàng quá số lượng tồn kho phải trả về false!");

            // 4. Kiểm tra tồn kho của sản phẩm: Phải giữ nguyên là 2 (Được Rollback)
            Product updatedProduct = productRepository.findByID(product.getId());
            assertEquals(2, updatedProduct.getQuantity(), "Tồn kho phải được giữ nguyên là 2 vì giao dịch bị rollback!");

            // 5. Kiểm tra đơn hàng: Không được phép lưu vào database
            Order savedOrder = orderRepository.findByID(order.getId());
            assertNull(savedOrder, "Đơn hàng không được phép xuất hiện trong DB do rollback!");

            // 6. Dọn dẹp dữ liệu sản phẩm test
            cleanUpTestData(0, product.getId());
            System.out.println("✅ Test Rollback Transaction thành công! Tồn kho được giữ nguyên, đơn hàng không được lưu.");
        });
    }

    // Helper dọn dẹp dữ liệu kiểm thử
    private void cleanUpTestData(int orderId, int productId) {
        try (Connection conn = productRepository.getConnection()) {
            if (productId > 0) {
                // Xóa lịch sử kho
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM inventory_transactions WHERE product_id = ?")) {
                    ps.setInt(1, productId);
                    ps.executeUpdate();
                }
            }
            if (orderId > 0) {
                // Xóa chi tiết đơn hàng
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM order_items WHERE order_id = ?")) {
                    ps.setInt(1, orderId);
                    ps.executeUpdate();
                }
                // Xóa đơn hàng
                orderRepository.delete(orderId);
            }
            if (productId > 0) {
                // Xóa sản phẩm
                productRepository.delete(productId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
