package com.fengshui.repository;

import com.fengshui.entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductRepository extends BaseRepository implements IProductRepository {
    private static final String SELECT_ALL_PRODUCTS = "SELECT * FROM products";
    private static final String SELECT_PRODUCT_BY_ID = "SELECT * FROM products WHERE id = ?";
    private static final String SELECT_ALL_PRODUCTS_ELEMENT = "select p.*, pe.element from products p join product_elements pe on p.id = pe.product_id where pe.element = ?";
    private static final String SELECT_PRODUCT_BY_NAME = "select * from products where name like ? and status = 'ACTIVE' ";
    private static final String INSERT_PRODUCT = "INSERT INTO products (name, price, quantity, material, image_url, youtube_url, status, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_PRODUCT = "UPDATE products SET name = ?, price = ?, quantity = ?, material = ?, image_url = ?, youtube_url = ?, status = ?, description = ? WHERE id = ?";
    private static final String DELETE_PRODUCT = "DELETE FROM products WHERE id = ?";

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
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
                product.setElements(getAllElementByProduct(connection, product.getId()));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public Product findByID(int id) {
        Product product = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_ID)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    product = new Product();
                    product.setId(resultSet.getInt("id"));
                    product.setName(resultSet.getString("name"));
                    product.setPrice(resultSet.getBigDecimal("price"));
                    product.setQuantity(resultSet.getInt("quantity"));
                    product.setMaterial(resultSet.getString("material"));
                    product.setImageURL(resultSet.getString("image_url"));
                    product.setYoutubeURL(resultSet.getString("youtube_url"));
                    product.setStatus(resultSet.getString("status"));
                    product.setDescription(resultSet.getString("description"));
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
        int rowsInserted = 0;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setBigDecimal(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setString(4, product.getMaterial());
            preparedStatement.setString(5, product.getImageURL());
            preparedStatement.setString(6, product.getYoutubeURL());
            preparedStatement.setString(7, product.getStatus());
            preparedStatement.setString(8, product.getDescription());   

            rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsInserted > 0;
    }

    @Override
    public boolean update(Product product) {
        int rowsUpdated = 0;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setBigDecimal(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setString(4, product.getMaterial());
            preparedStatement.setString(5, product.getImageURL());
            preparedStatement.setString(6, product.getYoutubeURL());
            preparedStatement.setString(7, product.getStatus());
            preparedStatement.setString(8, product.getDescription());
            preparedStatement.setInt(9, product.getId());

            rowsUpdated = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsUpdated > 0;
    }

    @Override
    public List<Product> findByElement(String element) {
        List<Product> products = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS_ELEMENT)
        ) {
            preparedStatement.setString(1, element);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Product product = null;
                while (resultSet.next()) {
                    product = new Product();

                    product.setId(resultSet.getInt("id"));
                    product.setName(resultSet.getString("name"));
                    product.setPrice(resultSet.getBigDecimal("price"));
                    product.setQuantity(resultSet.getInt("quantity"));
                    product.setMaterial(resultSet.getString("material"));
                    product.setImageURL(resultSet.getString("image_url"));
                    product.setYoutubeURL(resultSet.getString("youtube_url"));
                    product.setStatus(resultSet.getString("status"));
                    product.setDescription(resultSet.getString("description"));
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
        String sql = "select pe.element from product_elements pe where pe.product_id = ?";
        Set<String> elements = new HashSet<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
                Product product = null;
                while (resultSet.next()) {
                    product = new Product();

                    product.setId(resultSet.getInt("id"));
                    product.setName(resultSet.getString("name"));
                    product.setPrice(resultSet.getBigDecimal("price"));
                    product.setQuantity(resultSet.getInt("quantity"));
                    product.setMaterial(resultSet.getString("material"));
                    product.setImageURL(resultSet.getString("image_url"));
                    product.setYoutubeURL(resultSet.getString("youtube_url"));
                    product.setStatus(resultSet.getString("status"));
                    product.setDescription(resultSet.getString("description"));
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
    public boolean reduceStock(Connection connection, int productId, int quantity) {
        String sql = "UPDATE products SET quantity = quantity - ? WHERE id = ? AND quantity >= ?";
        int rowsUpdated = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, productId);
            preparedStatement.setInt(3, quantity);

            rowsUpdated = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsUpdated > 0;
    }
}
