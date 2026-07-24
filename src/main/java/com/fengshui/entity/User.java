package com.fengshui.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fengshui.enums.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    private String username;
    private String password;
    private String role;  // Giữ role là String để tương thích với DB, sử dụng UserRole.fromString() khi cần

    /**
     * Lấy role dưới dạng UserRole enum
     */
    public UserRole getUserRole() {
        return UserRole.fromString(this.role);
    }

    /**
     * Set role từ UserRole enum
     */
    public void setUserRole(UserRole userRole) {
        this.role = userRole.getValue();
    }
}

