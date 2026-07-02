package com.fengshui.repository;

import com.fengshui.DTO.InventoryTransactionDTO;
import com.fengshui.entity.InventoryTransaction;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryTransactionRepository extends BaseRepository implements IInventoryTransactionRepository {
    private static final String SELECT_ALL_TRANSACTIONS = "SELECT * FROM inventory_transactions ORDER BY created_at DESC";
    private static final String SELECT_TRANSACTIONS_BY_PRODUCT_ID = "SELECT * FROM inventory_transactions WHERE product_id = ? ORDER BY created_at DESC";
    private static final String INSERT_TRANSACTION = "INSERT INTO inventory_transactions (product_id, transaction_type, quantity, price, reason, created_by) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_TRANSACTIONS_WITH_JOIN =
            "SELECT t.*, p.name AS product_name, p.image_url AS product_image " +
                    "FROM inventory_transactions t " +
                    "LEFT JOIN products p ON t.product_id = p.id " +
                    "ORDER BY t.created_at DESC";

    private static final String SELECT_TRANSACTIONS_BY_PRODUCT_ID_WITH_JOIN =
            "SELECT t.*, p.name AS product_name, p.image_url AS product_image " +
                    "FROM inventory_transactions t " +
                    "LEFT JOIN products p ON t.product_id = p.id " +
                    "WHERE t.product_id = ? " +
                    "ORDER BY t.created_at DESC";
    
    @Override
    public List<InventoryTransaction> findAll() {
        List<InventoryTransaction> list = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TRANSACTIONS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                list.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<InventoryTransactionDTO> findAllDTO() {
        List<InventoryTransactionDTO> list = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TRANSACTIONS_WITH_JOIN);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                list.add(mapResultSetToDTO(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  list;
    }

    @Override
    public List<InventoryTransaction> findByProductID(int productId) {
        List<InventoryTransaction> list = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TRANSACTIONS_BY_PRODUCT_ID)) {

            preparedStatement.setInt(1, productId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(mapResultSetToEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<InventoryTransactionDTO> findDTOByProductID(int productId) {
        List<InventoryTransactionDTO> list = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TRANSACTIONS_BY_PRODUCT_ID_WITH_JOIN)) {

            preparedStatement.setInt(1, productId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(mapResultSetToDTO(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean save(InventoryTransaction transaction) {
        try (Connection connection = getConnection()) {
            return save(connection, transaction);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean save(Connection connection, InventoryTransaction transaction) {
        int rowsInserted = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TRANSACTION)) {

            preparedStatement.setInt(1, transaction.getProductId());
            preparedStatement.setString(2, transaction.getTransactionType());
            preparedStatement.setInt(3, transaction.getQuantity());
            BigDecimal price = transaction.getPrice();
            if (price == null) price = BigDecimal.ZERO;
            preparedStatement.setBigDecimal(4, price);
            preparedStatement.setString(5, transaction.getReason());

            // Xử lý an toàn: đặt giá trị NULL cho cột created_by nếu người thực hiện không tồn tại (> 0)
            if (transaction.getCreatedBy() > 0) {
                preparedStatement.setInt(6, transaction.getCreatedBy());
            } else {
                preparedStatement.setNull(6, Types.INTEGER);
            }

            rowsInserted = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("--- LỖI DATABASE KHI GHI LOG KHO ---");
            System.err.println("Mã lỗi: " + e.getErrorCode());
            System.err.println("Thông báo: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            e.printStackTrace(); // In ra toàn bộ dấu vết lỗi
            return false;
        }
        return rowsInserted > 0;
    }
    private static InventoryTransaction mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        InventoryTransaction tx = new InventoryTransaction();
        tx.setId(resultSet.getInt("id"));
        tx.setProductId(resultSet.getInt("product_id"));
        tx.setTransactionType(resultSet.getString("transaction_type"));
        tx.setQuantity(resultSet.getInt("quantity"));
        tx.setPrice(resultSet.getBigDecimal("price"));
        tx.setReason(resultSet.getString("reason"));
        Timestamp ts = resultSet.getTimestamp("created_at");
        if (ts != null) {
            tx.setCreatedAt(ts.toLocalDateTime());
        }
        tx.setCreatedBy(resultSet.getInt("created_by"));
        return tx;
    }
    private InventoryTransactionDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("created_at");
        return InventoryTransactionDTO.builder()
                .id(rs.getInt("id"))
                .productId(rs.getInt("product_id"))
                .productName(rs.getString("product_name"))
                .imageURL(rs.getString("product_image"))
                .transactionType(rs.getString("transaction_type"))
                .quantity(rs.getInt("quantity"))
                .price(rs.getBigDecimal("price"))
                .reason(rs.getString("reason"))
                .createdAt(ts != null ? ts.toLocalDateTime() : null)
                .adminId(rs.getInt("created_by"))
                .build();
    }
}
