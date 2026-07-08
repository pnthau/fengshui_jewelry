<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="context-path" content="${pageContext.request.contextPath}">
    <title>${product.name} - Trang Sức Phong Thủy</title>
    <!-- Bootstrap 5 CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css?v=1.0.7" rel="stylesheet">

    <style>
        /* Premium Aesthetics cho Product Detail */
        body {
            background-color: #121212;
            color: #f8f9fa;
        }

        .detail-glass-panel {
            background: rgba(30, 30, 30, 0.6);
            backdrop-filter: blur(15px);
            -webkit-backdrop-filter: blur(15px);
            border: 1px solid rgba(255, 215, 0, 0.2);
            border-radius: 20px;
            padding: 2rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
            transition: transform 0.3s ease;
        }

        .detail-glass-panel:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 40px rgba(243, 156, 18, 0.2);
        }

        .media-wrapper {
            border-radius: 15px;
            overflow: hidden;
            border: 2px solid rgba(243, 156, 18, 0.5);
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
            background: rgba(0, 0, 0, 0.3);
        }
        .media-wrapper img, .media-wrapper iframe {
            object-fit: cover;
            width: 100%;
            height: 100%;
        }

        .price-tag {
            color: #f39c12;
            font-size: 2.5rem;
            font-weight: bold;
            text-shadow: 0 0 10px rgba(243, 156, 18, 0.4);
        }

        .element-badge {
            font-size: 1.1rem;
            padding: 8px 16px;
            border-radius: 30px;
            background: linear-gradient(45deg, #2c3e50, #f39c12);
            border: 1px solid #f39c12;
            color: white;
            margin-right: 10px;
            display: inline-block;
        }

        .btn-gold {
            background: linear-gradient(45deg, #f39c12, #f1c40f);
            color: #1a1a1a;
            font-weight: bold;
            border: none;
            border-radius: 30px;
            padding: 12px 30px;
            transition: all 0.3s;
        }

        .btn-gold:hover {
            transform: scale(1.05);
            box-shadow: 0 0 15px rgba(243, 156, 18, 0.8);
            color: #000;
        }

        /* Custom Tabs Styling */
        .nav-pills .nav-link {
            color: #f8f9fa;
            border: 1px solid rgba(243, 156, 18, 0.3);
            margin: 0 5px;
            transition: all 0.3s ease;
        }

        .nav-pills .nav-link:hover {
            background-color: rgba(243, 156, 18, 0.2);
        }

        .nav-pills .nav-link.active {
            background-color: #f39c12;
            color: #1a1a1a;
            font-weight: bold;
            box-shadow: 0 4px 10px rgba(243, 156, 18, 0.4);
        }
    </style>
</head>
<body>

<!-- NAV BAR -->
<nav class="navbar navbar-expand-lg navbar-dark navbar-custom sticky-top">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/home">🌸 PHONG THỦY</a>
        <div class="collapse navbar-collapse" id="navbarContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">Trang Chủ</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/products">Sản Phẩm</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<main class="container my-5">
    <!-- Nút quay lại -->
    <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-warning mb-4"
       style="border-radius: 20px;">
        <i class="bi bi-arrow-left"></i> Quay lại danh sách
    </a>

    <!-- Chi tiết sản phẩm (Glassmorphism) -->
    <div class="detail-glass-panel">
        <div class="row">
            <!-- Cột Trái: Hình ảnh & Video -->
            <div class="col-lg-5 mb-4 mb-lg-0 text-center">
                <c:choose>
                    <c:when test="${not empty product.youtubeURL and not empty product.imageURL}">
                        <!-- Cả Image và Video: Dùng Tabs -->
                        <ul class="nav nav-pills mb-3 justify-content-center" id="media-tabs" role="tablist">
                            <li class="nav-item" role="presentation">
                                <button class="nav-link active" id="video-tab" data-bs-toggle="pill"
                                        data-bs-target="#tab-video" type="button" role="tab"
                                        style="border-radius: 20px;">
                                    <i class="bi bi-play-btn-fill me-1"></i>Video
                                </button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link " id="image-tab" data-bs-toggle="pill"
                                        data-bs-target="#tab-image" type="button" role="tab"
                                        style="border-radius: 20px;">
                                    <i class="bi bi-image me-1"></i>Hình ảnh
                                </button>
                            </li>

                        </ul>
                        <div class="tab-content" id="media-tabs-content">
                            <div class="tab-pane fade show active" id="tab-video" role="tabpanel">
                                <div class="ratio ratio-1x1 media-wrapper">
                                    <iframe class="product-video-iframe" src="" data-raw-url="${product.youtubeURL}" title="Product video" allowfullscreen></iframe>
                                </div>
                            </div>
                            <div class="tab-pane fade" id="tab-image" role="tabpanel">
                                <div class="ratio ratio-1x1 media-wrapper">
                                    <img src="${product.imageURL}" alt="${product.name}" 
                                         onerror="this.style.display='none';">
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${not empty product.youtubeURL}">
                        <!-- Chỉ có Video -->
                        <div class="ratio ratio-1x1 media-wrapper mb-3">
                            <iframe class="product-video-iframe" src="" data-raw-url="${product.youtubeURL}" title="Product video" allowfullscreen></iframe>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <!-- Chỉ có Hình ảnh -->
                        <c:choose>
                            <c:when test="${not empty product.imageURL}">
                                <div class="ratio ratio-1x1 media-wrapper mb-3">
                                    <img src="${product.imageURL}" alt="${product.name}"
                                         onerror="this.style.display='none';">
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="ratio ratio-1x1 media-wrapper mb-3">
                                    <div class="w-100 h-100" style="background: rgba(0,0,0,0.5);"></div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Cột Phải: Thông tin chi tiết -->
            <div class="col-lg-7 px-4">
                <h1 class="display-5 fw-bold text-white mb-3">${product.name}</h1>

                <!-- Hiển thị giá tiền -->
                <div class="price-tag mb-3">
                    <fmt:formatNumber value="${product.price}" type="number" groupingUsed="true" maxFractionDigits="0"/>
                    VNĐ
                </div>

                <!-- Các thẻ Mệnh -->
                <div class="mb-4">
                    <c:forEach var="element" items="${product.elements}">
                        <span class="element-badge"><i
                                class="bi bi-star-fill text-warning me-1"></i> Mệnh ${element}</span>
                    </c:forEach>
                </div>

                <!-- Bảng cấu hình -->
                <table class="table table-dark table-striped table-hover mt-4"
                       style="border-radius: 10px; overflow: hidden;">
                    <tbody>
                    <tr>
                        <th scope="row" style="width: 30%; color: #f39c12;">Chất liệu</th>
                        <td>${product.material}</td>
                    </tr>
                    <tr>
                        <th scope="row" style="color: #f39c12;">Tình trạng</th>
                        <td>
                            <c:choose>
                                <c:when test="${product.quantity > 0}">
                                    <span class="badge bg-success">Còn hàng (${product.quantity})</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-danger">Hết hàng</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" style="color: #f39c12;">Phân loại</th>
                        <td>${product.status}</td>
                    </tr>
                    </tbody>
                </table>

                <!-- Mô tả sản phẩm -->
                <div class="mt-4">
                    <h4 style="color: #f39c12;"><i class="bi bi-info-circle-fill me-2"></i>Mô tả chi tiết</h4>
                    <p class="text-light" style="line-height: 1.8; font-size: 1.1rem;">
                        ${product.description}
                    </p>
                </div>

                <!-- Nút Hành động -->
                <div class="mt-5 d-flex gap-3">
                    <button class="btn btn-gold btn-lg w-50"
                            onclick="addToCart(event, '${product.id}', '${fn:escapeXml(product.name)}', ${product.price}, '${product.imageURL}')">
                        <i class="bi bi-cart-plus-fill me-2"></i>Thêm vào giỏ
                    </button>
                    <a href="${pageContext.request.contextPath}/home#contact"
                       class="btn btn-outline-warning btn-lg w-50" style="border-radius: 30px;">
                        <i class="bi bi-telephone-fill me-2"></i>Nhờ tư vấn
                    </a>
                </div>
            </div>
        </div>
    </div>
</main>

<!-- FLOATING CART BUTTON -->
<div class="floating-cart-btn" onclick="openCartModal()" data-bs-toggle="tooltip" data-bs-placement="left"
     title="Giỏ Hàng Của Bạn">
    <i class="bi bi-cart3"></i>
    <span class="cart-badge" id="cartBadgeCount">${sessionScope.cart != null ? sessionScope.cart.totalQuantity : 0}</span>
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
                <div class="d-flex gap-2 flex-wrap">
                    <button type="button" class="btn btn-outline-light px-4 py-2" data-bs-dismiss="modal">Tiếp Tục Mua
                    </button>
                    <button type="button" class="btn btn-gold px-4 py-2 fs-5 fw-bold" onclick="checkoutCart()">Xác Nhận
                        Đặt Hàng
                    </button>
                    <button type="button" class="btn btn-info px-4 py-2 fs-5 fw-bold text-white" onclick="checkoutVnpay()" style="background: linear-gradient(135deg, #0066b2, #004080); border: none;">
                        <i class="bi bi-credit-card-fill me-2"></i>VNPay
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS for Tabs and Interactions -->
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
<!-- Toast Utility -->
<script src="${pageContext.request.contextPath}/assets/js/toast.js"></script>
<!-- Cart Logic -->
<script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>

<!-- Multi-Platform Video Embed Auto-Parser -->
<script>
    document.addEventListener("DOMContentLoaded", function() {
        // Hàm chuyển đổi link nhúng đa nền tảng
        function getEmbedUrl(rawUrl) {
            if (!rawUrl) return "";
            rawUrl = rawUrl.trim();

            // 1. YouTube (sử dụng Regex tối giản đã sửa lỗi của chúng ta)
            let ytReg = /(?:youtube\.com\/(?:embed\/|v\/|shorts\/|watch\?v=)|youtu\.be\/)([^"&?\/\s]{11})/i;
            let ytMatch = rawUrl.match(ytReg);
            if (ytMatch && ytMatch[1]) {
                return "https://www.youtube.com/embed/" + ytMatch[1];
            }

            // 2. TikTok
            let ttReg = /tiktok\.com\/@[^\/]+\/video\/(\d+)/i;
            let ttMatch = rawUrl.match(ttReg);
            if (ttMatch && ttMatch[1]) {
                return "https://www.tiktok.com/embed/v2/" + ttMatch[1];
            }

            // 3. Facebook
            if (rawUrl.includes("facebook.com") || rawUrl.includes("fb.watch")) {
                return "https://www.facebook.com/plugins/video.php?href=" + encodeURIComponent(rawUrl) + "&show_text=false&t=0";
            }

            if (rawUrl.startsWith("http://") || rawUrl.startsWith("https://")) {
                return rawUrl;
            }

            return null; // Trả về null nếu không phải link hợp lệ
        }

        // Vòng lặp duyệt các iframe của anh
        document.querySelectorAll(".product-video-iframe").forEach(function(iframe) {
            let rawUrl = iframe.dataset.rawUrl;
            let embedUrl = getEmbedUrl(rawUrl);
            
            if (embedUrl === null) {
                // Link video sai định dạng, ẩn iframe đi (để trống)
                iframe.style.display = 'none';
            } else {
                iframe.src = embedUrl;
            }
        });
    });
</script>
</body>
</html>
