package com.fengshui.repository;

import com.fengshui.entity.InventoryTransaction;
import com.fengshui.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTransactionRepositoryTest {
    private ProductRepository productRepository;
    private InventoryTransactionRepository transactionRepository;

    @BeforeEach
    public void setUp() {
        productRepository = new ProductRepository();
        transactionRepository = new InventoryTransactionRepository();
    }

    @Test
    public void testFindAll_Success() {
        assertDoesNotThrow(() -> {
            List<InventoryTransaction> list = transactionRepository.findAll();
            assertNotNull(list);
            System.out.println("✅ Test findAll() Transactions thành công! Số lượng: " + list.size());
        });
    }

    @Test
    public void testTransaction_SaveAndFind() {
        assertDoesNotThrow(() -> {
            // 1. Tạo sản phẩm test
            Product product = new Product();
            product.setName("Sản phẩm Test Kho");
            product.setPrice(new BigDecimal("100000.00"));
            product.setQuantity(0); // Bắt đầu bằng 0
            product.setMaterial("Đồng");
            product.setStatus("ACTIVE");
            productRepository.save(product);
            assertTrue(product.getId() > 0);

            // 2. Tạo giao dịch nhập kho test (sử dụng created_by = 1 là Admin mặc định trong SQL)
            InventoryTransaction tx = new InventoryTransaction();
            tx.setProductId(product.getId());
            tx.setTransactionType("IMPORT");
            tx.setQuantity(50);
            tx.setPrice(new BigDecimal("80000.00"));
            tx.setReason("Nhập hàng test JUnit");
            tx.setCreatedBy(1); // Admin ID = 1

            // 3. Test save()
            boolean isSaved = transactionRepository.save(tx);
            assertTrue(isSaved, "Thêm mới giao dịch kho phải thành công!");

            // 4. Test findByProductID()
            List<InventoryTransaction> transactions = transactionRepository.findByProductID(product.getId());
            assertEquals(1, transactions.size(), "Số lượng giao dịch của sản phẩm test phải là 1!");
            assertEquals("IMPORT", transactions.get(0).getTransactionType());
            System.out.println("✅ Test save() và findByProductID() cho Kho thành công!");

            // 5. Dọn dẹp CSDL
            // Xóa giao dịch kho trước (Dùng kết nối thô trong test để xóa sạch tránh lỗi khóa ngoại)
            try (Connection conn = productRepository.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM inventory_transactions WHERE product_id = ?")) {
                ps.setInt(1, product.getId());
                ps.executeUpdate();
            }
            
            // Sau khi xóa giao dịch kho, ta mới xóa được sản phẩm test
            productRepository.delete(product.getId());
            System.out.println("✅ Dữ liệu test Kho đã được dọn sạch hoàn toàn!");
        });
    }
}
