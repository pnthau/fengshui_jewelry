package com.fengshui.repository;

import com.fengshui.entity.InventoryTransaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class InventoryTransactionRepository extends BaseRepository implements IInventoryTransactionRepository {
    private static final String SELECT_ALL_TRANSACTIONS = "SELECT * FROM inventory_transactions ORDER BY created_at DESC";
    private static final String SELECT_TRANSACTIONS_BY_PRODUCT_ID = "SELECT * FROM inventory_transactions WHERE product_id = ? ORDER BY created_at DESC";
    private static final String INSERT_TRANSACTION = "INSERT INTO inventory_transactions (product_id, transaction_type, quantity, price, reason, created_by) VALUES (?, ?, ?, ?, ?, ?)";

    @Override
    public List<InventoryTransaction> findAll() {
        List<InventoryTransaction> list = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TRANSACTIONS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
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
                list.add(tx);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<InventoryTransaction> findByProductID(int productId) {
        List<InventoryTransaction> list = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TRANSACTIONS_BY_PRODUCT_ID)) {

            preparedStatement.setInt(1, productId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
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
                    list.add(tx);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean save(InventoryTransaction transaction) {
        int rowsInserted = 0;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TRANSACTION)) {

            preparedStatement.setInt(1, transaction.getProductId());
            preparedStatement.setString(2, transaction.getTransactionType());
            preparedStatement.setInt(3, transaction.getQuantity());
            preparedStatement.setBigDecimal(4, transaction.getPrice());
            preparedStatement.setString(5, transaction.getReason());
            preparedStatement.setInt(6, transaction.getCreatedBy());

            rowsInserted = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsInserted > 0;
    }

    @Override
    public boolean save(Connection connection, InventoryTransaction transaction) {
        int rowsInserted = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TRANSACTION)) {

            preparedStatement.setInt(1, transaction.getProductId());
            preparedStatement.setString(2, transaction.getTransactionType());
            preparedStatement.setInt(3, transaction.getQuantity());
            preparedStatement.setBigDecimal(4, transaction.getPrice());
            preparedStatement.setString(5, transaction.getReason());
            preparedStatement.setInt(6, transaction.getCreatedBy());

            rowsInserted = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsInserted > 0;
    }
}
