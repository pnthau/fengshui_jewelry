package com.fengshui.repository;

import com.fengshui.entity.Order;
import com.fengshui.entity.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface IProductRepository {
    List<Product> findAll();

    Product findByID(int id);

    boolean save(Product product);

    boolean delete(int id);

    boolean update(Product product);

    List<Product> findByElement(String element);

    List<Product> searchByName(String name);

    void deleteElements(Connection conn, int productId) throws SQLException;
    void addElements(Connection conn, int productId, String element) throws SQLException;

    boolean updateInTransaction(Connection conn, Product product) throws SQLException;

    boolean reduceStock(Connection connection, int productId, int quantity);

    boolean save(Connection connection, Product product) throws java.sql.SQLException;
    boolean saveWithElements(Product product);
    boolean updateWithElements(Product product);
    boolean increaseStock(Connection connection, int productId, int quantity);
}
