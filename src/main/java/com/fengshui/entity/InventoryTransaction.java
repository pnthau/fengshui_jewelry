package com.fengshui.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransaction {
    private int id;
    private int productId;
    private String transactionType; // 'IMPORT' or 'EXPORT'
    private int quantity;
    private BigDecimal price;
    private String reason;
    private LocalDateTime createdAt;
    private int createdBy; // Links to User ID (Admin)
}
