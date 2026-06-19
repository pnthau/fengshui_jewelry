<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Sức Phong Thủy Hậu - Danh Sách Sản Phẩm Hộ Mệnh</title>
    <!-- Bootstrap 5 CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
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

        /* === CẢI THIỆN ĐỘ TƯƠNG PHẢN CHO INPUT (TRÁNH BỊ TỐI) === */
        .form-control {
            background-color: #2c3036 !important; /* Tăng độ sáng nền từ ngăm đen lên xám sáng */
            border: 1.5px solid #6c757d !important; /* Làm rõ viền xám mặc định */
            color: #ffffff !important;
            font-size: 1.1rem; /* Chữ to rõ ràng hơn cho người lớn tuổi */
            font-weight: 500;
        }

        .form-control::placeholder {
            color: #adb5bd !important; /* Giúp chữ gợi ý sáng và rõ hơn */
            opacity: 0.85;
        }

        .form-control:focus {
            background-color: #343a40 !important;
            border-color: var(--gold-primary) !important;
            box-shadow: 0 0 0 0.25rem rgba(212, 175, 55, 0.25) !important;
            color: #ffffff !important;
        }

        /* Thẻ sản phẩm */
        .product-card {
            background-color: var(--bg-card);
            border: 1px solid #2a2a2a;
            border-radius: 12px;
            overflow: hidden;
            transition: all 0.3s ease;
            height: 100%;
            display: flex;
            flex-direction: column;
        }

        .product-card:hover {
            transform: translateY(-5px);
            border-color: var(--gold-primary);
            box-shadow: 0 8px 24px rgba(212, 175, 55, 0.15);
        }

        .product-img-wrapper {
            position: relative;
            height: 220px;
            overflow: hidden;
            background-color: #151515;
        }

        .product-img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.5s ease;
        }

        .product-card:hover .product-img {
            transform: scale(1.1);
        }

        .product-body {
            padding: 20px;
            display: flex;
            flex-direction: column;
            flex-grow: 1;
        }

        .product-title {
            font-family: 'Playfair Display', serif;
            color: #fff;
            font-size: 1.25rem;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .product-price {
            color: var(--gold-primary);
            font-weight: bold;
            font-size: 1.2rem;
        }

        /* Huy hiệu hệ mệnh phong thủy */
        .badge-element {
            display: inline-block;
            padding: 4px 10px;
            border-radius: 20px;
            font-weight: bold;
            font-size: 0.75rem;
            margin-right: 5px;
            margin-bottom: 5px;
            text-transform: uppercase;
        }

        .badge-kim { background-color: #e9ecef; color: #212529; border: 1px solid #ced4da; }
        .badge-moc { background-color: #198754; color: #ffffff; }
        .badge-thuy { background-color: #0dcaf0; color: #212529; }
        .badge-hoa { background-color: #dc3545; color: #ffffff; }
        .badge-tho { background-color: #fd7e14; color: #ffffff; }

        /* Hiệu ứng nhấp nháy cho nút giọng nói */
        .voice-search-btn {
            background-color: #2a2a2a;
            color: #fff;
            border: 1px solid #3d3d3d;
            transition: all 0.3s ease;
        }

        .voice-search-btn:hover {
            color: var(--gold-primary);
            border-color: var(--gold-primary);
        }

        .voice-recording {
            background-color: #dc3545 !important;
            color: white !important;
            animation: pulse-red 1.5s infinite;
            border-color: #dc3545 !important;
        }

        @keyframes pulse-red {
            0% { transform: scale(1); box-shadow: 0 0 0 0 rgba(220, 53, 69, 0.7); }
            70% { transform: scale(1.05); box-shadow: 0 0 0 10px rgba(220, 53, 69, 0); }
            100% { transform: scale(1); box-shadow: 0 0 0 0 rgba(220, 53, 69, 0); }
        }

        /* Bộ lọc Cung Mệnh dạng Tab */
        .btn-element {
            background-color: #1a1a1a;
            color: #a0a0a0;
            border: 1px solid #2d2d2d;
            border-radius: 8px;
            padding: 8px 16px;
            font-weight: 500;
            transition: all 0.2s ease;
        }

        .btn-element:hover {
            border-color: var(--gold-primary);
            color: var(--text-gold);
        }

        .btn-element.active {
            background: linear-gradient(135deg, #D4AF37 0%, #AA7C11 100%);
            color: #000 !important;
            font-weight: bold;
            border-color: var(--gold-primary);
        }
    </style>
</head>
<body>

<!-- ==================== HEADER & NAVBAR ==================== -->
<nav class="navbar navbar-expand-lg navbar-dark navbar-custom sticky-top">
    <div class="container">
        <a class="navbar-brand d-flex align-items-center" href="${pageContext.request.contextPath}/home">
            <i class="bi bi-gem text-gold-gradient fs-3 me-2"></i>
            <span class="brand-font fs-3 fw-bold text-gold-gradient">PHONG THỦY HẠU</span>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">Trang Chủ</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/products">Sản Phẩm</a>
                </li>
            </ul>
            <a href="tel:0905123456" class="btn btn-gold px-4" id="navCallBtn">
                <i class="bi bi-telephone-fill me-2"></i> Tư Vấn: 0905.123.456
            </a>
        </div>
    </div>
</nav>

<!-- ==================== MAIN CONTENT AREA ==================== -->
<main class="container my-5">
    <div class="row g-4">

        <!-- SIDEBAR: TÌM KIẾM & TRA CỨU MỆNH (Bên trái, col-lg-4) -->
        <div class="col-lg-4">
            <div class="glass-panel p-4 mb-4">
                <h3 class="h4 mb-3 text-gold-gradient"><i class="bi bi-search me-2"></i>Tìm Kiếm</h3>

                <!-- Form Tìm kiếm -->
                <form action="${pageContext.request.contextPath}/products" method="GET" id="search-form" class="mb-4">
                    <div class="input-group">
                        <input type="text" name="keyword" id="search-input"
                               class="form-control"
                               placeholder="Nhập tên sản phẩm..."
                               value="${keyword}">

                        <!-- Nút micro tìm kiếm giọng nói -->
                        <button class="btn voice-search-btn px-3" type="button" id="voice-btn"
                                title="Tìm bằng giọng nói">
                            <i class="bi bi-mic-fill"></i>
                        </button>

                        <button class="btn btn-gold px-3" type="submit">
                            <i class="bi bi-search"></i>
                        </button>
                    </div>
                </form>

                <hr class="border-secondary my-4">

                <!-- Widget Tra Cứu Mệnh theo Năm Sinh (Giống giao diện Trang Chủ) -->
                <div class="card bg-black bg-opacity-40 border border-secondary border-opacity-25 p-4 text-center">
                    <h4 class="h5 text-warning mb-3"><i class="bi bi-compass-fill me-2"></i>Tra Cứu Mệnh</h4>
                    <div class="row g-2 justify-content-center mb-3">
                        <div class="col-8">
                            <input type="number" id="birthYearInput" class="form-control text-center fw-bold"
                                   placeholder="Ví dụ: 1968" min="1930" max="2030">
                        </div>
                        <div class="col-4">
                            <button type="button" class="btn btn-gold w-100" onclick="calculateElement()">Tra cứu</button>
                        </div>
                    </div>

                    <!-- Giao diện kết quả tra mệnh trực quan, sinh động giống hệt trang chủ -->
                    <div id="element-result" class="mt-4 p-3 rounded text-start d-none"
                         style="background: rgba(0,0,0,0.6); border: 1px dashed var(--accent-gold);">
                        <div class="d-flex align-items-center mb-3">
                            <div id="resultIcon" class="fs-1 me-3 text-warning">🔮</div>
                            <div>
                                <div class="text-secondary small" style="font-size: 0.75rem;">Kết quả cung mệnh:</div>
                                <h5 id="resultTitle" class="fw-bold text-white mb-0" style="font-size: 0.95rem;">MỆNH HOẢ</h5>
                            </div>
                        </div>
                        <p id="resultDesc" class="text-secondary mb-3" style="font-size: 0.8rem; line-height: 1.5; color: #ced4da !important;">
                            Màu sắc tương sinh tốt nhất: Xanh lục (Mộc sinh Hỏa).
                        </p>
                        <button type="button" class="btn btn-sm btn-outline-warning w-100" style="font-size: 0.8rem;" onclick="filterByResult()">
                            Đến cửa hàng xem bộ sưu tập phù hợp <i class="bi bi-arrow-right-short"></i>
                        </button>
                    </div>

                    <!-- Trường hợp load lại trang có tham số JSP từ Server -->
                    <c:if test="${not empty year}">
                        <div id="server-result" class="mt-4 p-3 rounded text-start"
                             style="background: rgba(0,0,0,0.5); border: 1px dashed var(--accent-gold);">
                            <div class="mb-2 text-white" style="font-size: 0.85rem;">
                                Năm sinh <strong>${year}</strong> hợp:
                                <span class="badge-element badge-${fn:toLowerCase(selectedElement)}">Mệnh ${selectedElement}</span>
                            </div>
                            <small class="text-secondary d-block" style="font-size: 0.75rem;">
                                Hệ thống đang lọc các vật phẩm phong thủy tương sinh, tương hợp tốt nhất cho mệnh của bạn.
                            </small>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>

        <!-- DANH SÁCH SẢN PHẨM & BỘ LỌC MỆNH (Bên phải, col-lg-8) -->
        <div class="col-lg-8">
            <!-- Bộ lọc Cung Mệnh dạng Tab lớn -->
            <div class="glass-panel p-4 mb-4">
                <h3 class="h4 mb-4 text-gold-gradient"><i class="bi bi-funnel-fill me-2"></i>Lọc Theo Cung Mệnh</h3>
                <div class="d-flex flex-wrap gap-2">
                    <a href="${pageContext.request.contextPath}/products"
                       class="btn btn-element ${empty selectedElement ? 'active' : ''}">Tất Cả</a>
                    <a href="${pageContext.request.contextPath}/products?element=KIM"
                       class="btn btn-element ${selectedElement == 'KIM' ? 'active' : ''}">Mệnh Kim</a>
                    <a href="${pageContext.request.contextPath}/products?element=MOC"
                       class="btn btn-element ${selectedElement == 'MOC' ? 'active' : ''}">Mệnh Mộc</a>
                    <a href="${pageContext.request.contextPath}/products?element=THUY"
                       class="btn btn-element ${selectedElement == 'THUY' ? 'active' : ''}">Mệnh Thủy</a>
                    <a href="${pageContext.request.contextPath}/products?element=HOA"
                       class="btn btn-element ${selectedElement == 'HOA' ? 'active' : ''}">Mệnh Hỏa</a>
                    <a href="${pageContext.request.contextPath}/products?element=THO"
                       class="btn btn-element ${selectedElement == 'THO' ? 'active' : ''}">Mệnh Thổ</a>
                </div>
            </div>

            <!-- Grid Danh sách sản phẩm -->
            <c:choose>
                <c:when test="${empty products}">
                    <div class="glass-panel p-5 text-center my-4">
                        <i class="bi bi-box2-open text-secondary display-3 mb-3"></i>
                        <p class="text-secondary fs-5 mb-0">Không tìm thấy sản phẩm phong thủy nào phù hợp.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="text-secondary mb-3 px-1" style="font-size: 0.9rem;">
                        <i class="bi bi-info-circle me-1 text-warning"></i> Đang hiển thị ${fn:length(products)} vật phẩm cát tường cao cấp:
                    </p>

                    <div class="row row-cols-1 row-cols-md-2 row-cols-xl-3 g-3" id="productsGrid">
                        <c:forEach var="product" items="${products}">
                            <div class="col">
                                <div class="product-card">
                                    <div class="product-img-wrapper">
                                        <img src="${pageContext.request.contextPath}/${product.imageURL}"
                                             class="product-img"
                                             alt="${product.name}"
                                             onerror="this.src='https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=600&auto=format&fit=crop'">
                                    </div>
                                    <div class="product-body">
                                        <div class="mb-2">
                                            <c:forEach var="elem" items="${product.elements}">
                                                <span class="badge-element badge-${fn:toLowerCase(elem)}">Mệnh ${elem}</span>
                                            </c:forEach>
                                        </div>
                                        <h4 class="product-title">${product.name}</h4>
                                        <p class="text-secondary small mb-2"><i class="bi bi-gem me-1 text-warning"></i> Chất liệu: ${product.material}</p>

                                        <div class="d-flex justify-content-between align-items-center mb-3 mt-auto">
                                             <span class="product-price">
                                                 <fmt:formatNumber value="${product.price}" type="number" maxFractionDigits="0"/> VNĐ
                                             </span>
                                            <c:choose>
                                                <c:when test="${product.quantity > 0}">
                                                    <span class="badge bg-success-subtle text-success border border-success border-opacity-25 py-1 px-2">Còn hàng</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-danger-subtle text-danger border border-danger border-opacity-25 py-1 px-2">Hết hàng</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <div class="row g-2">
                                            <div class="col-6">
                                                <a href="${pageContext.request.contextPath}/product-detail?id=${product.id}"
                                                   class="btn btn-outline-secondary w-100 py-2 btn-sm">Chi tiết</a>
                                            </div>
                                            <div class="col-6">
                                                <!-- Nút Mua nhanh mở Form thu thập thông tin siêu tốc -->
                                                <button type="button" class="btn btn-gold w-100 py-2 btn-sm btn-quick-order"
                                                        onclick="openQuickOrderModal('${product.id}', '${fn:escapeXml(product.name)}', ${product.price})">
                                                    <i class="bi bi-telephone-outbound me-1"></i> Mua nhanh
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div> <!-- Đóng div.col-lg-8 -->

    </div> <!-- Đóng div.row -->
</main>

<!-- ==================== MODAL MUA NHANH SIÊU TỐC (TASK 18) ==================== -->
<div class="modal fade" id="quickOrderModal" tabindex="-1" aria-labelledby="quickOrderTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content bg-dark border border-warning border-opacity-50 text-white">
            <div class="modal-header border-secondary border-opacity-25">
                <h5 class="modal-title text-gold-gradient fw-bold" id="quickOrderTitle"><i class="bi bi-telephone-outbound me-2"></i>Đặt Hàng Nhanh Hộ Mệnh</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form id="quickOrderForm" onsubmit="submitQuickOrder(event)">
                <div class="modal-body">
                    <div class="p-3 bg-black bg-opacity-30 rounded border border-secondary border-opacity-25 mb-4">
                        <label class="text-secondary small d-block">Vật phẩm cát tường đã chọn:</label>
                        <h5 class="text-white fw-bold mb-1" id="quickProductTitle">Tên sản phẩm phong thủy</h5>
                        <h4 class="text-warning fw-bold mb-0" id="quickProductPrice">0 VNĐ</h4>
                    </div>

                    <!-- Trường Honeypot ẩn bảo vệ hệ thống khỏi Bot spam -->
                    <div class="d-none" style="display: none !important;">
                        <input type="text" id="email_validate" name="email_validate" tabindex="-1" autocomplete="off">
                    </div>

                    <div class="mb-3">
                        <label for="customerName" class="form-label text-secondary small">Họ và Tên Của Ông/Bà <span class="text-danger">*</span></label>
                        <input type="text" class="form-control bg-dark text-white border-secondary py-2" id="customerName" required placeholder="Ví dụ: Nguyễn Văn An">
                    </div>
                    <div class="mb-3">
                        <label for="customerPhone" class="form-label text-secondary small">Số Điện Thoại Nhận Cuộc Gọi Tư Vấn <span class="text-danger">*</span></label>
                        <input type="tel" class="form-control bg-dark text-white border-secondary py-2" id="customerPhone" required placeholder="Ví dụ: 0905123456" pattern="^0[35789]\d{8}$">
                        <div class="form-text text-muted">Hệ thống sẽ gọi lại cho bạn trong vòng 10 phút để xác nhận và đóng gói.</div>
                    </div>
                    <div class="mb-3">
                        <label for="customerAddress" class="form-label text-secondary small">Địa Chỉ Giao Hàng <span class="text-danger">*</span></label>
                        <textarea class="form-control bg-dark text-white border-secondary" id="customerAddress" rows="2" required placeholder="Nhập địa chỉ nhà nhận hàng phong thủy..."></textarea>
                    </div>

                    <input type="hidden" id="quickProductId">
                    <input type="hidden" id="rawProductPrice">
                </div>
                <div class="modal-footer border-secondary border-opacity-25 d-flex justify-content-between">
                    <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">Đóng</button>
                    <button type="submit" class="btn btn-gold px-4 py-2">Xác Nhận Đặt Mua <i class="bi bi-arrow-right-short"></i></button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- ==================== FOOTER ==================== -->
<footer class="text-center py-5 bg-black border-top border-secondary border-opacity-25 mt-5">
    <div class="container">
        <h5 class="text-warning mb-3 brand-font fs-3 text-gold-gradient">🌸 PHONG THỦY HẠU 🌸</h5>
        <p class="mb-2 text-secondary">Địa chỉ: 123 Hùng Vương, Hải Châu, Đà Nẵng</p>
        <p class="mb-3 text-secondary">Điện thoại hỗ trợ khách hàng: <strong>0905.123.456</strong> (Hỗ trợ 24/7)</p>
        <div class="text-muted small">© 2026 Phong Thủy Hậu - Chữ to rõ ràng, sản phẩm chuẩn tự nhiên.</div>
    </div>
</footer>

<script src="${pageContext.request.contextPath}/assets/js/fengshui-utils.js"></script>
<!-- Bootstrap 5 Bundle JS -->
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>

<script>
    // Module Voice Search nhận diện giọng nói tiếng Việt
    const VoiceSearchModule = (function () {
        const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
        let recognition = null;
        let isListening = false;

        function initialize(config) {
            if (!SpeechRecognition) {
                console.warn("Trình duyệt không hỗ trợ Web Speech API.");
                if (config.voiceBtn) config.voiceBtn.style.display = 'none';
                return;
            }

            recognition = new SpeechRecognition();
            recognition.lang = 'vi-VN';
            recognition.continuous = false;
            recognition.interimResults = false;

            recognition.onstart = function () {
                isListening = true;
                config.onStart();
            };

            recognition.onresult = function (event) {
                const text = event.results[0][0].transcript;
                config.onResult(text);
            };

            recognition.onend = function () {
                isListening = false;
                config.onEnd();
            };

            recognition.onerror = function (event) {
                config.onError(event.error);
            };

            config.voiceBtn.addEventListener('click', function () {
                if (isListening) {
                    recognition.stop();
                } else {
                    recognition.start();
                }
            });
        }

        return {
            init: initialize
        };
    })();

    const voiceBtn = document.getElementById('voice-btn');
    const searchInput = document.getElementById("search-input");
    const searchForm = document.getElementById("search-form");

    VoiceSearchModule.init({
        voiceBtn: voiceBtn,
        onStart: function () {
            voiceBtn.classList.add('voice-recording');
        },
        onEnd: function () {
            voiceBtn.classList.remove('voice-recording');
        },
        onError: function (err) {
            console.error("Lỗi ghi âm giọng nói:", err);
        },
        onResult: function (text) {
            const cleanText = text.trim().replace(/[.,?!]+$/, "");
            console.log("Kết quả nhận dạng giọng nói:", cleanText);
            searchInput.value = cleanText;
            searchForm.submit();
        }
    });

    // Biến lưu kết quả mệnh được tính toán tạm thời ở Client
    let calculatedUserElement = "";

    // Tính Mệnh theo năm sinh sử dụng thuật toán Can Chi đồng bộ với trang chủ
    function calculateElement() {
        const yearInput = document.getElementById('birthYearInput');
        const year = parseInt(yearInput.value);
        const resultDiv = document.getElementById('element-result');
        const serverResultDiv = document.getElementById('server-result');

        if (isNaN(year) || year < 1930 || year > 2030) {
            alert("Vui lòng nhập năm sinh hợp lệ (1930 - 2030)!");
            return;
        }

        // Tự tính mệnh thông qua can chi
        const canValueMap = { 4: 1, 5: 1, 6: 2, 7: 2, 8: 3, 9: 3, 0: 4, 1: 4, 2: 5, 3: 5 };
        const lastDigit = year % 10;
        const canScore = canValueMap[lastDigit] || 1;

        const chiIndex = (year - 4) % 12;
        let chiScore = 0;
        if ([2, 3, 8, 9].includes(chiIndex)) {
            chiScore = 1;
        } else if ([4, 5, 10, 11].includes(chiIndex)) {
            chiScore = 2;
        }

        let totalScore = canScore + chiScore;
        if (totalScore > 5) {
            totalScore -= 5;
        }

        const elementsArray = ["", "Kim", "Thủy", "Hỏa", "Thổ", "Mộc"];
        const resultElement = elementsArray[totalScore] || "Mộc";

        const elementsDetail = {
            "Kim": { title: "MỆNH KIM - CHỮA LÀNH", desc: "Màu sắc tương sinh tốt nhất là Vàng Sẫm, Nâu Đất (Thổ sinh Kim). Bạn hợp với Thạch Anh Trắng, Thạch Anh Vàng.", icon: "🪙" },
            "Mộc": { title: "MỆNH MỘC - SỨC SỐNG", desc: "Màu sắc tương sinh tốt nhất là Đen, Xanh lam (Thủy sinh Mộc). Bạn cực kỳ hợp với đá Cẩm Thạch, Ngọc Bích.", icon: "🌳" },
            "Thủy": { title: "MỆNH THỦY - TRÍ TUỆ", desc: "Màu sắc tương sinh tốt nhất là Trắng, Ánh Kim (Kim sinh Thủy). Bạn hợp với Thạch Anh Đen, Thạch Anh Trắng.", icon: "💧" },
            "Hỏa": { title: "MỆNH HỎA - NHIỆT HUYẾT", desc: "Màu sắc tương sinh tốt nhất là Xanh lục (Mộc sinh Hỏa). Bạn hợp với Thạch Anh Tóc Đỏ, Ruby tự nhiên.", icon: "🔥" },
            "Thổ": { title: "MỆNH THỔ - VỮNG CHÃI", desc: "Màu sắc tương sinh tốt nhất là Đỏ, Hồng, Tím (Hỏa sinh Thổ). Bạn hợp với đá Mắt Hổ, Thạch Anh Đào Hoa.", icon: "⛰️" }
        };

        calculatedUserElement = resultElement;
        const detail = elementsDetail[resultElement];

        // Ẩn khối kết quả cũ của server (nếu có) để tránh loạn thông tin
        if (serverResultDiv) {
            serverResultDiv.classList.add('d-none');
        }

        // Đổ thông tin chi tiết vào khối kết quả client giống như trang chủ
        document.getElementById("resultTitle").innerText = detail.title;
        document.getElementById("resultDesc").innerText = detail.desc;
        document.getElementById("resultIcon").innerText = detail.icon;

        resultDiv.classList.remove('d-none');
    }

    // Chuyển hướng / Lọc sản phẩm theo mệnh vừa tính toán được
    function filterByResult() {
        if (!calculatedUserElement) return;
        const contextPath = "${pageContext.request.contextPath}";

        // Chuẩn hóa tên mệnh sang chuỗi Latin viết hoa không dấu để gửi Servlet
        const elementParam = calculatedUserElement.toUpperCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "").replace(/Đ/g, "D");
        const yearInput = document.getElementById('birthYearInput');
        const year = parseInt(yearInput.value);

        // Chuyển hướng để server render danh sách sản phẩm động từ MySQL
        window.location.href = contextPath + "/products?element=" + encodeURIComponent(elementParam) + "&year=" + encodeURIComponent(year);
    }

    // --- LOGIC MUA NHANH SIÊU TỐC HỖ TRỢ NGƯỜI LỚN TUỔI ---
    let quickOrderModalInstance = null;

    function openQuickOrderModal(productId, productName, price) {
        document.getElementById("quickProductId").value = productId;
        document.getElementById("quickProductTitle").innerText = productName;
        document.getElementById("rawProductPrice").value = price;
        document.getElementById("quickProductPrice").innerText = price.toLocaleString('vi-VN') + " VNĐ";

        // Xóa các dữ liệu nhập cũ
        document.getElementById("customerName").value = "";
        document.getElementById("customerPhone").value = "";
        document.getElementById("customerAddress").value = "";
        document.getElementById("email_validate").value = ""; // Xóa bẫy honeypot

        const modalEl = document.getElementById("quickOrderModal");
        quickOrderModalInstance = new bootstrap.Modal(modalEl);
        quickOrderModalInstance.show();
    }

    // Submit đặt hàng nhanh trực tiếp lên Java API
    function submitQuickOrder(event) {
        event.preventDefault();

        // 1. Kiểm tra bẫy Honeypot bảo vệ tầng mạng (Task 18 - Honeypot Field)
        const honeyValue = document.getElementById("email_validate").value;
        if (honeyValue && honeyValue.trim() !== "") {
            console.warn("Chặn đứng Bot spam tự điền bẫy Honeypot.");
            // Giả vờ báo thành công nhưng không ghi vào MySQL
            if (quickOrderModalInstance) quickOrderModalInstance.hide();
            alert("🎉 Yêu cầu đặt hàng của bạn đã gửi thành công!");
            return;
        }

        const productId = document.getElementById("quickProductId").value;
        const customerName = document.getElementById("customerName").value.trim();
        const customerPhone = document.getElementById("customerPhone").value.trim();
        const customerAddress = document.getElementById("customerAddress").value.trim();
        const productPrice = parseFloat(document.getElementById("rawProductPrice").value);

        // Đóng gói mảng 1 sản phẩm cho luồng mua nhanh
        const orderPayload = {
            customer_name: customerName,
            customer_phone: customerPhone,
            customer_address: customerAddress,
            total_price: productPrice,
            items: [
                {
                    id: parseInt(productId),
                    quantity: 1,
                    price: productPrice
                }
            ]
        };

        // Gọi API Java Controller
        fetch('${pageContext.request.contextPath}/api/checkout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderPayload)
        })
            .then(response => response.json())
            .then(data => {
                if (quickOrderModalInstance) {
                    quickOrderModalInstance.hide();
                }
                if (data.success) {
                    alert("🎉 " + data.message);
                } else {
                    alert("❌ Đặt hàng thất bại: " + data.message);
                }
            })
            .catch(error => {
                console.error("Lỗi gửi yêu cầu mua nhanh:", error);
                alert("❌ Có lỗi kết nối xảy ra khi gửi đơn hàng!");
            });
    }
</script>

</body>
</html>