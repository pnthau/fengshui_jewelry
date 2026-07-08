package com.fengshui.repository;

import com.fengshui.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderRepositoryTest {
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        orderRepository = new OrderRepository();
    }

    @Test
    public void testFindAll_Success() {
        assertDoesNotThrow(() -> {
            List<Order> orders = orderRepository.findAll();
            assertNotNull(orders, "Danh sách đơn hàng không được null!");
            System.out.println("✅ Test findAll() Orders thành công! Số lượng hiện tại: " + orders.size());
        });
    }

    @Test
    public void testOrderCRUD_Lifecycle() {
        assertDoesNotThrow(() -> {
            // 1. Tạo đơn hàng test
            Order order = new Order();
            order.setCustomerName("Khách Hàng Test JUnit");
            order.setCustomerPhone("0999999999");
            order.setCustomerAddress("Địa chỉ Test JUnit");
            order.setTotalPrice(new BigDecimal("1500000.00"));
            order.setStatus("PENDING");

            // 2. Test INSERT (save) - lấy được ID tự tăng
            boolean isSaved = orderRepository.save(order);
            assertTrue(isSaved, "Thêm mới đơn hàng phải thành công!");
            assertTrue(order.getId() > 0, "ID của đơn hàng phải tự động cập nhật > 0!");
            System.out.println("✅ Test save() Order thành công! ID vừa sinh: " + order.getId());

            // 3. Test READ (findByID)
            Order foundOrder = orderRepository.findByID(order.getId());
            assertNotNull(foundOrder, "Tìm đơn hàng theo ID không được null!");
            assertEquals("Khách Hàng Test JUnit", foundOrder.getCustomerName(), "Tên khách hàng phải khớp!");

            // 4. Test UPDATE (updateStatus)
            boolean isStatusUpdated = orderRepository.updateStatus(order.getId(), "APPROVED");
            assertTrue(isStatusUpdated, "Cập nhật trạng thái đơn hàng phải thành công!");

            Order updatedOrder = orderRepository.findByID(order.getId());
            assertEquals("APPROVED", updatedOrder.getStatus(), "Trạng thái mới phải là APPROVED!");
            System.out.println("✅ Test updateStatus() Order thành công!");

            // 5. Test DELETE
            boolean isDeleted = orderRepository.delete(order.getId());
            assertTrue(isDeleted, "Xóa đơn hàng test phải thành công!");

            Order deletedOrder = orderRepository.findByID(order.getId());
            assertNull(deletedOrder, "Đơn hàng sau khi xóa phải trả về null!");
            System.out.println("✅ Test delete() Order thành công! Dữ liệu DB sạch sẽ.");
        });
    }
}
