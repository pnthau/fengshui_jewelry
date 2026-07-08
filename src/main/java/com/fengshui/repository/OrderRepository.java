package com.fengshui.repository;

import com.fengshui.entity.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository extends BaseRepository implements IOrderRepository {
    private static final String SELECT_ALL_ORDERS = "SELECT * FROM orders ORDER BY created_at DESC";
    private static final String SELECT_ORDER_BY_ID = "SELECT * FROM orders WHERE id = ?";
    private static final String INSERT_ORDER = "INSERT INTO orders (customer_name, customer_phone, customer_address, total_price, status) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_ORDER_STATUS = "UPDATE orders SET status = ? WHERE id = ?";
    private static final String DELETE_ORDER = "DELETE FROM orders WHERE id = ?";
    private static final String SELECT_TOTAL_REVENUE = "SELECT SUM(total_price) FROM orders WHERE status = 'SUCCESS'";
    private static final String COUNT_ORDERS_BY_STATUS = "SELECT COUNT(*) FROM orders WHERE status = ?";
    private static final String SELECT_MONTHLY_REVENUE = "SELECT MONTH(created_at) AS month, SUM(total_price) AS revenue FROM orders WHERE status = 'SUCCESS' AND YEAR(created_at) = YEAR(CURDATE()) GROUP BY MONTH(created_at) ORDER BY MONTH(created_at)";


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
            throw new RuntimeException("DB Error in save(Order): " + e.getMessage());
        }
        return rowsInserted > 0;
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
            throw new RuntimeException("DB Error in save(Conn, Order): " + e.getMessage());
        }
        return rowsInserted > 0;
    }

    @Override
    public boolean update(Order order) {
        return false;
    }

    @Override
    public boolean updateStatus(int orderId, String status) {
        int rowsUpdated = 0;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER_STATUS)) {

            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, orderId);

            rowsUpdated = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsUpdated > 0;
    }

    @Override
    public boolean updateStatus(Connection connection, int orderId, String status) {
        int rowsUpdated = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER_STATUS)) {
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, orderId);
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
    public double getTotalRevenue() {
        double totalRevenue = 0;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TOTAL_REVENUE);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                totalRevenue = resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRevenue;
    }

    @Override
    public int countOrdersByStatus(String status) {
        int count = 0;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(COUNT_ORDERS_BY_STATUS)) {
            preparedStatement.setString(1, status);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public Map<String, Double> getMonthlyRevenue() {
        Map<String, Double> monthlyRevenue = new LinkedHashMap<>();
        // Initialize all 12 months with 0 revenue
        for (int i = 1; i <= 12; i++) {
            monthlyRevenue.put("Tháng " + i, 0.0);
        }

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_MONTHLY_REVENUE);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int month = resultSet.getInt("month");
                double revenue = resultSet.getDouble("revenue");
                monthlyRevenue.put("Tháng " + month, revenue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monthlyRevenue;
    }
}
