package com.fengshui.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryTransactionDTO {
    private int id;
    private int productId;
    private String productName;
    private String imageURL;
    private String transactionType;
    private int quantity;
    private BigDecimal price;
    private String reason;
    private LocalDateTime createdAt;
    private int adminId;

}
