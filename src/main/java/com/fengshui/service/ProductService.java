package com.fengshui.service;

import com.fengshui.entity.Product;
import com.fengshui.repository.BaseRepository;
import com.fengshui.repository.IProductRepository;
import com.fengshui.repository.ProductRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

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
        // return productRepository.update(product);
        try (Connection conn = BaseRepository.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction
            try {
                // 1. Cập nhật thông tin sản phẩm
                boolean updated = productRepository.update(product);

                // 2. Xóa mệnh cũ và thêm mệnh mới (nếu có)
                if (updated) {
                    productRepository.deleteElements(conn, product.getId());
                    if (product.getElements() != null) {
                        for (String element : product.getElements()) {
                            productRepository.addElements(conn, product.getId(), element);
                        }
                    }
                    conn.commit(); // Lưu thay đổi
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback(); // Hoàn tác nếu lỗi
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Product> searchByName(String name) {
        return productRepository.searchByName(name);
    }

    @Override
    public List<Product> findByElement(String element) {
        return productRepository.findByElement(element);
    }

    @Override
    public Set<String> getElementsForProduct(int productId) {
        return productRepository.getElementsForProduct(productId);
    }
}
