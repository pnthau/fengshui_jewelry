package com.fengshui.enums;

/**
 * Enum định nghĩa các vai trò (role) trong hệ thống
 * - ADMIN: Toàn quyền quản lý toàn hệ thống
 * - SALES: Xem & xác nhận đơn hàng, không có quyền xuất nhập kho
 * - WAREHOUSE: Quản lý xuất/nhập kho, không quản lý sản phẩm
 * - ACCOUNTANT: Xem báo cáo doanh thu, không được chỉnh sửa
 */
public enum UserRole {
    ADMIN("admin"),
    SALES("sales"),
    WAREHOUSE("warehouse"),
    ACCOUNTANT("accountant");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Chuyển đổi từ string sang UserRole enum
     * @param value Giá trị string (admin, sales, warehouse, accountant)
     * @return UserRole enum tương ứng, mặc định là SALES nếu không tìm thấy
     */
    public static UserRole fromString(String value) {
        if (value == null) {
            return SALES; // Mặc định là SALES nếu role không được chỉ định
        }
        for (UserRole role : UserRole.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        return SALES; // Nếu không tìm thấy, trả về SALES
    }
}
