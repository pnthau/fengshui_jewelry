# 🌸 Phong Thủy Jewelry - Web Application

Dự án website bán trang sức phong thủy dành cho người trung niên và lớn tuổi, được xây dựng trên mô hình MVC chuẩn bằng Java Servlet, JSP và JDBC thuần.

---

## 🛠️ Công Nghệ & Môi Trường Phát Triển (Tech Stack)

Để dự án chạy đồng bộ trên máy của tất cả thành viên, vui lòng cài đặt các môi trường theo đúng thông số cấu hình dưới đây:

| Thành phần | Phiên bản (Version) | Chi tiết |
| :--- | :--- | :--- |
| **☕ Java JDK** | **Java 17** | Khuyên dùng Amazon Corretto 17 hoặc Microsoft OpenJDK 17. |
| **🏛️ Jakarta EE** | **Jakarta EE 10** | Toàn bộ dự án sử dụng gói thư viện mới `jakarta.*` (Không dùng `javax.*`). |
| **🌐 Servlet API** | **Servlet 6.0** | Định nghĩa trong `web.xml` và dependency `jakarta.servlet-api:6.0.0`. |
| **⚡ JSTL** | **JSTL 3.0** | Dùng API `3.0.0` và Glassfish Implementation `3.0.1` để chạy JSTL trên Tomcat 10. |
| **🐯 Application Server** | **Tomcat 10.1.x** | **Bắt buộc dùng Tomcat 10.1.x** (Ví dụ: `10.1.55`). *Không dùng Tomcat 9 trở xuống.* |
| **🐘 Build Tool** | **Gradle 9.0.0** | Quản lý dự án và các thư viện dependencies. |

---

## 💾 Cấu Hình Cơ Sở Dữ Liệu (MySQL)

Dự án sử dụng MySQL để lưu trữ thông tin sản phẩm và đơn hàng. Dưới đây là thông số kết nối mặc định trong dự án:

* **Database Name:** `fengshui_db`
* **Port:** `3306`
* **Username:** `root`
* **Password:** `123456`

> [!NOTE]
> File script SQL tạo bảng và trigger nằm trong thư mục tài liệu chung của nhóm. Hãy đảm bảo bạn đã tạo database `fengshui_db` và import đầy đủ bảng trước khi khởi chạy server.

---

## 🚀 Hướng Dẫn Import & Thiết Lập Dự Án trên IntelliJ IDEA

### 1. Import Dự án
1. Mở IntelliJ IDEA.
2. Chọn **Open** (hoặc **Import Project**) ➔ Tìm và chọn file `build.gradle` (hoặc thư mục gốc `fengshui_jewelry`).
3. Chọn **Open as Project**.
4. Chờ vài phút để Gradle đồng bộ và tự động tải các thư viện về máy (MySQL Connector, Jakarta API, JSTL).

### 2. Cấu hình Server Tomcat 10.1.x
1. Nhấp vào menu cấu hình chạy ở góc trên bên phải ➔ Chọn **Edit Configurations...**
2. Nhấn nút **`+`** (Add New Configuration) ➔ Chọn **Tomcat Server** ➔ **Local**.
3. Tại mục **Application Server**, trỏ tới thư mục cài đặt **Tomcat 10.1.x** của bạn.
4. Chuyển sang tab **Deployment** ở phía dưới:
   * Nhấn nút **`+`** ➔ Chọn **Artifact...** ➔ Chọn **`fengshui-jewelry:war exploded`**.
   * Thiết lập **Application context** ở ô bên dưới là: `/fengshui_jewelry`.
5. Quay lại tab **Server**:
   * **On 'Update' action:** Chọn **`Update classes and resources`**.
   * **On frame deactivation:** Chọn **`Update resources`** *(Giúp tự động đồng bộ thay đổi JSP/CSS sang Tomcat ngay khi bạn chuyển tab sang trình duyệt)*.
6. Nhấn **OK** để lưu lại.

---

## 🛡️ Nguyên Tắc Làm Việc với Git (Git Hygiene)

* Dự án đã được cấu hình file `.gitignore` chuẩn hóa để loại bỏ các thư mục rác và cấu hình cá nhân.
* **Tuyệt đối KHÔNG** đẩy các thư mục sau lên GitHub:
  * Thư mục `.gradle/` và `build/`
  * Thư mục `out/` và `target/`
  * Thư mục cấu hình cá nhân `.idea/` và các file `*.iml`
* Trước khi commit code mới, hãy chạy thử server cục bộ trên máy của bạn để đảm bảo không phát sinh lỗi biên dịch (Compile Error).
