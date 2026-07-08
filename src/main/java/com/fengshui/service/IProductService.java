package com.fengshui.service;

import com.fengshui.entity.Product;

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

    // New method for Dashboard
    int countLowStockProducts(int threshold);

    boolean saveWithElements(Product product);
    boolean updateWithElements(Product product);
}