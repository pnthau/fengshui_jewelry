package com.fengshui.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int id;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime createdAt;
}
