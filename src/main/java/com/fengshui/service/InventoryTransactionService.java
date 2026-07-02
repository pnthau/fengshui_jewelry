package com.fengshui.service;

import com.fengshui.DTO.InventoryTransactionDTO;
import com.fengshui.entity.InventoryTransaction;
import com.fengshui.entity.Product;
import com.fengshui.repository.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class InventoryTransactionService implements IInventoryTransactionService{
    private final IInventoryTransactionRepository transactionRepository;
    private final IProductRepository productRepository;

    public InventoryTransactionService() {
        this.transactionRepository = new InventoryTransactionRepository();
        this.productRepository = new ProductRepository();
    }

    @Override
    public List<InventoryTransactionDTO> getAllTransactionsDTO() {
        return ((InventoryTransactionRepository) transactionRepository).findAllDTO();
    }

    @Override
    public boolean executeStockTransaction(InventoryTransaction transaction) {
        boolean isSuccess = false;

        try (Connection connection = ((BaseRepository) productRepository).getConnection()) {
            connection.setAutoCommit(false);{
            try {
                boolean transactionSaved = transactionRepository.save(connection, transaction);
                if (!transactionSaved) {
                    throw new SQLException("Failed to record inventory transaction log in database!");
                }

                if ("IMPORT".equals(transaction.getTransactionType())) {
                    Product product = productRepository.findByID(transaction.getProductId());
                    product.setQuantity(product.getQuantity() + transaction.getQuantity());
                    product.setPrice(transaction.getPrice()); // Cập nhật giá bán từ phiếu nhập
                    productRepository.updateInTransaction(connection, product);
                }

                connection.commit(); // Thành công -> COMMIT chính thức lưu trữ thay đổi
                isSuccess = true;
                System.out.println("✅ Inventory Transaction processed successfully by DB Trigger!");
            } catch (SQLException innerEx) {
                connection.rollback(); // Có bất kỳ lỗi gì xảy ra -> ROLLBACK ngay lập tức
                System.err.println("❌ LỖI DATABASE CHI TIẾT: " + innerEx.getSQLState() + " - " + innerEx.getMessage());
                throw new RuntimeException("Lỗi nhập kho: " + innerEx.getMessage());
            }
        }
    } catch (SQLException outerEx) {
            outerEx.printStackTrace();
        }
        return isSuccess;
    }
}

