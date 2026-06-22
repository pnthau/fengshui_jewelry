package com.fengshui.service;

import com.fengshui.entity.CartItem;
import com.fengshui.entity.Order;
import com.fengshui.entity.Product;
import com.fengshui.repository.OrderRepository;
import com.fengshui.repository.OrderItemRepository;
import com.fengshui.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    // 1. CHUẨN BỊ DIỄN VIÊN ĐÓNG THẾ (MOCKS)
    // Phải mock class cụ thể (OrderRepository) để có thể giả lập hàm getConnection() của BaseRepository
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Connection mockConnection;

    private OrderService orderService;

    @BeforeEach
    void setUp() throws SQLException {
        // Nhét các Diễn viên đóng thế vào trong Bếp trưởng (Thông qua cửa hậu Constructor)
        orderService = new OrderService(orderRepository, orderItemRepository, productRepository);

        // Giả lập: Bất cứ khi nào Bếp trưởng đòi chìa khóa Database, thì đưa cho ổng cái Chìa khóa giả (mockConnection)
        lenient().when(orderRepository.getConnection()).thenReturn(mockConnection);
    }

    @Test
    void testPlaceOrderFromCart_OutOfStock_ThrowsException() throws SQLException {
        // --- BƯỚC 1: LÊN KỊCH BẢN (GIVEN) ---
        
        // Tạo một tờ Hóa Đơn nháp
        Order order = new Order();
        order.setId(1);
        
        // Cài cắm: Báo cho Bếp trưởng biết là việc lưu Order sẽ thành công
        when(orderRepository.save(any(Connection.class), any(Order.class))).thenReturn(true);

        // *** KỊCH BẢN CHÍNH: KHỞI TẠO KHO HÀNG ***
        Product dbProduct = new Product();
        dbProduct.setId(99);
        dbProduct.setName("Vòng Tỳ Hưu");
        dbProduct.setQuantity(2); // Kho chỉ còn đúng 2 cái!

        // Cài cắm: Khi Bếp trưởng vô kho tìm món số 99, hãy đưa ra cái dbProduct này
        when(productRepository.findByID(99)).thenReturn(dbProduct);

        // *** HÀNH ĐỘNG CỦA KHÁCH HÀNG: MUA VƯỢT QUÁ SỐ LƯỢNG ***
        Product cartProduct = new Product();
        cartProduct.setId(99);
        cartProduct.setPrice(new BigDecimal("1000"));
        
        // Khách hàng bấm mua tận 5 cái!
        CartItem item1 = new CartItem(cartProduct, 5); 
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(item1);

        // --- BƯỚC 2: THỰC THI (WHEN) ---
        // Yêu cầu máy tính xác nhận rằng: Khi chạy hàm này, Bếp trưởng CHẮC CHẮN SẼ NÉM RA RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.placeOrderFromCart(order, cartItems);
        });

        // --- BƯỚC 3: KIỂM TRA LỜI CHỬI CỦA BẾP TRƯỞNG (THEN) ---
        // Đảm bảo rằng câu chửi có chứa tên sản phẩm và số lượng tồn kho đúng như ta thiết kế
        String errorMessage = exception.getMessage();
        System.out.println("Câu báo lỗi thực tế: " + errorMessage); // In ra console để bạn xem cho đã!
        
        assertTrue(errorMessage.contains("Not enough stock for product : Vòng Tỳ Hưu"), "Câu báo lỗi phải chứa thông tin ID sản phẩm thiếu hàng");
        assertTrue(errorMessage.contains("Required: 5"), "Câu báo lỗi phải chứa số lượng yêu cầu");
        assertTrue(errorMessage.contains("Available: 2"), "Câu báo lỗi phải chứa số lượng tồn kho thực tế");

        // --- BƯỚC 4: KIỂM TRA TRANSACTION (VÔ CÙNG QUAN TRỌNG) ---
        // Đảm bảo rằng ngay khi phát hiện thiếu hàng, hệ thống đã gọi lệnh rollback() để hủy đơn!
        verify(mockConnection, times(1)).rollback();
    }
}
