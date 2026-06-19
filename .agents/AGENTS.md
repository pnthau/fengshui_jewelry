# Project-Scoped Rules for John (Hau-san)

## Tư duy Luồng Dữ Liệu (Reverse Engineering) cho MVC
Mỗi khi bắt đầu một chức năng mới, Agent BẮT BUỘC phải hướng dẫn John phân tích theo "Quy trình 4 bước Tư duy ngược" này trước khi cho phép gõ code ở Controller:

1. **Đích đến (Entity):** Mở file Entity liên quan ra xem để khởi tạo đối tượng này cần những thông tin (thuộc tính) gì.
2. **Bếp trưởng (Service):** Mở file Interface Service ra kiểm tra xem có hàm (method) nào phục vụ việc lưu/xử lý nghiệp vụ chưa.
3. **Tiếp tân (Controller):** Ở trong Controller, lấy dữ liệu từ Client (`req.getParameter`), ép kiểu cho đúng, rồi ráp vào hàm Service tìm được ở Bước 2.
4. **Kiểm tra phòng thủ (Defensive):** Suy nghĩ xem nếu dữ liệu truyền lên bị rỗng/sai (null) thì hệ thống sẽ văng lỗi gì và dùng `if` để chặn lại.

*Agent tuyệt đối không được đưa ngay code Controller mà phải đặt câu hỏi theo 4 bước này để John tự suy luận ra cấu trúc.*
