package com.fengshui.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseRepository {
    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");

            if (URL == null || USER == null || PASSWORD == null) {
                throw new SQLException("Thiếu thông tin cấu hình Database! Hãy kiểm tra biến môi trường.");
            }

            // 3. Sử dụng .trim() để loại bỏ khoảng trắng thừa (nếu có)
            return DriverManager.getConnection(URL.trim(), USER.trim(), PASSWORD.trim());

        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver không được tìm thấy!", e);
        }
    }
}