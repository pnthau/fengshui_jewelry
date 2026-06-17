package com.fengshui.repository;

import com.fengshui.entity.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemRepository extends BaseRepository implements IOrderItemRepository {
    private static final String SELECT_ITEMS_BY_ORDER_ID = "SELECT * FROM order_items WHERE order_id = ?";
    private static final String INSERT_ORDER_ITEM = "INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase) VALUES (?, ?, ?, ?)";

    @Override
    public List<OrderItem> findByOrderID(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ITEMS_BY_ORDER_ID)) {

            preparedStatement.setInt(1, orderId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    OrderItem item = new OrderItem();
                    item.setId(resultSet.getInt("id"));
                    item.setOrderId(resultSet.getInt("order_id"));
                    item.setProductId(resultSet.getInt("product_id"));
                    item.setQuantity(resultSet.getInt("quantity"));
                    item.setPriceAtPurchase(resultSet.getBigDecimal("price_at_purchase"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public boolean save(OrderItem orderItem) {
        int rowsInserted = 0;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER_ITEM)) {

            preparedStatement.setInt(1, orderItem.getOrderId());
            preparedStatement.setInt(2, orderItem.getProductId());
            preparedStatement.setInt(3, orderItem.getQuantity());
            preparedStatement.setBigDecimal(4, orderItem.getPriceAtPurchase());

            rowsInserted = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsInserted > 0;
    }

    @Override
    public boolean save(Connection connection, OrderItem orderItem) {
        int rowsInserted = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER_ITEM)) {

            preparedStatement.setInt(1, orderItem.getOrderId());
            preparedStatement.setInt(2, orderItem.getProductId());
            preparedStatement.setInt(3, orderItem.getQuantity());
            preparedStatement.setBigDecimal(4, orderItem.getPriceAtPurchase());

            rowsInserted = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsInserted > 0;
    }
}
