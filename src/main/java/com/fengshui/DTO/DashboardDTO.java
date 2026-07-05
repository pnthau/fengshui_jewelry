package com.fengshui.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private double totalRevenue;
    private int newOrdersCount;
    private int lowStockProductsCount;
    private int totalUsersCount;
    private Map<String, Double> monthlyRevenue;
}