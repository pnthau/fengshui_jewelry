package com.fengshui.service;

import com.fengshui.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserService userService;
    private int testUserId = 0;

    @BeforeEach
    public void setUp() {
        userService = new UserService();
    }

    @AfterEach
    public void tearDown() {
        if (testUserId > 0) {
            userService.delete(testUserId);
            testUserId = 0;
        }
    }

    @Test
    public void testSaveAndFindByID() {
        User user = new User();
        user.setUsername("testadmin");
        user.setPassword("testpwd123");
        user.setRole("ADMIN");

        boolean saved = userService.save(user);
        assertTrue(saved, "Phải lưu user thành công");
        assertTrue(user.getId() > 0, "Id user sau khi lưu phải lớn hơn 0");
        testUserId = user.getId();

        User found = userService.findByID(testUserId);
        assertNotNull(found, "Phải tìm thấy user bằng ID");
        assertEquals("testadmin", found.getUsername());
        assertEquals("testpwd123", found.getPassword());
        assertEquals("ADMIN", found.getRole());
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setUsername("updateuser");
        user.setPassword("pwd123");
        user.setRole("ADMIN");

        userService.save(user);
        testUserId = user.getId();

        user.setPassword("newpwd456");
        boolean updated = userService.update(user);
        assertTrue(updated, "Phải cập nhật thông tin user thành công");

        User found = userService.findByID(testUserId);
        assertEquals("newpwd456", found.getPassword(), "Mật khẩu cập nhật phải trùng khớp");
    }

    @Test
    public void testLoginSuccessAndFailure() {
        User user = new User();
        user.setUsername("loginuser");
        user.setPassword("securepass");
        user.setRole("ADMIN");

        userService.save(user);
        testUserId = user.getId();

        // Thử đăng nhập đúng
        User loggedIn = userService.login("loginuser", "securepass");
        assertNotNull(loggedIn, "Đăng nhập với tài khoản đúng phải thành công");
        assertEquals(testUserId, loggedIn.getId());

        // Thử đăng nhập sai mật khẩu
        User wrongPassword = userService.login("loginuser", "wrongpassword");
        assertNull(wrongPassword, "Đăng nhập sai mật khẩu phải thất bại (trả về null)");

        // Thử đăng nhập tài khoản không tồn tại
        User nonExistent = userService.login("nonexistentuser", "somepassword");
        assertNull(nonExistent, "Đăng nhập tài khoản không tồn tại phải thất bại (trả về null)");
    }
}
