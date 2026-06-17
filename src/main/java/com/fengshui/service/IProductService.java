package com.fengshui.service;

import com.fengshui.entity.Product;

import java.util.List;

public interface IProductService {
    List<Product> findAll();

    Product findByID(int id);

    boolean save(Product product);

    boolean delete(int id);

    boolean update(Product product);

    List<Product> searchByName(String name);

    List<Product> findByElement(String element);
}
