package com.fengshui.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private int id;
    private int orderId;
    private int productId;
    private int quantity;
    private BigDecimal priceAtPurchase;
    private String productName;
    private String imageURL; // Thuộc tính hình ảnh từ Product
}