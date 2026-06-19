package com.fengshui.service;

import com.fengshui.entity.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface IProductService {
    List<Product> findAll();

    Product findByID(int id);

    boolean save(Product product);

    boolean delete(int id);

    boolean update(Product product);

    List<Product> searchByName(String name);

    List<Product> findByElement(String element);

    Set<String> getElementsForProduct(int productId);
}
