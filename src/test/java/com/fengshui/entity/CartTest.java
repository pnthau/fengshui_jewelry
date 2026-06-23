package com.fengshui.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Cart cart;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        // Hàm này sẽ chạy trước MỖI bài test để "reset" lại dữ liệu cho sạch sẽ
        cart = new Cart();

        // Tạo sẵn 2 vật phẩm mẫu để dùng cho việc test
        product1 = new Product();
        product1.setId(1);
        product1.setName("Vòng Tỳ Hưu");
        product1.setPrice(new BigDecimal("1000000")); // 1 triệu

        product2 = new Product();
        product2.setId(2);
        product2.setName("Đá Thạch Anh");
        product2.setPrice(new BigDecimal("500000")); // 500 nghìn
    }

    @Test
    void testAddItem_NewProduct() {
        // Kịch bản: Bỏ 1 sản phẩm mới vào giỏ
        cart.addItem(product1, 2);

        // Kiểm tra xem giỏ hàng có đúng 1 loại mặt hàng không
        assertEquals(1, cart.getItems().size(), "Giỏ hàng phải có 1 mặt hàng");
        
        // Kiểm tra xem số lượng có đúng là 2 không
        assertEquals(2, cart.getItems().get(1).getQuantity(), "Số lượng Vòng Tỳ Hưu phải là 2");
    }

    @Test
    void testAddItem_ExistingProduct() {
        // Kịch bản: Bỏ 1 sản phẩm vào giỏ, sau đó lại bấm thêm sản phẩm ĐÓ một lần nữa
        cart.addItem(product1, 1); // Thêm 1 cái
        cart.addItem(product1, 2); // Thêm 2 cái nữa

        // Kiểm tra xem giỏ hàng vẫn chỉ có 1 mặt hàng (không bị tách dòng)
        assertEquals(1, cart.getItems().size());
        
        // Tổng số lượng phải được cộng dồn thành 3
        assertEquals(3, cart.getItems().get(1).getQuantity(), "Số lượng cộng dồn phải là 3");
    }

    @Test
    void testRemoveItem() {
        // Kịch bản: Bỏ 2 mặt hàng vào giỏ, sau đó xóa mặt hàng số 1
        cart.addItem(product1, 1);
        cart.addItem(product2, 1);

        cart.removeItem(1); // Xóa Vòng Tỳ Hưu

        // Kiểm tra xem giỏ chỉ còn 1 mặt hàng (là Đá Thạch Anh)
        assertEquals(1, cart.getItems().size());
        assertNull(cart.getItems().get(1), "Sản phẩm 1 phải bị xóa hoàn toàn");
        assertNotNull(cart.getItems().get(2), "Sản phẩm 2 vẫn phải còn");
    }

    @Test
    void testChangeQuantityItem() {
        // Kịch bản: Khách hàng tự gõ số lượng mới vào ô input
        cart.addItem(product1, 1);
        
        // Khách gõ số 10
        cart.changeQuantityItem(1, 10);

        assertEquals(10, cart.getItems().get(1).getQuantity(), "Số lượng phải bị ghi đè thành 10");
    }

    @Test
    void testGetTotalCartPrice() {
        // Kịch bản: Tính tiền
        // Mua 2 Vòng Tỳ Hưu (1 triệu x 2 = 2 triệu)
        cart.addItem(product1, 2);
        // Mua 3 Đá Thạch Anh (500 nghìn x 3 = 1.5 triệu)
        cart.addItem(product2, 3);

        // Tổng cộng phải là 3.5 triệu
        BigDecimal expectedTotal = new BigDecimal("3500000");
        BigDecimal actualTotal = cart.getTotalCartPrice();

        assertEquals(expectedTotal, actualTotal, "Tổng tiền tính toán bị sai");
    }

    @Test
    void testGetTotalCartPrice_EmptyCart() {
        // Kịch bản: Giỏ hàng trống thì tính tiền phải ra 0
        assertEquals(BigDecimal.ZERO, cart.getTotalCartPrice(), "Giỏ trống thì tổng tiền phải bằng 0");
    }
}
