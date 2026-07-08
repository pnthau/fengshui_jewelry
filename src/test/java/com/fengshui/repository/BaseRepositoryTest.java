package com.fengshui.repository;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class BaseRepositoryTest {

    @Test
    public void testGetConnection_Success() {
        // Vì BaseRepository là abstract, ta khởi tạo anonymous class để test
        BaseRepository baseRepository = new BaseRepository() {};
        
        assertDoesNotThrow(() -> {
            try (Connection connection = baseRepository.getConnection()) {
                assertNotNull(connection, "Kết nối CSDL không được phép null!");
                assertFalse(connection.isClosed(), "Kết nối CSDL phải đang mở!");
                System.out.println("✅ Unit Test: Kết nối CSDL thành công!");
            }
        });
    }
}
