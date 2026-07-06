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

    public boolean voidTransaction(int transactionId) {
        InventoryTransactionRepository repo = (InventoryTransactionRepository) transactionRepository;
        InventoryTransaction tx = repo.findById(transactionId);
        
        if (tx == null || "VOIDED".equals(tx.getStatus())) {
            throw new RuntimeException("Giao dịch không tồn tại hoặc đã bị hủy trước đó!");
        }

        try (Connection conn = ((BaseRepository) productRepository).getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 1. Cập nhật trạng thái phiếu thành VOIDED
                repo.updateStatus(conn, transactionId, "VOIDED");

                // 2. Hoàn tác số lượng tồn kho
                Product product = productRepository.findByID(tx.getProductId());
                if ("IMPORT".equals(tx.getTransactionType())) {
                    // Nếu hủy phiếu NHẬP -> Phải TRỪ kho
                    if (product.getQuantity() < tx.getQuantity()) {
                        throw new SQLException("Không thể hủy vì lượng hàng tồn kho hiện tại không đủ để trừ lại!");
                    }
                    product.setQuantity(product.getQuantity() - tx.getQuantity());
                } else {
                    // Nếu hủy phiếu XUẤT -> Phải CỘNG lại kho
                    product.setQuantity(product.getQuantity() + tx.getQuantity());
                }

                productRepository.updateInTransaction(conn, product);
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Lỗi khi hoàn tác kho: " + e.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
