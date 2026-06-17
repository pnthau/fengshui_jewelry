package com.fengshui.repository;

import com.fengshui.entity.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository extends BaseRepository implements IOrderRepository {
    private static final String SELECT_ALL_ORDERS = "SELECT * FROM orders ORDER BY created_at DESC";
    private static final String SELECT_ORDER_BY_ID = "SELECT * FROM orders WHERE id = ?";
    private static final String INSERT_ORDER = "INSERT INTO orders (customer_name, customer_phone, customer_address, total_price, status) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_ORDER_STATUS = "UPDATE orders SET status = ? WHERE id = ?";
    private static final String DELETE_ORDER = "DELETE FROM orders WHERE id = ?";

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ORDERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt("id"));
                order.setCustomerName(resultSet.getString("customer_name"));
                order.setCustomerPhone(resultSet.getString("customer_phone"));
                order.setCustomerAddress(resultSet.getString("customer_address"));
                order.setTotalPrice(resultSet.getBigDecimal("total_price"));
                order.setStatus(resultSet.getString("status"));
                Timestamp ts = resultSet.getTimestamp("created_at");
                if (ts != null) {
                    order.setCreatedAt(ts.toLocalDateTime());
                }
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public Order findByID(int id) {
        Order order = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ORDER_BY_ID)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    order = new Order();
                    order.setId(resultSet.getInt("id"));
                    order.setCustomerName(resultSet.getString("customer_name"));
                    order.setCustomerPhone(resultSet.getString("customer_phone"));
                    order.setCustomerAddress(resultSet.getString("customer_address"));
                    order.setTotalPrice(resultSet.getBigDecimal("total_price"));
                    order.setStatus(resultSet.getString("status"));
                    Timestamp ts = resultSet.getTimestamp("created_at");
                    if (ts != null) {
                        order.setCreatedAt(ts.toLocalDateTime());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public boolean save(Order order) {
        int rowsInserted = 0;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, order.getCustomerName());
            preparedStatement.setString(2, order.getCustomerPhone());
            preparedStatement.setString(3, order.getCustomerAddress());
            preparedStatement.setBigDecimal(4, order.getTotalPrice());
            preparedStatement.setString(5, order.getStatus());

            rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsInserted > 0;
    }

    @Override
    public boolean updateStatus(int id, String status) {
        int rowsUpdated = 0;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER_STATUS)) {

            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, id);

            rowsUpdated = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsUpdated > 0;
    }

    @Override
    public boolean delete(int id) {
        int rowsDeleted = 0;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ORDER)) {

            preparedStatement.setInt(1, id);
            rowsDeleted = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsDeleted > 0;
    }

    @Override
    public boolean save(Connection connection, Order order) {
        int rowsInserted = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, order.getCustomerName());
            preparedStatement.setString(2, order.getCustomerPhone());
            preparedStatement.setString(3, order.getCustomerAddress());
            preparedStatement.setBigDecimal(4, order.getTotalPrice());
            preparedStatement.setString(5, order.getStatus());

            rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsInserted > 0;
    }
}
