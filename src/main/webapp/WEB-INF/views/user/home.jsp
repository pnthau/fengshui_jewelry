<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Sức Phong Thủy - Khởi Đầu Tài Lộc & Bình An</title>
    <!-- Bootstrap 5 CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>

    <!-- NAV BAR -->
    <nav class="navbar navbar-expand-lg navbar-dark navbar-custom sticky-top" id="mainNavbar">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/home" id="navLogo">
                🌸 PHONG THỦY
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation" id="btnToggleNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/home" id="navLinkHome">Trang Chủ</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/products" id="navLinkProducts">Sản Phẩm</a>
                    </li>
                </ul>
                <div class="d-flex align-items-center">
                    <a href="tel:0905123456" class="btn btn-gold px-4" id="navCallBtn">
                        <i class="bi bi-telephone-fill me-2"></i> Tư Vấn: 0905.123.456
                    </a>
                </div>
            </div>
        </div>
    </nav>

    <!-- HERO HOME BANNER -->
    <header class="py-5 text-center text-white" style="background: radial-gradient(circle, #25292e 0%, #111315 100%); min-height: 80vh; display: flex; align-items: center;">
        <div class="container py-5">
            <div class="glass-panel p-5 max-width-800 mx-auto" style="border: 2px solid var(--accent-gold);">
                <span class="text-warning fw-bold text-uppercase tracking-wider mb-2 d-block" style="font-size: 1.2rem; letter-spacing: 2px;">Vật Phẩm Cát Tường Cao Cấp</span>
                <h1 class="display-4 fw-bold mb-4" id="homeHeroTitle" style="color: var(--accent-gold);">Vòng Tay & Trang Sức Phong Thủy</h1>
                
                <p class="lead text-secondary mb-5 mx-auto" style="font-size: 1.3rem; max-width: 650px;" id="homeHeroSub">
                    Được chế tác thủ công từ đá tự nhiên 100% kết hợp charm Vàng Non quý phái. Hỗ trợ tra cứu cung mệnh tự động và đặt hàng siêu tốc qua điện thoại.
                </p>
                
                <div class="row g-3 justify-content-center">
                    <div class="col-sm-6 col-md-5">
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-gold w-100 py-3 fs-5 fw-bold" id="btnExploreProducts">
                            <i class="bi bi-gem me-2"></i> Xem Sản Phẩm
                        </a>
                    </div>
                    <div class="col-sm-6 col-md-5">
                        <a href="tel:0905123456" class="btn btn-outline-light w-100 py-3 fs-5 fw-medium" style="border-radius: 8px;" id="btnCallNow">
                            <i class="bi bi-telephone me-2"></i> Gọi Điện Nghe Tư Vấn
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </header>

    <!-- VALUE PROPOSITIONS -->
    <section class="py-5" style="background-color: #16181b;">
        <div class="container py-4">
            <div class="row g-4 text-center">
                <div class="col-md-4">
                    <div class="p-4 rounded glass-panel h-100">
                        <i class="bi bi-shield-check text-warning display-4 mb-3 d-block"></i>
                        <h3 class="h4 mb-3 text-warning">Đá Tự Nhiên 100%</h3>
                        <p class="text-secondary" style="font-size: 1.1rem;">Cam kết bồi hoàn 200% nếu phát hiện đá giả, đá nhân tạo. Năng lượng dương thuần khiết từ tự nhiên giúp cải thiện sức khỏe, gia đạo bình an.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="p-4 rounded glass-panel h-100">
                        <i class="bi bi-mic-fill text-warning display-4 mb-3 d-block"></i>
                        <h3 class="h4 mb-3 text-warning">Tìm Kiếm Giọng Nói</h3>
                        <p class="text-secondary" style="font-size: 1.1rem;">Không cần bàn phím phức tạp! Người lớn tuổi chỉ cần chạm vào nút Micro và nói để tìm ngay sản phẩm mong muốn.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="p-4 rounded glass-panel h-100">
                        <i class="bi bi-telephone-outbound text-warning display-4 mb-3 d-block"></i>
                        <h3 class="h4 mb-3 text-warning">Đặt Hàng Siêu Tốc</h3>
                        <p class="text-secondary" style="font-size: 1.1rem;">Bỏ qua các bước thanh toán thẻ rườm rà. Chỉ cần nhập tên và số điện thoại, nhân viên chuyên nghiệp của chúng tôi sẽ gọi lại tư vấn chu đáo.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- FOOTER -->
    <footer class="text-center">
        <div class="container">
            <h5 class="text-warning mb-3">🌸 PHONG THỦY HẠU 🌸</h5>
            <p class="mb-2">Địa chỉ: 123 Hùng Vương, Hải Châu, Đà Nẵng</p>
            <p class="mb-3">Điện thoại hỗ trợ khách hàng: <strong>0905.123.456</strong> (Hỗ trợ 24/7)</p>
            <div class="text-secondary small">© 2026 Phong Thủy Hậu - Chữ to rõ ràng, sản phẩm chuẩn tự nhiên.</div>
        </div>
    </footer>

    <!-- Bootstrap 5 Bundle JS -->
    <script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>