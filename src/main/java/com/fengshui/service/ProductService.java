package com.fengshui.service;

import com.fengshui.entity.Product;
import com.fengshui.repository.IProductRepository;
import com.fengshui.repository.ProductRepository;

import java.sql.Connection;
import java.sql.SQLException;
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
        // return productRepository.save(product);
        try (Connection conn = ((ProductRepository) this.productRepository).getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction
            try {
                // 1. Lưu sản phẩm vào bảng products trước để lấy ID tự tăng sinh ra
                boolean saved = productRepository.save(conn, product);

                // 2. Nếu lưu sản phẩm thành công, tiến hành lưu tiếp các mệnh liên kết
                if (saved) {
                    if (product.getElements() != null) {
                        for (String element : product.getElements()) {
                            // Gọi repo lưu từng mệnh vào bảng trung gian
                            productRepository.addElements(conn, product.getId(), element);
                        }
                    }
                    conn.commit(); // Thành công hết thì commit dữ liệu xuống DB
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback(); // Nếu có bất kỳ lỗi nào, hoàn tác dữ liệu (Rollback)
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        return productRepository.delete(id);
    }

    @Override
    public boolean update(Product product) {
        // return productRepository.update(product);
        //try (Connection conn = BaseRepository.getConnection()) {
        try (Connection conn = ((ProductRepository) this.productRepository).getConnection()) { // ép kiểu
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
}
