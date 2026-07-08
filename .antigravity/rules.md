## Project Context — fengshui_jewelry
## 1. Project Overview
- fengshui_jewelry: hệ thống trang web bán hàng và quản lý bán hàng
- Mục đích: quản lý đơn hàng, kho hàng, khác hàng — tối ưu việc bán hàng và
mua hàng cho các sản phẩm phong thủy
- Ưu tiên: code sạch, dễ mở rộng, hướng dẫn đưa ra các bước giúp người học tự lập trình được
## 2. Project Structure
- Package gốc: com.fengshui_jewelry
- entity/ → java core Entities: InventoryTransaction, Order, OrderItem,Product, User
-repository/ →  java core repository 
-service/ + service/impl/ → Interface và implementation business logic
-controller/ → Servlets
## 3. Coding Conventions & Standards
- Stack: Java 17, Lombok, Tomcat,Servlet API, JSP
- Naming: camelCase cho field/method, PascalCase cho class
- DB: MySQL, table names số nhiều (users, products, orders)
- Dùng @Builder, @Data, @NoArgsConstructor, @AllArgsConstructor từ Lombok
- Tất cả entities extend BaseEntity (id, createdAt, updatedAt)
## 4. Architecture Patterns
- Pattern bắt buộc: Repository → Service → ServiceImpl → Controller
Không trả Entity trực tiếp ra Controller 
## 5. Response Style
- Trả về code Java hoàn chỉnh, copy paste được ngay
- Không thêm dependency ngoài build.gradle hiện tại
- Review code: dùng format có cấu trúc, không viết đoạn văn dài
## 6. Workflows & Modes
- Task (feature mới, refactor nhiều file): đưa Implementation Plan trước, chờ xác nhận mới code và giải thích
- Trước khi sửa file: đọc qua @file để hiểu context hiện tại, không suy đoán nội dung
