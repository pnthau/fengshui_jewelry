package com.fengshui.service;

import com.fengshui.entity.InventoryTransaction;

import java.sql.Connection;
import java.util.List;

public interface IInventoryTransactionService {
    List<InventoryTransaction> findAll();
    List<InventoryTransaction> findByProductId(int productId);
    boolean save(InventoryTransaction transaction);
    // Dùng cho Transaction Management trong Service
    boolean save(Connection connection, InventoryTransaction transaction);
}
