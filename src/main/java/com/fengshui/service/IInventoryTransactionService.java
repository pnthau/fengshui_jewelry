package com.fengshui.service;

import com.fengshui.DTO.InventoryTransactionDTO;
import com.fengshui.entity.InventoryTransaction;

import java.util.List;

public interface IInventoryTransactionService {
    List<InventoryTransactionDTO> getAllTransactionsDTO();

    boolean executeStockTransaction(InventoryTransaction transaction);
}
