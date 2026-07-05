package com.fengshui.enums;

public enum OrderStatus {
    PENDING("Chờ xử lý"),
    DELIVERING("Đang giao"),
    DELIVERED("Đã giao"),
    CANCELLED("Đã hủy");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // Biến chuỗi String phù hợp thành Enum tương ứng
    public static OrderStatus fromString(String text) {
        if (text == null) return null;
        String trimmed = text.trim();
        for (OrderStatus status : OrderStatus.values()) {
            if (status.name().equalsIgnoreCase(trimmed) || status.value.equalsIgnoreCase(trimmed)) {
                return status;
            }
        }
        return null;
    }
}
