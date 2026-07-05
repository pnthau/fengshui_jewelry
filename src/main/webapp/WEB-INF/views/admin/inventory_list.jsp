<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Nhật Ký Xuất Nhập Kho - Phong Thủy Hậu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- DataTables Bootstrap 5 CSS -->
    <link href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body { font-family: 'Quicksand', sans-serif; }
        .text-gold { color: #D4AF37 !important; }
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
            <a href="${pageContext.request.contextPath}/admin/inventory?action=export" class="btn btn-success">
                <i class="bi bi-file-earmark-spreadsheet me-1"></i> Xuất CSV
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

    <!-- BỘ LỌC NÂNG CAO (SEARCH BY COLUMN) -->
    <div class="card shadow-sm border-0 mb-3">
        <div class="card-body bg-white rounded">
            <div class="row g-3">
                <div class="col-md-4">
                    <label class="form-label small fw-bold text-muted">Tìm theo tên sản phẩm</label>
                    <div class="input-group">
                        <span class="input-group-text bg-light border-end-0"><i class="bi bi-search"></i></span>
                        <input type="text" id="searchProduct" class="form-control border-start-0 ps-0" placeholder="Nhập tên sản phẩm...">
                    </div>
                </div>
                <div class="col-md-3">
                    <label class="form-label small fw-bold text-muted">Loại giao dịch</label>
                    <select id="filterType" class="form-select">
                        <option value="">-- Tất cả loại --</option>
                        <option value="IMPORT">📥 NHẬP KHO</option>
                        <option value="EXPORT">📤 XUẤT KHO</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label small fw-bold text-muted">Trạng thái phiếu</label>
                    <select id="filterStatus" class="form-select">
                        <option value="">-- Tất cả trạng thái --</option>
                        <option value="COMPLETED">Hoạt động</option>
                        <option value="VOIDED">Đã hủy</option>
                    </select>
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <button type="button" id="resetFilter" class="btn btn-outline-secondary w-100">
                        <i class="bi bi-arrow-clockwise me-1"></i> Làm mới
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- BẢNG NHẬT KÝ CHI TIẾT (DTO LÊN BẢNG) -->
    <div class="card shadow border-0">
        <div class="card-body p-0">
            <table id="inventoryTable" class="table table-hover table-bordered mb-0 align-middle">
                <thead class="table-dark">
                <tr>
                    <th style="width: 5%">ID</th>
                    <th style="width: 35%">Sản phẩm phong thủy</th>
                    <th style="width: 12%" class="text-center">Loại giao dịch</th>
                    <th style="width: 10%" class="text-center">Số lượng</th>
                    <th style="width: 13%">Đơn giá nhập/xuất</th>
                    <th style="width: 20%">Lý do thay đổi</th>
                    <th style="width: 5%">Thao tác</th>
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
                        <!-- Gắn data-search để DataTables lọc theo code thay vì icon -->
                        <td class="text-center" data-search="${tx.transactionType}">
              <span class="badge px-3 py-2 ${tx.transactionType == 'IMPORT' ? 'badge-in' : 'badge-out'}" data-type="${tx.transactionType}">
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
                        <!-- Gắn data-search để lọc trạng thái chuẩn xác -->
                        <td data-search="${tx.status}">
                            <c:if test="${tx.status != 'VOIDED'}">
                                <form action="${pageContext.request.contextPath}/admin/inventory" method="POST" onsubmit="return confirm('Bạn có chắc muốn hủy phiếu này?')">
                                    <input type="hidden" name="action" value="void">
                                    <input type="hidden" name="id" value="${tx.id}">
                                    <button class="btn btn-sm btn-outline-danger">Hủy</button>
                                </form>
                            </c:if>
                            <c:if test="${tx.status == 'VOIDED'}"><span class="badge bg-secondary">Đã hủy</span></c:if>
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
                    <div class="mb-3">
                        <label class="form-label fw-bold">Hình thức giao dịch:</label>
                        <select name="transactionType" class="form-select" required>
                            <option value="IMPORT">📥 Nhập Kho (IMPORT)</option>
                            <option value="EXPORT">📤 Xuất Kho (EXPORT)</option>
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
<!-- JQuery & DataTables JS -->
<script src="https://code.jquery.com/jquery-3.7.0.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

<script>
    $(document).ready(function() {
        var table = $('#inventoryTable').DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/1.13.6/i18n/vi.json" // Việt hóa giao diện
            },
            "order": [[0, "desc"]], // Mặc định sắp xếp theo ID giảm dần
            "pageLength": 10, // Số dòng trên mỗi trang
            "columnDefs": [
                { "orderable": false, "targets": 6 }, // Vô hiệu hóa sắp xếp cột "Thao tác"
                { "visible": true, "targets": [1, 2, 6] }
            ],
            "dom": '<"d-flex justify-content-between align-items-center p-3"<"d-flex align-items-center"l>>t<"d-flex justify-content-between align-items-center p-3"ip>'
        });

        // 1. Tìm kiếm theo Tên sản phẩm (Cột index 1)
        $('#searchProduct').on('keyup', function() {
            table.column(1).search(this.value).draw();
        });

        // 2. Lọc theo Loại giao dịch (Cột index 2)
        $('#filterType').on('change', function() {
            var val = $(this).val();
            // Tìm kiếm chính xác (exact match) dựa trên data-search
            table.column(2).search(val ? '^' + val + '$' : '', true, false).draw();
        });

        // 3. Lọc theo Trạng thái (Cột index 6)
        $('#filterStatus').on('change', function() {
            var val = $(this).val();
            // Lọc theo code trạng thái COMPLETED hoặc VOIDED gắn trong data-search
            table.column(6).search(val ? '^' + val + '$' : '', true, false).draw();
        });

        // Nút Reset bộ lọc
        $('#resetFilter').on('click', function() {
            $('#searchProduct').val('');
            $('#filterType').val('');
            $('#filterStatus').val('');
            table.columns().search('').draw();
        });

        // Tự động đóng alert sau 3 giây
        setTimeout(function() {
            $(".alert").fadeOut('slow');
        }, 3000);
    });
</script>
</body>
</html>