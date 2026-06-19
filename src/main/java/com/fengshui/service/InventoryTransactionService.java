package com.fengshui.service;

import com.fengshui.entity.InventoryTransaction;
import com.fengshui.repository.IInventoryTransactionRepository;
import com.fengshui.repository.InventoryTransactionRepository;

import java.sql.Connection;
import java.util.List;

public class InventoryTransactionService implements IInventoryTransactionService{
    private final IInventoryTransactionRepository inventoryTransactionRepository;
    public InventoryTransactionService() {
        this.inventoryTransactionRepository = new InventoryTransactionRepository();
    }

    @Override
    public List<InventoryTransaction> findAll() {
        return inventoryTransactionRepository.findAll();
    }

    @Override
    public List<InventoryTransaction> findByProductId(int productId) {
        return inventoryTransactionRepository.findByProductID(productId);
    }

    @Override
    public boolean save(InventoryTransaction transaction) {
        return inventoryTransactionRepository.save(transaction);
    }

    @Override
    public boolean save(Connection connection, InventoryTransaction transaction) {
        return inventoryTransactionRepository.save(connection,transaction);
    }
}
