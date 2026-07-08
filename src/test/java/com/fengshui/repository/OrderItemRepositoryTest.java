package com.fengshui.repository;

import com.fengshui.entity.Order;
import com.fengshui.entity.OrderItem;
import com.fengshui.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderItemRepositoryTest {
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private OrderItemRepository orderItemRepository;

    @BeforeEach
    public void setUp() {
        orderRepository = new OrderRepository();
        productRepository = new ProductRepository();
        orderItemRepository = new OrderItemRepository();
    }

    @Test
    public void testOrderItem_SaveAndFind() {
        assertDoesNotThrow(() -> {
            // 1. Tạo sản phẩm test
            Product product = new Product();
            product.setName("Sản phẩm Test OrderItem");
            product.setPrice(new BigDecimal("500000.00"));
            product.setQuantity(20);
            product.setMaterial("Đá Thạch Anh");
            product.setImageURL("test.jpg");
            product.setStatus("ACTIVE");
            productRepository.save(product);
            assertTrue(product.getId() > 0);

            // 2. Tạo đơn hàng test
            Order order = new Order();
            order.setCustomerName("Khách Hàng Test Item");
            order.setCustomerPhone("0123456789");
            order.setCustomerAddress("Địa chỉ Test");
            order.setTotalPrice(new BigDecimal("500000.00"));
            order.setStatus("PENDING");
            orderRepository.save(order);
            assertTrue(order.getId() > 0);

            // 3. Tạo chi tiết đơn hàng test
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(product.getId());
            orderItem.setQuantity(1);
            orderItem.setPriceAtPurchase(product.getPrice());

            // 4. Test save() OrderItem
            boolean isSaved = orderItemRepository.save(orderItem);
            assertTrue(isSaved, "Thêm mới chi tiết đơn hàng phải thành công!");

            // 5. Test findByOrderID()
            List<OrderItem> items = orderItemRepository.findByOrderID(order.getId());
            assertEquals(1, items.size(), "Số lượng item của đơn hàng phải là 1!");
            assertEquals(product.getId(), items.get(0).getProductId(), "Product ID của item phải khớp!");
            System.out.println("✅ Test save() và findByOrderID() cho OrderItem thành công!");

            // 6. Dọn dẹp CSDL
            // Xóa đơn hàng (sẽ tự động CASCADE delete OrderItem do ràng buộc khoá ngoại ON DELETE CASCADE)
            orderRepository.delete(order.getId());
            // Xóa sản phẩm
            productRepository.delete(product.getId());
            System.out.println("✅ Dữ liệu test OrderItem đã được dọn sạch hoàn toàn!");
        });
    }
}
