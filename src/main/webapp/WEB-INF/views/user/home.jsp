<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Sức Phong Thủy Hậu - Khởi Đầu Tài Lộc & Bình An</title>
    <!-- Bootstrap 5 CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <!-- Google Fonts - Serif sang trọng -->
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:ital,wght@0,400..900;1,400..900&family=Quicksand:wght@300..700&display=swap" rel="stylesheet">

    <style>
        :root {
            --gold-primary: #D4AF37;
            --gold-dark: #AA7C11;
            --bg-dark: #121212;
            --bg-card: #1E1E1E;
            --text-gold: #F3E5AB;
            --accent-gold: #D4AF37;
        }

        body {
            font-family: 'Quicksand', sans-serif;
            background-color: var(--bg-dark);
            color: #f8f9fa;
        }

        h1, h2, h3, h4, .brand-font {
            font-family: 'Playfair Display', serif;
            color: var(--text-gold);
        }

        /* Phong cách vàng kim loại lấp lánh */
        .text-gold-gradient {
            background: linear-gradient(135deg, #FFF9E6 0%, #D4AF37 50%, #AA7C11 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        /* Nút vàng cao cấp */
        .btn-gold {
            background: linear-gradient(135deg, #D4AF37 0%, #AA7C11 100%);
            border: none;
            color: #000;
            font-weight: bold;
            transition: all 0.3s ease;
        }

        .btn-gold:hover {
            background: linear-gradient(135deg, #FFF9E6 0%, #D4AF37 100%);
            transform: scale(1.02);
            color: #000;
        }

        /* Khu vực tra cứu mệnh */
        .calculator-section {
            background: linear-gradient(180deg, #181818 0%, #1a1508 100%);
            border-top: 2px solid var(--gold-dark);
            border-bottom: 2px solid var(--gold-dark);
        }

        .navbar-custom {
            background-color: rgba(18, 18, 18, 0.95);
            border-bottom: 1px solid #2a2a2a;
            backdrop-filter: blur(10px);
        }

        /* Thẻ kính sang trọng (Glassmorphism) */
        .glass-panel {
            background: rgba(30, 30, 30, 0.75);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(212, 175, 55, 0.2);
            border-radius: 12px;
            box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.37);
        }
    </style>
</head>
<body>

<!-- ==================== HEADER & NAVBAR ==================== -->
<nav class="navbar navbar-expand-lg navbar-dark navbar-custom sticky-top" id="mainNavbar">
    <div class="container">
        <a class="navbar-brand d-flex align-items-center" href="${pageContext.request.contextPath}/home" id="navLogo">
            <i class="bi bi-gem text-gold-gradient fs-3 me-2"></i>
            <span class="brand-font fs-3 fw-bold text-gold-gradient">PHONG THỦY HẠU</span>
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
                    <a class="nav-link" href="#calculator" id="navLinkCalc">Tra Bản Mệnh</a>
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

<!-- ==================== HERO HOME BANNER ==================== -->
<header class="py-5 text-center text-white" style="background: radial-gradient(circle, #25292e 0%, #111315 100%); min-height: 80vh; display: flex; align-items: center;">
    <div class="container py-5">
        <div class="glass-panel p-5 max-width-800 mx-auto" style="border: 2px solid var(--accent-gold); max-width: 800px;">
            <span class="text-warning fw-bold text-uppercase tracking-wider mb-2 d-block" style="font-size: 1.2rem; letter-spacing: 2px;">Vật Phẩm Cát Tường Cao Cấp</span>
            <h1 class="display-4 fw-bold mb-4 text-gold-gradient" id="homeHeroTitle">Vòng Tay & Trang Sức Phong Thủy</h1>

            <p class="lead text-secondary mb-5 mx-auto" style="font-size: 1.3rem; max-width: 650px;" id="homeHeroSub">
                Được chế tác thủ công từ đá tự nhiên 100% kết hợp charm Vàng Non quý phái. Hỗ trợ tra cứu cung mệnh tự động và đặt hàng siêu tốc qua điện thoại.
            </p>

            <div class="row g-3 justify-content-center">
                <div class="col-sm-6 col-md-5">
                    <a href="#calculator" class="btn btn-gold w-100 py-3 fs-5 fw-bold" id="btnExploreProducts">
                        <i class="bi bi-compass me-2"></i> Tra Cung Mệnh Ngay
                    </a>
                </div>
                <div class="col-sm-6 col-md-5">
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-light w-100 py-3 fs-5 fw-medium" style="border-radius: 8px;" id="btnCallNow">
                        <i class="bi bi-gem me-2"></i> Xem Cửa Hàng
                    </a>
                </div>
            </div>
        </div>
    </div>
</header>

<!-- ==================== KHU VỰC TÍNH BẢN MỆNH (BÁT TỰ KHAI MỆNH) ==================== -->
<section id="calculator" class="py-5 calculator-section">
    <div class="container">
        <div class="row align-items-center">
            <div class="col-lg-5 mb-4 mb-lg-0">
                <span class="text-uppercase tracking-wider text-warning fw-bold small">Công cụ độc quyền</span>
                <h2 class="h1 fw-bold mb-3">Bát Tự Khai Mệnh</h2>
                <p class="text-secondary mb-4">
                    Nhập chính xác ngày sinh dương lịch của bạn để thuật toán ngũ hành quy đổi cung mệnh thực tế. Hệ thống sẽ tự động tìm kiếm bộ sưu tập đá tương sinh phù hợp nhất với bản mệnh của bạn.
                </p>
                <div class="p-3 bg-dark rounded border border-warning border-opacity-25">
                    <h5 class="text-warning"><i class="bi bi-info-circle-fill me-2"></i>Vì sao cần hợp mệnh?</h5>
                    <p class="small text-secondary mb-0">
                        Đá phong thủy mang từ trường tự nhiên lớn giúp cân bằng sinh khí, hỗ trợ trừ tà khí, điều hòa tinh thần và mang lại sức khỏe vững chãi cho người lớn tuổi.
                    </p>
                </div>
            </div>

            <div class="col-lg-6 offset-lg-1">
                <div class="card bg-dark border border-secondary border-opacity-25 p-4 rounded-3 shadow">
                    <h3 class="h4 mb-4 text-center text-gold-gradient">Tra Cứu Cung Mệnh Ngũ Hành</h3>
                    <form id="calcForm" onsubmit="calculateElement(event)">
                        <div class="row g-3">
                            <div class="col-md-12">
                                <label class="form-label text-secondary small">Chọn Ngày/Tháng/Năm Sinh Dương Lịch</label>
                                <input type="date" id="birthDateInput" class="form-control bg-dark text-white border-secondary" required min="1940-01-01" max="2026-12-31">
                            </div>
                            <div class="col-12 mt-4">
                                <button type="submit" class="btn btn-gold w-full w-100 py-3">Tính Bản Mệnh & Xem BST Phù Hợp <i class="bi bi-magic ms-1"></i></button>
                            </div>
                        </div>
                    </form>

                    <!-- Kết quả tính mệnh -->
                    <div id="calcResult" class="mt-4 p-4 rounded bg-black bg-opacity-50 border border-secondary border-opacity-50 d-none">
                        <div class="d-flex align-items-center mb-3">
                            <div id="resultIcon" class="fs-1 me-3 text-warning">🔮</div>
                            <div>
                                <div class="text-secondary small">Kết quả cung mệnh:</div>
                                <h4 id="resultTitle" class="fw-bold text-white mb-0">MỆNH HOẢ</h4>
                            </div>
                        </div>
                        <p id="resultDesc" class="text-secondary small mb-3">
                            Màu sắc tương sinh tốt nhất: Xanh lục (Mộc sinh Hỏa).
                        </p>
                        <!-- Nút kích hoạt chuyển hướng sang Servlet Products -->
                        <button class="btn btn-sm btn-outline-warning w-100" onclick="redirectToCatalog()">Đến cửa hàng xem bộ sưu tập phù hợp <i class="bi bi-arrow-right-short"></i></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- ==================== VALUE PROPOSITIONS ==================== -->
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

<!-- ==================== FOOTER ==================== -->
<footer class="text-center py-5 bg-black border-top border-secondary border-opacity-25 mt-5">
    <div class="container">
        <h5 class="text-warning mb-3 brand-font fs-3 text-gold-gradient">🌸 PHONG THỦY HẠU 🌸</h5>
        <p class="mb-2 text-secondary">Địa chỉ: 123 Hùng Vương, Hải Châu, Đà Nẵng</p>
        <p class="mb-3 text-secondary">Điện thoại hỗ trợ khách hàng: <strong>0905.123.456</strong> (Hỗ trợ 24/7)</p>
        <div class="text-muted small">© 2026 Phong Thủy Hậu - Chữ to rõ ràng, sản phẩm chuẩn tự nhiên.</div>
    </div>
</footer>

<!-- Bootstrap 5 Bundle JS -->
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>

<script>
    let calculatedUserElement = "";

    // Thuật toán tính bản mệnh theo Can Chi dựa vào năm sinh dương lịch
    function calculateElement(event) {
        event.preventDefault();
        const birthDateValue = document.getElementById("birthDateInput").value;
        if (!birthDateValue) return;

        const date = new Date(birthDateValue);
        const year = date.getFullYear();

        // 1. Tính Thiên Can (Số cuối cùng của năm sinh)
        const canValueMap = { 4: 1, 5: 1, 6: 2, 7: 2, 8: 3, 9: 3, 0: 4, 1: 4, 2: 5, 3: 5 };
        const lastDigit = year % 10;
        const canScore = canValueMap[lastDigit] || 1;

        // 2. Tính Địa Chi (Năm sinh chia 12 lấy dư)
        const chiIndex = (year - 4) % 12;
        let chiScore = 0;
        if ([2, 3, 8, 9].includes(chiIndex)) {
            chiScore = 1;
        } else if ([4, 5, 10, 11].includes(chiIndex)) {
            chiScore = 2;
        }

        // 3. Tính Tổng điểm Ngũ Hành
        let totalScore = canScore + chiScore;
        if (totalScore > 5) {
            totalScore -= 5;
        }

        const elementsArray = ["", "Kim", "Thủy", "Hỏa", "Thổ", "Mộc"];
        const resultElement = elementsArray[totalScore] || "Mộc";

        const elementsDetail = {
            "Kim": { title: "MỆNH KIM - CHỮA LÀNH", desc: "Màu sắc tương sinh tốt nhất là Vàng Sẫm, Nâu Đất (Thổ sinh Kim). Bạn phù hợp với Thạch Anh Trắng, Thạch Anh Vàng.", icon: "🪙" },
            "Mộc": { title: "MỆNH MỘC - SỨC SỐNG", desc: "Màu sắc tương sinh tốt nhất là Đen, Xanh lam (Thủy sinh Mộc). Bạn cực kỳ phù hợp với đá Cẩm Thạch, Ngọc Bích.", icon: "🌳" },
            "Thủy": { title: "MỆNH THỦY - TRÍ TUỆ", desc: "Màu sắc tương sinh tốt nhất là Trắng, Ánh Kim (Kim sinh Thủy). Bạn hợp với Thạch Anh Đen, Thạch Anh Trắng.", icon: "💧" },
            "Hỏa": { title: "MỆNH HỎA - NHIỆT HUYẾT", desc: "Màu sắc tương sinh tốt nhất là Xanh lục (Mộc sinh Hỏa). Bạn hợp với Thạch Anh Tóc Đỏ, Ruby tự nhiên.", icon: "🔥" },
            "Thổ": { title: "MỆNH THỔ - VỮNG CHÃI", desc: "Màu sắc tương sinh tốt nhất là Đỏ, Hồng, Tím (Hỏa sinh Thổ). Bạn hợp với đá Mắt Hổ, Thạch Anh Đào Hoa.", icon: "⛰️" }
        };

        calculatedUserElement = resultElement;
        const detail = elementsDetail[resultElement];

        document.getElementById("resultTitle").innerText = detail.title;
        document.getElementById("resultDesc").innerText = detail.desc;
        document.getElementById("resultIcon").innerText = detail.icon;

        const resultBox = document.getElementById("calcResult");
        resultBox.classList.remove("d-none");
        resultBox.scrollIntoView({ behavior: 'smooth' });
    }

    // Chuyển hướng thông minh sang Servlet /products kèm tham số mệnh tương thích
    function redirectToCatalog() {
        if (!calculatedUserElement) return;
        const contextPath = "${pageContext.request.contextPath}";

        // Chuẩn hóa chuỗi tham số sang không dấu, viết hoa (KIM, MOC, THUY, HOA, THO) để khớp với bộ lọc product_list
        const elementParam = calculatedUserElement.toUpperCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "").replace(/Đ/g, "D");
        window.location.href = contextPath + "/products?element=" + encodeURIComponent(elementParam);
    }
</script>
</body>
</html>