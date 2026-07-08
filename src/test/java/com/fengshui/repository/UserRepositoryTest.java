package com.fengshui.repository;

import com.fengshui.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository = new UserRepository();
    }

    @Test
    public void testFindAllSuccess() {
        assertDoesNotThrow(() -> {
            List<User> users = userRepository.findAll();
            assertNotNull(users, "Danh sách người dùng không được null!");
            System.out.println("✅ Test findAll() Users thành công! Số lượng: " + users.size());
        });
    }

    @Test
    public void testUserCRUDLifecycle() {
        assertDoesNotThrow(() -> {
            // 1. Khởi tạo User test
            User user = new User();
            user.setUsername("test_user_junit");
            user.setPassword("testpwd123");
            user.setRole("ADMIN");

            // 2. Test INSERT (save)
            boolean isSaved = userRepository.save(user);
            assertTrue(isSaved, "Thêm mới user test phải thành công!");

            // 3. Test READ (findByUsername) để lấy ID của user vừa thêm
            User foundUser = userRepository.findByUsername("test_user_junit");
            assertNotNull(foundUser, "Tìm user test theo username không được null!");
            assertTrue(foundUser.getId() > 0, "ID của user phải lớn hơn 0!");
            assertEquals("testpwd123", foundUser.getPassword(), "Mật khẩu phải khớp!");
            System.out.println("✅ Test save() and findByUsername()  User | ID: " + foundUser.getId());

            // 4. Test UPDATE
            foundUser.setPassword("new_secure_password");
            boolean isUpdated = userRepository.update(foundUser);
            assertTrue(isUpdated, "Cập nhật mật khẩu user phải thành công!");

            User updatedUser = userRepository.findByID(foundUser.getId());
            assertEquals("new_secure_password", updatedUser.getPassword(), "Mật khẩu mới phải được cập nhật vào DB!");
            System.out.println("✅ Test update() User");

            // 5. Test DELETE
            boolean isDeleted = userRepository.delete(foundUser.getId());
            assertTrue(isDeleted, "Xóa user test phải thành công!");

            User deletedUser = userRepository.findByID(foundUser.getId());
            assertNull(deletedUser, "User sau khi xóa tìm lại phải trả về null!");
            System.out.println("✅ Test delete() User");
        });
    }
}
