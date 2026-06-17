package com.fengshui.service;

import com.fengshui.entity.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {
    private ProductService productService;
    private int testProductId = 0;

    @BeforeEach
    public void setUp() {
        productService = new ProductService();
    }

    @AfterEach
    public void tearDown() {
        if (testProductId > 0) {
            productService.delete(testProductId);
            testProductId = 0;
        }
    }

    @Test
    public void testSaveAndFindByID() {
        Product product = new Product();
        product.setName("Vòng Tay Cẩm Thạch Thử Nghiệm");
        product.setPrice(new BigDecimal("1500000.00"));
        product.setQuantity(5);
        product.setMaterial("Ngọc Cẩm Thạch");
        product.setStatus("ACTIVE");
        product.setDescription("Mô tả thử nghiệm");

        boolean saved = productService.save(product);
        assertTrue(saved, "Phải lưu sản phẩm thành công");
        assertTrue(product.getId() > 0, "Id sản phẩm sau khi lưu phải lớn hơn 0");
        testProductId = product.getId();

        Product found = productService.findByID(testProductId);
        assertNotNull(found, "Phải tìm thấy sản phẩm bằng ID");
        assertEquals("Vòng Tay Cẩm Thạch Thử Nghiệm", found.getName());
        assertEquals(new BigDecimal("1500000.00"), found.getPrice());
        assertEquals(5, found.getQuantity());
        assertEquals("Ngọc Cẩm Thạch", found.getMaterial());
    }

    @Test
    public void testUpdate() {
        Product product = new Product();
        product.setName("Nhẫn Thạch Anh Thử Nghiệm");
        product.setPrice(new BigDecimal("800000.00"));
        product.setQuantity(10);
        product.setMaterial("Thạch Anh");
        product.setStatus("ACTIVE");

        productService.save(product);
        testProductId = product.getId();

        // Cập nhật thông tin
        product.setName("Nhẫn Thạch Anh Đã Cập Nhật");
        product.setPrice(new BigDecimal("850000.00"));
        product.setQuantity(12);
        
        boolean updated = productService.update(product);
        assertTrue(updated, "Phải cập nhật sản phẩm thành công");

        Product found = productService.findByID(testProductId);
        assertEquals("Nhẫn Thạch Anh Đã Cập Nhật", found.getName());
        assertEquals(new BigDecimal("850000.00"), found.getPrice());
        assertEquals(12, found.getQuantity());
    }

    @Test
    public void testSearchByName() {
        Product product = new Product();
        product.setName("Vòng Chỉ Đỏ May Mắn XYZ");
        product.setPrice(new BigDecimal("200000.00"));
        product.setQuantity(20);
        product.setMaterial("Chỉ đỏ");
        product.setStatus("ACTIVE");

        productService.save(product);
        testProductId = product.getId();

        List<Product> searchResults = productService.searchByName("Chỉ Đỏ May Mắn");
        assertFalse(searchResults.isEmpty(), "Kết quả tìm kiếm không được rỗng");
        
        boolean found = false;
        for (Product p : searchResults) {
            if (p.getId() == testProductId) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Phải tìm thấy sản phẩm vừa lưu trong kết quả tìm kiếm");
    }

    @Test
    public void testFindByElement() {
        List<Product> kimProducts = productService.findByElement("KIM");
        assertNotNull(kimProducts, "Danh sách tìm kiếm mệnh Kim không được null");
        
        for (Product p : kimProducts) {
            assertNotNull(p.getElements(), "Sản phẩm tìm theo mệnh phải có danh sách mệnh không rỗng");
            assertTrue(p.getElements().contains("KIM"), "Sản phẩm tìm ra phải thực sự chứa mệnh KIM");
        }
    }
}
