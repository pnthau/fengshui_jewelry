<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Nhật Ký Xuất Nhập Kho - Phong Thủy Hậu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body { font-family: 'Quicksand', sans-serif; }
        .border-gold { border: 2px solid #D4AF37 !important; }
        .text-gold { color: #D4AF37 !important; }
        .badge-in { background-color: #22c55e !important; color: white; }
        .badge-out { background-color: #ef4444 !important; color: white; }
    </style>
</head>
<body class="bg-light mt-4">
<div class="container">

    <!-- Tiêu đề trang và điều hướng nhanh -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2><i class="bi bi-journal-album text-gold me-2"></i>Nhật Ký Xuất Nhập Kho</h2>
        <div class="gap-2 d-flex">
            <a href="${pageContext.request.contextPath}/admin/products?action=list" class="btn btn-outline-secondary">
                <i class="bi bi-box-seam me-1"></i> Sản phẩm
            </a>
            <a href="${pageContext.request.contextPath}/admin/orders?action=list" class="btn btn-outline-secondary">
                <i class="bi bi-receipt-cutoff me-1"></i> Đơn hàng
            </a>
            <!-- Nút kích hoạt Modal tạo phiếu kho nhanh -->
            <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createTxModal">
                <i class="bi bi-plus-circle me-1"></i> Tạo Phiếu Kho
            </button>
        </div>
    </div>

    <!-- Thông báo trạng thái giao dịch kho -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show border-danger shadow-sm mb-4" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2 fs-5"></i>
            <strong>⚠️ LỖI GIAO DỊCH KHO:</strong> ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${param.success == 'true'}">
        <div class="alert alert-success alert-dismissible fade show border-success shadow-sm mb-4" role="alert">
            <i class="bi bi-check-circle-fill me-2 fs-5"></i>
            <strong>✅ THÀNH CÔNG:</strong> Thực hiện giao dịch thay đổi tồn kho vật lý thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- BẢNG NHẬT KÝ CHI TIẾT (DTO LÊN BẢNG) -->
    <div class="card shadow border-0">
        <div class="card-body p-0">
            <table class="table table-hover table-bordered mb-0 align-middle">
                <thead class="table-dark">
                <tr>
                    <th style="width: 5%">ID</th>
                    <th style="width: 35%">Sản phẩm phong thủy</th>
                    <th style="width: 12%" class="text-center">Loại giao dịch</th>
                    <th style="width: 10%" class="text-center">Số lượng</th>
                    <th style="width: 13%">Đơn giá nhập/xuất</th>
                    <th style="width: 25%">Lý do thay đổi</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="tx" items="${transactions}">
                    <tr>
                        <td>${tx.id}</td>
                        <td>
                            <div class="d-flex align-items-center">
                                <!-- Tận dụng DTO lấy ảnh lấp lánh của sản phẩm phong thủy -->
                                <img src="${pageContext.request.contextPath}/${tx.imageURL}"
                                     alt="${tx.productName}" class="rounded me-3"
                                     style="width: 45px; height: 45px; object-fit: cover; border: 1px solid #ddd;"
                                     onerror="this.src='https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=200&auto=format&fit=crop'">
                                <div>
                                    <span class="fw-bold d-block text-dark">${tx.productName}</span>
                                    <small class="text-muted">Mã sản phẩm: #${tx.productId}</small>
                                </div>
                            </div>
                        </td>
                        <td class="text-center">
              <span class="badge px-3 py-2 ${tx.transactionType == 'IMPORT' ? 'badge-in' : 'badge-out'}">
                      ${tx.transactionType == 'IMPORT' ? '📥 NHẬP KHO' : '📤 XUẤT KHO'}
              </span>
                        </td>
                        <td class="text-center fw-bold fs-5">${tx.quantity}</td>
                        <td>
                            <fmt:formatNumber value="${tx.price}" type="number" maxFractionDigits="0"/> VNĐ
                        </td>
                        <td>
                            <span class="text-dark d-block">${tx.reason}</span>
                            <small class="text-muted"><i class="bi bi-clock me-1"></i>${tx.createdAt}</small>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty transactions}">
                    <tr>
                        <td colspan="6" class="text-center py-5 text-muted">
                            <i class="bi bi-inbox fs-1 d-block mb-2"></i> Chưa có giao dịch xuất nhập kho nào được ghi nhận.
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>

</div>

<!-- ==================== MODAL TẠO PHIẾU NHẬP / XUẤT KHO THỦ CÔNG ==================== -->
<div class="modal fade" id="createTxModal" tabindex="-1" aria-labelledby="createTxModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content bg-white text-dark">
            <div class="modal-header bg-dark text-white">
                <h5 class="modal-title" id="createTxModalLabel"><i class="bi bi-pencil-square me-2 text-warning"></i>Tạo Phiếu Biến Động Kho</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <!-- Thay đổi phần form nhập kho trong Modal -->
            <form action="${pageContext.request.contextPath}/admin/inventory" method="POST">
                <input type="hidden" name="action" value="submit">
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label">Chọn sản phẩm:</label>
                        <select name="productId" class="form-select" required>
                            <c:forEach var="p" items="${products}">
                                <option value="${p.id}">${p.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Số lượng nhập:</label>
                            <input type="number" name="quantity" class="form-control" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Giá bán áp dụng (VNĐ):</label>
                            <input type="number" name="price" class="form-control" required>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Lý do nhập:</label>
                        <textarea name="reason" class="form-control" required></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">Xác nhận nhập kho & Cập nhật giá</button>
                </div>
            </form>        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>