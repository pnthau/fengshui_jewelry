<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh Sách Trang Sức Phong Thủy</title>
    <!-- Bootstrap 5 CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css?v=1.0.7" rel="stylesheet">
</head>
<body>

<!-- NAV BAR -->
<nav class="navbar navbar-expand-lg navbar-dark navbar-custom sticky-top">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/home">
            🌸 PHONG THỦY
        </a>
        <div class="collapse navbar-collapse" id="navbarContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">Trang Chủ</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/products">Sản Phẩm</a>
                </li>
            </ul>
            <a href="tel:0905123456" class="btn btn-gold px-4">
                <i class="bi bi-telephone-fill me-2"></i> Tư Vấn: 0905.123.456
            </a>
        </div>
    </div>
</nav>

<main class="container my-5">
    <div class="row g-4">
        <!-- SIDEBAR: TÌM KIẾM & TRA CỨU MỆNH -->
        <div class="col-lg-4">
            <div class="glass-panel p-4 mb-4">
                <h3 class="h4 mb-3"><i class="bi bi-search me-2"></i>Tìm Kiếm</h3>

                <!-- Form Tìm kiếm -->
                <form action="${pageContext.request.contextPath}/products" method="GET" id="search-form" class="mb-4">
                    <div class="input-group">
                        <input type="text" name="keyword" id="search-input"
                               class="form-control bg-dark text-white border-secondary"
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

                <!-- Widget Tra Cứu Mệnh -->
                <div class="card year-finder-card p-4 text-center">
                    <h4 class="h5 text-warning mb-3"><i class="bi bi-compass-fill me-2"></i>Tra Cứu Mệnh</h4>
                    <div class="row g-2 justify-content-center">
                        <div class="col-8">
                            <input type="number" id="birthYearInput" class="form-control text-center fw-bold"
                                   placeholder="Ví dụ: 1968" min="1930" max="2030">
                        </div>
                        <div class="col-4">
                            <button type="button" class="btn btn-gold w-100" onclick="calculateElement()">Tra cứu
                            </button>
                        </div>
                    </div>
                    <div id="element-result" class="mt-4 ${empty year ? 'd-none' : ''} p-3 rounded"
                         style="background: rgba(0,0,0,0.3); border: 1px dashed var(--accent-gold);">
                        <c:if test="${not empty year}">
                            <div class="mb-2 fs-5 text-white text-start">
                                ông/bà sinh năm <strong>${year}</strong> hợp:
                                <span class="badge-element badge-${fn:toLowerCase(selectedElement)}">Mệnh ${selectedElement}</span>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <!-- DANH SÁCH SẢN PHẨM & BỘ LỌC MỆNH -->
        <div class="col-lg-8">
            <div class="glass-panel p-4 mb-4">
                <h3 class="h4 mb-4"><i class="bi bi-funnel-fill me-2"></i>Lọc Theo Cung Mệnh</h3>
                <div class="d-flex flex-wrap gap-2 mb-2">
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
        </div>
        <%-- Đóng div.col-lg-8 --%>

        <c:choose>
        <c:when test="${empty products}">
            <p>Không có sản phẩm nào phù hợp.</p>
        </c:when>
        <c:otherwise>
        <p class="text-secondary mb-3"><i class="bi bi-info-circle me-1"></i> Tìm thấy danh sách sản phẩm...
        </p>
        <div class="row row-cols-1 row-cols-md-3 g-3" id="productsGrid">
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
                                    <span class="badge-element badge-${fn:toLowerCase(elem)}">${elem}</span>
                                </c:forEach>
                            </div>
                            <h4 class="product-title">${product.name}</h4>
                            <p class="text-secondary small mb-2"><i class="bi bi-gem me-1"></i> Chất
                                liệu: ${product.material}</p>
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                 <span class="product-price">
                                        <fmt:formatNumber value="${product.price}" type="number" maxFractionDigits="0"/> VNĐ
                                 </span>
                                <c:choose>
                                    <c:when test="${product.quantity > 0}">
                                        <span class="badge bg-success-subtle text-success">Còn hàng</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger-subtle text-danger">Hết hàng</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="row g-2">
                                <div class="col-6">
                                    <a href="${pageContext.request.contextPath}/product-detail?id=${product.id}"
                                       class="btn btn-outline-secondary w-100 py-1">Chi tiết</a>
                                </div>
                                <div class="col-6">
                                    <button type="button" class="btn btn-gold w-100 py-1 btn-quick-order"
                                            data-id="${product.id}"
                                            data-name="${fn:escapeXml(product.name)}"
                                            data-price="${product.price}"
                                            data-image="${pageContext.request.contextPath}/${product.imageURL}">Mua
                                        nhanh
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
            </c:otherwise>
            </c:choose>
        </div>
    </div>
    <%-- Đóng div.row --%>
</main>

<!-- FOOTER -->
<footer class="text-center">
    <div class="container">
        <h5 class="text-warning mb-3">🌸 PHONG THỦY 🌸</h5>
        <div class="text-secondary small">© 2026 Phong Thủy - Chữ to rõ ràng, sản phẩm chuẩn tự nhiên.</div>
    </div>
</footer>

<script src="${pageContext.request.contextPath}/assets/js/fengshui-utils.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>

<script>
    // TODO 2: Viết logic Voice Search sử dụng Web Speech API cho nút "voiceBtn"
    const VoiceSearchModule = (function () {
        const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
        let recognition = null;
        let isListening = false; // Trạng thái private bảo vệ nút micro tránh click đúp

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

        // --- Chỉ phơi bày (expose) API PUBLIC ra bên ngoài ---
        return {
            init: initialize
        };
    })();

    const voiceBtn = document.getElementById('voice-btn');
    const searchInput = document.getElementById("search-input");
    const searchFrom = document.getElementById("search-form");

    VoiceSearchModule.init({
        voiceBtn: voiceBtn,
        onStart: function () {
            voiceBtn.classList.add('voice-recording');
        },
        onEnd: function () {
            voiceBtn.classList.remove('voice-recording');
        },
        onError: function (err) {
            console.error("Lỗi ghi âm:", err);
        },
        onResult: function (text) {
            const cleanText = text.trim().replace(/[.,?!]+$/, "");
            console.log(cleanText);
            searchInput.value = cleanText;
            searchFrom.submit();
        }
    });


    // Xử lý Tính Mệnh theo năm sinh sử dụng hàm thuần khiết từ fengshui-utils.js
    function calculateElement() {
        const yearInput = document.getElementById('birthYearInput');
        const year = parseInt(yearInput.value);
        const resultDiv = document.getElementById('element-result');

        const elementName = getElementByYear(year);

        if (!elementName) {
            resultDiv.innerHTML = "Vui lòng nhập năm sinh hợp lệ!";
            resultDiv.classList.remove('d-none');
            resultDiv.className = "alert alert-danger";
            return;
        }

        const contextPath = "${pageContext.request.contextPath}";
        window.location.href = contextPath + "/products?element="
            + encodeURIComponent(elementName)
            + "&year=" + encodeURIComponent(year);
    }
</script>

</body>
</html>
