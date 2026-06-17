package com.fengshui.repository;

import com.fengshui.entity.Product;

import java.util.List;

public interface IProductRepository {
    List<Product> findAll();

    Product findByID(int id);

    boolean save(Product product);

    boolean delete(int id);

    boolean update(Product product);

    List<Product> findByElement(String element);

    List<Product> searchByName(String name);
    //boolean reduceStock(Connection connection, int productId, int quantity);
}
