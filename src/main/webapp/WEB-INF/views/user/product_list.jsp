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
    <style>
        /* Custom Scrollbar cho Modal Giỏ hàng */
        .custom-scrollbar::-webkit-scrollbar {
            width: 6px;
        }

        .custom-scrollbar::-webkit-scrollbar-track {
            background: rgba(255, 255, 255, 0.05);
            border-radius: 4px;
        }

        .custom-scrollbar::-webkit-scrollbar-thumb {
            background: var(--accent-gold, #f39c12);
            border-radius: 4px;
        }

        .custom-scrollbar::-webkit-scrollbar-thumb:hover {
            background: #e67e22;
        }

        /* --- CUSTOM TOOLTIP KHỔNG LỒ CHO NGƯỜI LỚN TUỔI --- */
        .tooltip-inner {
            font-size: 16px !important;
            font-weight: bold;
            padding: 8px 16px;
            background-color: #1a1a1a !important; /* Nền đen xám */
            color: #f39c12 !important; /* Chữ vàng */
            border: 2px solid #f39c12;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(243, 156, 18, 0.4);
            max-width: 300px; /* Cho phép chữ dài không bị xuống dòng vụn */
        }

        /* Mũi tên của Tooltip */
        .tooltip.bs-tooltip-top .tooltip-arrow::before {
            border-top-color: #f39c12 !important;
        }

        .tooltip.bs-tooltip-bottom .tooltip-arrow::before {
            border-bottom-color: #f39c12 !important;
        }

        .tooltip.bs-tooltip-start .tooltip-arrow::before,
        .tooltip.bs-tooltip-left .tooltip-arrow::before {
            border-left-color: #f39c12 !important;
        }

        .tooltip.bs-tooltip-end .tooltip-arrow::before,
        .tooltip.bs-tooltip-right .tooltip-arrow::before {
            border-right-color: #f39c12 !important;
        }
    </style>
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
                               value="${keyword}"
                               data-bs-toggle="tooltip" data-bs-placement="bottom" title="Nhập vô ní">

                        <!-- Nút micro tìm kiếm giọng nói -->
                        <button class="btn voice-search-btn px-3" type="button" id="voice-btn"
                                data-bs-toggle="tooltip" data-bs-placement="bottom" title="Nói đi ní">
                            <i class="bi bi-mic-fill"></i>
                        </button>

                        <button class="btn btn-gold px-3" type="submit"
                                data-bs-toggle="tooltip" data-bs-placement="bottom" title="Bấm đi ní">
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
                            <!-- Nút thêm giỏ hàng cực cháy (Mở rộng có chữ) -->
                            <button class="btn-add-cart-icon"
                                    onclick="addToCart(event, '${product.id}', '${fn:escapeXml(product.name)}', ${product.price}, '${product.imageURL}')"
                                    data-bs-toggle="tooltip" data-bs-placement="top" title="Thêm vào giỏ hàng">
                                <i class="bi bi-cart-plus-fill"></i>
                            </button>
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

<div class="modal fade" id="quickOrderModal" tabindex="-1" aria-labelledby="quickOrderModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content bg-dark text-white border-gold shadow-lg"
             style="border: 2px solid var(--accent-gold); border-radius: 16px;">
            <div class="modal-header border-secondary">
                <h5 class="modal-title text-warning" id="quickOrderModalLabel">
                    <i class="bi bi-telephone-outbound-fill me-2"></i> Đặt Hàng Nhanh
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                        aria-label="Đóng"></button>
            </div>
            <div class="modal-body">
                <!-- Thông tin sản phẩm vắn tắt -->
                <div
                        class="d-flex align-items-center mb-4 p-3 rounded bg-black bg-opacity-25 border border-secondary">
                    <img id="modalProductImage" src="" alt="" class="rounded me-3"
                         style="width: 80px; height: 80px; object-fit: cover; border: 1px solid var(--border-color);"
                         onerror="this.src='https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=600&auto=format&fit=crop'">
                    <div>
                        <h4 class="h5 text-white mb-1" id="modalProductName">Tên sản phẩm</h4>
                        <div class="text-warning fw-bold fs-5" id="modalProductPrice">0 VNĐ</div>
                    </div>
                </div>

                <!-- Hộp thông báo lỗi/thành công -->
                <div id="modalAlert" class="alert d-none" role="alert"></div>

                <!-- Form thông tin khách hàng -->
                <form id="quickOrderForm" autocomplete="on">
                    <input type="hidden" name="productId" id="modalProductId">

                    <!-- Tăng giảm số lượng (Stepper) -->
                    <div class="mb-4">
                        <label class="form-label text-secondary fw-medium">Số lượng mua:</label>
                        <div class="input-group" style="max-width: 180px;">
                            <button class="btn btn-outline-secondary btn-lg" type="button"
                                    id="btnDecreaseQty" style="font-weight: bold;">-
                            </button>
                            <input type="text"
                                   class="form-control form-control-lg text-center fw-bold bg-dark text-white border-secondary"
                                   id="modalQuantity" name="quantity" value="1" readonly>
                            <button class="btn btn-outline-secondary btn-lg" type="button"
                                    id="btnIncreaseQty" style="font-weight: bold;">+
                            </button>
                        </div>
                    </div>

                    <!-- Tên khách hàng -->
                    <div class="mb-3">
                        <label for="customerName" class="form-label text-secondary fw-medium">Họ và
                            Tên của Ông/Bà <span class="text-danger">*</span></label>
                        <input type="text"
                               class="form-control form-control-lg bg-dark text-white border-secondary"
                               id="customerName" name="customerName" placeholder="Ví dụ: Nguyễn Văn A"
                               required>
                    </div>

                    <!-- Số điện thoại -->
                    <div class="mb-3">
                        <label for="customerPhone" class="form-label text-secondary fw-medium">Số
                            điện thoại liên hệ <span class="text-danger">*</span></label>
                        <input type="tel"
                               class="form-control form-control-lg bg-dark text-white border-secondary"
                               id="customerPhone" name="customerPhone" placeholder="Ví dụ: 0905123456"
                               required>
                    </div>

                    <!-- Địa chỉ giao hàng -->
                    <div class="mb-3">
                        <label for="customerAddress" class="form-label text-secondary fw-medium">Địa
                            chỉ nhận hàng <span class="text-danger">*</span></label>
                        <textarea
                                class="form-control form-control-lg bg-dark text-white border-secondary"
                                id="customerAddress" name="customerAddress" rows="2"
                                placeholder="Ví dụ: 123 Hùng Vương, Đà Nẵng" required></textarea>
                    </div>
                </form>
            </div>
            <div class="modal-footer border-secondary">
                <button type="button" class="btn btn-outline-light px-4 py-2"
                        data-bs-dismiss="modal">Hủy
                </button>
                <button type="button" class="btn btn-gold px-5 py-2 fs-5 fw-bold"
                        id="btnSubmitOrder">
                    <i class="bi bi-check-circle-fill me-2"></i> Xác Nhận Đặt Hàng
                </button>
            </div>
        </div>
    </div>
</div>

<!-- FLOATING CART BUTTON -->
<div class="floating-cart-btn" onclick="openCartModal()" data-bs-toggle="tooltip" data-bs-placement="left"
     title="Giỏ Hàng Của Bạn">
    <i class="bi bi-cart3"></i>
    <span class="cart-badge" id="cartBadgeCount">0</span>
</div>

<!-- CART MODAL -->
<div class="modal fade" id="cartModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content bg-dark text-white border-gold shadow-lg"
             style="border: 2px solid var(--accent-gold); border-radius: 16px;">
            <div class="modal-header border-secondary">
                <h5 class="modal-title text-warning"><i class="bi bi-cart-check-fill me-2"></i> Giỏ Hàng Của Bạn</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                        aria-label="Đóng"></button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <!-- Nửa trái: Danh sách sản phẩm -->
                    <div class="col-md-6 border-end border-secondary">
                        <h6 class="text-secondary mb-3"><i class="bi bi-bag-heart-fill"></i> Sản phẩm đã chọn</h6>
                        <div id="cartItemsContainer" class="pe-2 custom-scrollbar"
                             style="max-height: 45vh; overflow-y: auto;">
                            <!-- Danh sách sản phẩm trong giỏ sẽ render ở đây -->
                        </div>
                    </div>

                    <!-- Nửa phải: Form thông tin khách hàng -->
                    <div class="col-md-6">
                        <h6 class="text-secondary mb-3"><i class="bi bi-person-lines-fill"></i> Thông tin giao hàng</h6>
                        <form id="cartOrderForm">
                            <div class="mb-3">
                                <label for="cartCustomerName" class="form-label text-white small">Họ và Tên <span
                                        class="text-danger">*</span></label>
                                <input type="text" id="cartCustomerName"
                                       class="form-control bg-dark text-white border-secondary"
                                       placeholder="Ví dụ: Nguyễn Văn A" required>
                            </div>
                            <div class="mb-3">
                                <label for="cartCustomerPhone" class="form-label text-white small">Số điện thoại <span
                                        class="text-danger">*</span></label>
                                <input type="tel" id="cartCustomerPhone"
                                       class="form-control bg-dark text-white border-secondary"
                                       placeholder="Ví dụ: 0905123456" required>
                            </div>
                            <div class="mb-3">
                                <label for="cartCustomerAddress" class="form-label text-white small">Địa chỉ nhận hàng
                                    <span class="text-danger">*</span></label>
                                <textarea id="cartCustomerAddress"
                                          class="form-control bg-dark text-white border-secondary" rows="3"
                                          placeholder="Ví dụ: 123 Hùng Vương, Đà Nẵng" required></textarea>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="modal-footer border-secondary d-flex justify-content-between align-items-center">
                <div class="d-flex align-items-center bg-black bg-opacity-50 rounded p-2 px-3 border border-warning shadow">
                    <span class="text-white me-3" style="font-size: 1.1rem;">Tổng Tiền:</span>
                    <span class="text-warning fw-bold" style="font-size: 1.4rem;" id="cartTotalPrice">0 VNĐ</span>
                </div>
                <div class="d-flex gap-2">
                    <button type="button" class="btn btn-outline-light px-4 py-2" data-bs-dismiss="modal">Tiếp Tục Mua
                    </button>
                    <button type="button" class="btn btn-gold px-4 py-2 fs-5 fw-bold" onclick="checkoutCart()">Xác Nhận
                        Đặt Hàng
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

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
    // Khởi tạo các thành phần UI
    document.addEventListener('DOMContentLoaded', () => {
        // Kích hoạt Bootstrap Tooltip
        var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    });

    const VoiceSearchModule = (function () {
        const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
        let recognition = null;
        let isListening = false; // Trạng thái private bảo vệ nút micro tránh click đúp

        function initialize(config) {
            if (!SpeechRecognition) {
                console.warn("error :  not support Web Speech API.");
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

    const ModalManagerModule = (function () {
        let bsModal = null;

        function initialize({containerSelector, modalSelector, btnSelector, onBind}) {
            const modalEL = document.querySelector(modalSelector);
            const containerEL = document.querySelector(containerSelector);
            if (!containerEL || !modalEL) {
                return;
            }

            bsModal = new bootstrap.Modal(modalEL);

            containerEL.addEventListener('click', function (event) {
                const btn = event.target.closest(btnSelector);
                if (btn) {
                    if (typeof onBind === 'function') {
                        onBind(btn);
                    }
                    if (bsModal) {
                        bsModal.show();
                    }
                }
            });

        }

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

    window.addEventListener('DOMContentLoaded', () => {
        const modalEl = document.getElementById('quickOrderModal');
        let idInputEl = null;
        let nameEl = null;
        let priceEl = null;
        let imgEl = null;

        let qtyInputEl = null;
        let btnDecreaseEl = null;
        let btnIncreaseEl = null;

        let customerNameInputEl = null;
        let customerPhoneInputEl = null;
        let customerAddressInputEl = null;
        let btnSubmitOrderEl = null;
        if (modalEl) {
            idInputEl = modalEl.querySelector('#modalProductId');
            nameEl = modalEl.querySelector('#modalProductName');
            priceEl = modalEl.querySelector('#modalProductPrice');
            imgEl = modalEl.querySelector('#modalProductImage');

            qtyInputEl = modalEl.querySelector('#modalQuantity');
            btnDecreaseEl = modalEl.querySelector('#btnDecreaseQty');
            btnIncreaseEl = modalEl.querySelector('#btnIncreaseQty');

            customerNameInputEl = modalEl.querySelector('#customerName');
            customerPhoneInputEl = modalEl.querySelector('#customerPhone');
            customerAddressInputEl = modalEl.querySelector('#customerAddress');
            btnSubmitOrderEl = modalEl.querySelector('#btnSubmitOrder');
        }

        ModalManagerModule.init({
            containerSelector: '#productsGrid',
            modalSelector: '#quickOrderModal',
            btnSelector: '.btn-quick-order',
            onBind: function (btn) {
                if (imgEl) {
                    imgEl.src = btn.dataset.image;
                }
                if (nameEl) {
                    nameEl.textContent = btn.dataset.name;
                }
                if (priceEl) {
                    priceEl.textContent = Number(btn.dataset.price).toLocaleString('vi-VN') + ' VNĐ';
                }
                if (idInputEl) {
                    idInputEl.value = btn.dataset.id;
                }
                if (qtyInputEl) {
                    qtyInputEl.value = 1;
                }
            },
        });
        if (btnDecreaseEl && qtyInputEl) {
            btnDecreaseEl.addEventListener('click', function (event) {
                let currQty = parseInt(qtyInputEl.value) || 1;
                if (currQty > 1) {
                    currQty -= 1;
                    qtyInputEl.value = currQty;
                }
            });
        }
        if (btnIncreaseEl && qtyInputEl) {
            btnIncreaseEl.addEventListener('click', function (event) {
                let currQty = parseInt(qtyInputEl.value) || 1;
                currQty += 1;
                qtyInputEl.value = currQty;
            });
        }
        if (btnSubmitOrderEl && qtyInputEl && idInputEl && customerPhoneInputEl && customerNameInputEl && customerAddressInputEl) {
            btnSubmitOrderEl.addEventListener('click', async function (event) {
                let productId = idInputEl.value || '';
                let quantity = qtyInputEl.value || '1';
                let customerPhone = customerPhoneInputEl.value || '';
                let customerName = customerNameInputEl.value || '';
                let customerAddress = customerAddressInputEl.value || '';

                const contextPath = "${pageContext.request.contextPath}";
                const url = contextPath + 'quick-order';

                const params = new URLSearchParams();
                params.append('productId', productId);
                params.append('quantity', quantity);
                params.append('customerPhone', customerPhone);
                params.append('customerName', customerName);
                params.append('customerAddress', customerAddress);
                try {
                    const response = await fetch(url, {
                        method: 'post',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: params.toString()
                    });
                    if (response.ok) {
                        alert("🎉 Đặt hàng thành công!");
                        bootstrap.Modal.getInstance(modalEl).hide();
                    } else {
                        alert("❌ Có lỗi xảy ra từ Server!");
                    }
                } catch (error) {
                    console.error("Lỗi:", error);
                    alert("⚠️ Không thể kết nối đến máy chủ.");
                }
            });
        }
    });

    // Khai báo biến toàn cục để file cart.js có thể lấy được đường dẫn thư mục gốc
    const CONTEXT_PATH = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/assets/js/cart.js">
</script>

</body>
</html>
