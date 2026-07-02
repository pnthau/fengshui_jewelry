package com.fengshui.repository;

import com.fengshui.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductRepository extends BaseRepository implements IProductRepository {
    private static final String SELECT_ALL_PRODUCTS = "SELECT * FROM products ORDER BY id DESC";
    private static final String SELECT_PRODUCT_BY_ID = "SELECT * FROM products WHERE id = ?";
    private static final String SELECT_ALL_PRODUCTS_ELEMENT = "SELECT p.*, pe.element FROM products p JOIN product_elements pe ON p.id = pe.product_id WHERE pe.element = ?";
    private static final String SELECT_PRODUCT_BY_NAME = "SELECT * FROM products WHERE name LIKE ? ORDER BY id DESC";
    private static final String INSERT_PRODUCT = "INSERT INTO products (name, price, quantity, material, image_url, youtube_url, status, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_PRODUCT = "UPDATE products SET name = ?, price = ?, quantity = ?, material = ?, image_url = ?, youtube_url = ?, status = ?, description = ? WHERE id = ?";
    private static final String DELETE_PRODUCT = "DELETE FROM products WHERE id = ?";
    private static final String DELETE_ELEMENTS = "DELETE FROM product_elements WHERE product_id = ?";
    private static final String INSERT_ELEMENT = "INSERT INTO product_elements (product_id, element) VALUES (?, ?)";
    private static final String SELECT_ELEMENTS_BY_PRODUCT_ID = "SELECT element FROM product_elements WHERE product_id = ?";
    private static final String UPDATE_STOCK_REDUCE = "UPDATE products SET quantity = quantity - ? WHERE id = ? AND quantity >= ?";
    private static final String UPDATE_STOCK_INCREASE = "UPDATE products SET quantity = quantity + ? WHERE id = ?";

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Product product = mapResultSetToProduct(resultSet);
                // Sử dụng connection đang mở để nạp mảng hệ mệnh, cực kỳ tiết kiệm tài nguyên
                product.setElements(this.getAllElementByProduct(connection, product.getId()));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    private static Product mapResultSetToProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getInt("id"));
        product.setName(resultSet.getString("name"));
        product.setPrice(resultSet.getBigDecimal("price"));
        product.setQuantity(resultSet.getInt("quantity"));
        product.setMaterial(resultSet.getString("material"));
        product.setImageURL(resultSet.getString("image_url"));
        product.setYoutubeURL(resultSet.getString("youtube_url"));
        product.setStatus(resultSet.getString("status"));
        product.setDescription(resultSet.getString("description"));
        return product;
    }

    @Override
    public Product findByID(int id) {
        Product product = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_ID)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    product = mapResultSetToProduct(resultSet);
                    product.setElements(this.getAllElementByProduct(connection, product.getId()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public boolean save(Product product) {
        try (Connection connection = getConnection()) {
            return this.save(connection, product);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Product> findByElement(String element) {
        List<Product> products = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS_ELEMENT)
        ) {
            preparedStatement.setString(1, element);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = mapResultSetToProduct(resultSet);
                    product.setElements(this.getAllElementByProduct(connection, product.getId()));

                    products.add(product);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return products;
    }

    private Set<String> getAllElementByProduct(Connection connection, int productID) {
        Set<String> elements = new HashSet<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ELEMENTS_BY_PRODUCT_ID)) {
            preparedStatement.setInt(1, productID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String element = resultSet.getString(1);
                    elements.add(element);
                }
            }
        } catch (
                SQLException ex) {
            ex.printStackTrace();
        }
        return elements;
    }

    @Override
    public List<Product> searchByName(String name) {
        List<Product> products = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_NAME);
        ) {
            preparedStatement.setString(1, "%" + name + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = mapResultSetToProduct(resultSet);
                    product.setElements(this.getAllElementByProduct(connection, product.getId()));

                    products.add(product);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return products;
    }

    @Override
    public boolean delete(int id) {
        int rowsDeleted = 0;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PRODUCT)) {

            preparedStatement.setInt(1, id);
            rowsDeleted = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsDeleted > 0;
    }

    @Override
    public void deleteElements(Connection conn, int productId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(DELETE_ELEMENTS)) {
            ps.setInt(1, productId);
            ps.executeUpdate();
        }
    }

    @Override
    public void addElements(Connection conn, int productId, String element) throws SQLException {
        String sql = "INSERT INTO product_elements (product_id, element) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(INSERT_ELEMENT)) {
            ps.setInt(1, productId);
            ps.setString(2, element);
            ps.executeUpdate();
        }
    }
    @Override
    public boolean update(Product product) {
        try (Connection conn = getConnection()) {
            return updateInTransaction(conn, product);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateInTransaction(Connection conn, Product product) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(UPDATE_PRODUCT)) {
            ps.setString(1, product.getName());
            ps.setBigDecimal(2, product.getPrice());
            ps.setInt(3, product.getQuantity());
            ps.setString(4, product.getMaterial());
            ps.setString(5, product.getImageURL());
            ps.setString(6, product.getYoutubeURL());
            ps.setString(7, product.getStatus());
            ps.setString(8, product.getDescription());
            ps.setInt(9, product.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean reduceStock(Connection connection, int productId, int quantity) {
        int rowsUpdated = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STOCK_REDUCE)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, productId);
            preparedStatement.setInt(3, quantity);

            rowsUpdated = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsUpdated > 0;

    }
    @Override
    public boolean increaseStock(Connection connection, int productId, int quantity) {
        int rowsUpdated = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STOCK_INCREASE)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, productId);
            rowsUpdated = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsUpdated > 0;
    }

    @Override
    public boolean save(Connection conn, Product product) throws SQLException {

        // Bắt buộc phải cấu hình Statement.RETURN_GENERATED_KEYS để lấy ID tự tăng sinh ra từ DB
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getName());
            stmt.setBigDecimal(2, product.getPrice());
            stmt.setInt(3, product.getQuantity());
            stmt.setString(4, product.getMaterial());
            stmt.setString(5, product.getImageURL());
            stmt.setString(6, product.getDescription());
            stmt.setString(7, product.getStatus());
            stmt.setString(8, product.getDescription());

            int rowAffected = stmt.executeUpdate();

            if (rowAffected > 0) {
                // Lấy ID tự sinh của sản phẩm vừa INSERT thành công
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        product.setId(generatedId); // GÁN NGƯỢC ID VÀO ĐỐI TƯỢNG PRODUCT
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * TRANSACTION 1: Thêm mới sản phẩm và lưu các mệnh ngũ hành liên quan trọn gói
     */
    @Override
    public boolean saveWithElements(Product product) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu giao dịch an toàn
            try {
                // 1. Lưu sản phẩm chính trước để lấy ID tự tăng
                boolean isProductSaved = this.save(conn, product);

                // 2. Nếu lưu thành công, lưu tiếp các mệnh ngũ hành vào bảng liên kết
                if (isProductSaved) {
                    if (product.getElements() != null && !product.getElements().isEmpty()) {
                        for (String element : product.getElements()) {
                            this.addElements(conn, product.getId(), element);
                        }
                    }
                    conn.commit(); // Thành công trọn vẹn mới ghi dữ liệu xuống DB
                    return true;
                }
                conn.rollback();
            } catch (SQLException e) {
                conn.rollback(); // Hoàn tác dữ liệu nếu có bất kỳ lỗi nào xảy ra
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * TRANSACTION 2: Cập nhật thông tin sản phẩm và đồng bộ mảng mệnh ngũ hành mới
     */
    @Override
    public boolean updateWithElements(Product product) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu giao dịch an toàn
            try {
                // 1. Cập nhật thông tin sản phẩm chính trước
                boolean isProductUpdated = this.updateInTransaction(conn, product);

                // 2. Nếu thành công, tiến hành xóa toàn bộ mệnh cũ và đồng bộ lại mệnh mới
                if (isProductUpdated) {
                    this.deleteElements(conn, product.getId());
                    if (product.getElements() != null && !product.getElements().isEmpty()) {
                        for (String element : product.getElements()) {
                            this.addElements(conn, product.getId(), element);
                        }
                    }
                    conn.commit(); // Thực hiện commit thành công dữ liệu xuống DB
                    return true;
                }
                conn.rollback();
            } catch (SQLException e) {
                conn.rollback(); // Hoàn tác dữ liệu ngay lập tức khi xảy ra lỗi SQL
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
