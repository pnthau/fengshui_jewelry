package com.fengshui.repository;

import com.fengshui.entity.InventoryTransaction;
import java.sql.Connection;
import java.util.List;

public interface IInventoryTransactionRepository {
    List<InventoryTransaction> findAll();
    List<InventoryTransaction> findByProductID(int productId);
    boolean save(InventoryTransaction transaction);
    
    // Phương thức transactional dùng chung Connection
    boolean save(Connection connection, InventoryTransaction transaction);
}
