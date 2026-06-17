package com.fengshui.service;

import com.fengshui.entity.Product;
import com.fengshui.repository.IProductRepository;
import com.fengshui.repository.ProductRepository;

import java.util.List;

public class ProductService implements IProductService {
    // Tham chiếu tới Interface của Repository theo nguyên lý Dependency Inversion
    private final IProductRepository productRepository;

    public ProductService() {
        this.productRepository = new ProductRepository();
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findByID(int id) {
        return productRepository.findByID(id);
    }

    @Override
    public boolean save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public boolean delete(int id) {
        return productRepository.delete(id);
    }

    @Override
    public boolean update(Product product) {
        return productRepository.update(product);
    }

    @Override
    public List<Product> searchByName(String name) {
        return productRepository.searchByName(name);
    }

    @Override
    public List<Product> findByElement(String element) {
        return productRepository.findByElement(element);
    }
}
