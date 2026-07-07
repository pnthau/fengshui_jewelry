<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<div class="container-xl">

    <!-- Tiêu đề trang và điều hướng nhanh -->
    <div class="page-header d-print-none">
        <div class="row align-items-center">
            <div class="col">
                <h2 class="page-title">
                    <i class="ti ti-clipboard-list me-2"></i> Nhật Ký Xuất Nhập Kho
                </h2>
            </div>
            <div class="col-auto ms-auto d-print-none">
                <div class="btn-list">
                    <a href="${pageContext.request.contextPath}/admin/inventory?action=export" class="btn btn-success d-none d-sm-inline-block">
                        <i class="ti ti-file-spreadsheet me-1"></i> Xuất CSV
                    </a>
                    <button type="button" class="btn btn-primary d-none d-sm-inline-block" data-bs-toggle="modal" data-bs-target="#createTxModal">
                        <i class="ti ti-plus me-1"></i> Tạo Phiếu Kho
                    </button>
                    <a href="#" class="btn btn-primary d-sm-none btn-icon" data-bs-toggle="modal" data-bs-target="#createTxModal" aria-label="Create new report">
                        <i class="ti ti-plus"></i>
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- Thông báo trạng thái giao dịch kho -->
    <c:if test="${not empty error}">
        <script>
            $(document).ready(function() {
                if (typeof window.showToast === 'function') {
                    window.showToast('danger', 'LỖI GIAO DỊCH KHO: ${error}');
                }
            });
        </script>
    </c:if>

    <c:if test="${param.success == 'true'}">
        <script>
            $(document).ready(function() {
                if (typeof window.showToast === 'function') {
                    window.showToast('success', 'THÀNH CÔNG: Thực hiện giao dịch thay đổi tồn kho vật lý thành công!');
                }
            });
        </script>
    </c:if>

    <!-- BỘ LỌC NÂNG CAO (SEARCH BY COLUMN) -->
    <div class="card mt-4">
        <div class="card-body">
            <div class="row g-3">
                <div class="col-md-4">
                    <label class="form-label">Tìm theo tên sản phẩm</label>
                    <div class="input-icon">
                        <input type="text" id="searchProduct" class="form-control" placeholder="Nhập tên sản phẩm...">
                        <span class="input-icon-addon">
                            <i class="ti ti-search"></i>
                        </span>
                    </div>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Loại giao dịch</label>
                    <select id="filterType" class="form-select">
                        <option value="">-- Tất cả loại --</option>
                        <option value="IMPORT">📥 NHẬP KHO</option>
                        <option value="EXPORT">📤 XUẤT KHO</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Trạng thái phiếu</label>
                    <select id="filterStatus" class="form-select">
                        <option value="">-- Tất cả trạng thái --</option>
                        <option value="COMPLETED">Hoạt động</option>
                        <option value="VOIDED">Đã hủy</option>
                    </select>
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <button type="button" id="resetFilter" class="btn btn-outline-secondary w-100">
                        <i class="ti ti-refresh me-1"></i> Làm mới
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- BẢNG NHẬT KÝ CHI TIẾT (DTO LÊN BẢNG) -->
    <div class="card mt-4">
        <div class="card-body p-0">
            <div class="table-responsive">
                <table id="inventoryTable" class="table table-vcenter card-table">
                    <thead>
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
                                    <span class="avatar me-3 rounded" style="background-image: url(${pageContext.request.contextPath}/${tx.imageURL}); background-size: cover; background-position: center;"
                                          onerror="this.style.backgroundImage='url(https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=200&auto=format&fit=crop)'"></span>
                                    <div>
                                        <div class="text-truncate">${tx.productName}</div>
                                        <div class="text-muted text-truncate mt-n1">Mã sản phẩm: #${tx.productId}</div>
                                    </div>
                                </div>
                            </td>
                            <!-- Gắn data-search để DataTables lọc theo code thay vì icon -->
                            <td class="text-center" data-search="${tx.transactionType}">
                                <span class="badge ${tx.transactionType == 'IMPORT' ? 'bg-success' : 'bg-danger'}">
                                    ${tx.transactionType == 'IMPORT' ? '📥 NHẬP KHO' : '📤 XUẤT KHO'}
                                </span>
                            </td>
                            <td class="text-center fw-bold">${tx.quantity}</td>
                            <td>
                                <fmt:formatNumber value="${tx.price}" type="number" maxFractionDigits="0"/> VNĐ
                            </td>
                            <td>
                                <div class="text-truncate">${tx.reason}</div>
                                <div class="text-muted text-truncate mt-n1"><i class="ti ti-clock me-1"></i>${tx.createdAt}</div>
                            </td>
                            <!-- Gắn data-search để lọc trạng thái chuẩn xác -->
                            <td data-search="${tx.status}">
                                <c:if test="${tx.status != 'VOIDED'}">
                                    <form action="${pageContext.request.contextPath}/admin/inventory" method="POST" class="void-transaction-form">
                                        <input type="hidden" name="action" value="void">
                                        <input type="hidden" name="id" value="${tx.id}">
                                        <button class="btn btn-icon btn-sm btn-danger" type="submit">
                                            <i class="ti ti-x"></i>
                                        </button>
                                    </form>
                                </c:if>
                                <c:if test="${tx.status == 'VOIDED'}"><span class="badge bg-secondary">Đã hủy</span></c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    <%-- Đã loại bỏ khối c:if test="${empty transactions}" để DataTables tự xử lý thông báo không có dữ liệu --%>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</div>

<!-- ==================== MODAL TẠO PHIẾU NHẬP / XUẤT KHO THỦ CÔNG ==================== -->
<div class="modal modal-blur fade" id="createTxModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><i class="ti ti-pencil-plus me-2"></i> Tạo Phiếu Biến Động Kho</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
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
                        <label class="form-label">Hình thức giao dịch:</label>
                        <select name="transactionType" class="form-select" required>
                            <option value="IMPORT">📥 Nhập Kho (IMPORT)</option>
                            <option value="EXPORT">📤 Xuất Kho (EXPORT)</option>
                        </select>
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Số lượng:</label>
                            <input type="number" name="quantity" class="form-control" required min="1">
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Giá áp dụng (VNĐ):</label>
                            <input type="number" name="price" class="form-control" required min="0">
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Lý do:</label>
                        <textarea name="reason" class="form-control" rows="3" required></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn me-auto" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary">
                        <i class="ti ti-device-floppy me-2"></i> Lưu phiếu
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    $(document).ready(function() {
        $('.void-transaction-form').on('submit', function(e) {
            e.preventDefault();
            const form = this;
            const transactionId = $(form).find('input[name="id"]').val();
            const message = `Bạn có chắc muốn HỦY phiếu giao dịch kho (ID: ${transactionId}) này không? Hành động này không thể hoàn tác!`;

            window.showConfirmationModal(message, function() {
                form.submit(); // Gửi form nếu xác nhận
            });
        });
    });
</script>