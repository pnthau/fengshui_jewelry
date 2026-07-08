package com.fengshui.repository;

import com.fengshui.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductRepositoryTest {
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        productRepository = new ProductRepository();
    }

    @Test
    public void testFindAllSuccess() {
        assertDoesNotThrow(() -> {
            List<Product> products = productRepository.findAll();
            assertNotNull(products, "Danh sách sản phẩm không được null!");
            System.out.println("✅ Test findAll() thành công! Số lượng sản phẩm hiện tại: " + products.size());
        });
    }

    @Test
    public void testCRUDLifecycle() {
        assertDoesNotThrow(() -> {
            // 1. Khởi tạo đối tượng test
            Product product = new Product();
            product.setName("Vòng Tay Test JUnit");
            product.setPrice(new BigDecimal("150000.00"));
            product.setQuantity(10);
            product.setMaterial("Đá Thạch Anh");
            product.setImageURL("assets/images/test.jpg");
            product.setYoutubeURL("https://youtube.com/test");
            product.setStatus("ACTIVE");
            product.setDescription("Mô tả test JUnit");

            // 2. Test INSERT (save)
            boolean isSaved = productRepository.save(product);
            assertTrue(isSaved, "Add product not succeed");
            assertTrue(product.getId() > 0, "ID tự tăng phải được lấy về thành công!");
            System.out.println("✅ Test save() ID :" + product.getId());

            // 3. Test READ (findByID)
            Product foundProduct = productRepository.findByID(product.getId());
            assertNotNull(foundProduct, "Tìm sản phẩm vừa tạo theo ID không được trả về null!");
            assertEquals("Vòng Tay Test JUnit", foundProduct.getName(), "Tên sản phẩm tìm thấy phải khớp!");

            // 4. Test UPDATE
            foundProduct.setName("Vòng Tay Test JUnit Update");
            foundProduct.setPrice(new BigDecimal("200000.00"));
            boolean isUpdated = productRepository.update(foundProduct);
            assertTrue(isUpdated, "Cập nhật sản phẩm phải thành công!");

            Product updatedProduct = productRepository.findByID(product.getId());
            assertEquals("Vòng Tay Test JUnit Update", updatedProduct.getName(), "Tên sau khi update phải khớp!");
            assertEquals(new BigDecimal("200000.00"), updatedProduct.getPrice(), "Giá sau khi update phải khớp!");
            System.out.println("✅ Test update()");

            // 5. Test DELETE
            boolean isDeleted = productRepository.delete(product.getId());
            assertTrue(isDeleted, "Xóa sản phẩm test phải thành công!");

            Product deletedProduct = productRepository.findByID(product.getId());
            assertNull(deletedProduct, "Sản phẩm sau khi xóa tìm lại phải trả về null!");
            System.out.println("✅ Test delete()");
        });
    }

    @Test
    public void testFindByElement() {
        assertDoesNotThrow(() -> {
            List<Product> products = productRepository.findByElement("KIM");
            assertNotNull(products);
            assertFalse(products.isEmpty(), "Danh sách sản phẩm mệnh KIM không được rỗng!");

            // Kiểm tra ngầm xem hàm getAllElementByProduct có chạy đúng không
            Product p = products.get(0);
            assertNotNull(p.getElements(), "Elements của sản phẩm không được null!");
            assertTrue(p.getElements().contains("KIM"), "Sản phẩm tìm thấy phải chứa mệnh KIM!");
            System.out.println("✅ Test findByElement() thành công! Mệnh tìm được: " + p.getElements());
        });
    }

    @Test
    public void testSearchByName() {
        assertDoesNotThrow(() -> {
            // Thử tìm kiếm với chữ "Obsidian" (không dấu để tránh lỗi font DB)
            List<Product> products = productRepository.searchByName("Obsidian");
            assertNotNull(products);
            assertFalse(products.isEmpty(), "Tìm kiếm từ khóa 'Obsidian' phải ra kết quả!");

            Product p = products.get(0);
            assertTrue(p.getName().contains("Obsidian"), "Tên sản phẩm tìm thấy phải chứa từ khóa tìm kiếm!");
            assertNotNull(p.getElements(), "Elements của sản phẩm tìm được không được null!");
            System.out.println("✅ Test searchByName() thành công! Sản phẩm tìm thấy: " + p.getName());
        });
    }
}
