<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<div class="container-xl">
    <c:if test="${not empty error}">
        <script>
            $(document).ready(function() {
                if (typeof window.showToast === 'function') {
                    window.showToast('danger', '${error}');
                }
            });
        </script>
    </c:if>

    <c:if test="${param.success == 1}">
        <script>
            $(document).ready(function() {
                if (typeof window.showToast === 'function') {
                    window.showToast('success', 'Cập nhật trạng thái thành công!');
                }
            });
        </script>
    </c:if>

    <div class="card mt-4">
        <div class="card-header">
            <h3 class="card-title">Chi tiết đơn hàng #${order.id}</h3>
            <div class="card-actions">
                <a href="${pageContext.request.contextPath}/admin/orders?action=list" class="btn btn-secondary">
                    <i class="ti ti-arrow-left me-2"></i> Quay lại
                </a>
            </div>
        </div>
        <div class="card-body">
            <div class="row">
                <div class="col-md-6">
                    <h5 class="card-title">Thông tin khách hàng</h5>
                    <p><strong>Họ tên:</strong> ${order.customerName}</p>
                    <p><strong>Số điện thoại:</strong> ${order.customerPhone}</p>
                    <p><strong>Địa chỉ:</strong> ${order.customerAddress}</p>
                </div>
                <div class="col-md-6">
                    <h5 class="card-title">Trạng thái đơn hàng</h5>
                    <form id="updateOrderStatusForm" action="${pageContext.request.contextPath}/admin/orders" method="POST">
                        <input type="hidden" name="action" value="updateStatus">
                        <input type="hidden" name="id" value="${order.id}">
                        <div class="input-group">
                            <select name="status" class="form-select">
                                <option value="PENDING" ${order.status == 'PENDING' ? 'selected' : ''}>Chờ xử lý</option>
                                <option value="DELIVERING" ${order.status == 'DELIVERING' ? 'selected' : ''}>Đang giao</option>
                                <option value="DELIVERED" ${order.status == 'DELIVERED' ? 'selected' : ''}>Đã giao</option>
                                <option value="CANCELLED" ${order.status == 'CANCELLED' ? 'selected' : ''}>Đã hủy</option>
                            </select>
                            <button type="submit" name="submitUpdateStatus" class="btn btn-primary">
                                <i class="ti ti-refresh me-2"></i> Cập nhật
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <hr class="my-4">

            <h5 class="card-title">Sản phẩm trong đơn</h5>
            <div class="table-responsive">
                <table class="table table-vcenter card-table">
                    <thead>
                    <tr>
                        <th>Hình ảnh</th> <%-- Thêm cột Hình ảnh --%>
                        <th>Tên sản phẩm</th>
                        <th>Số lượng</th>
                        <th>Giá mua</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${items}">
                        <tr>
                            <td> <%-- Hiển thị hình ảnh sản phẩm --%>
                                <span class="avatar avatar-sm" style="background-image: url(${item.imageURL}); background-size: cover; background-position: center;"
                                      onerror="this.style.backgroundImage='url(https://via.placeholder.com/40x40?text=No+Image)'"></span>
                            </td>
                            <td class="fw-bold text-dark">
                                <c:choose>
                                    <c:when test="${not empty item.productName}">
                                        ${item.productName}
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">Sản phẩm #${item.productId} (Đã bị xóa hoặc không tìm thấy)</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${item.quantity}</td>
                            <td><fmt:formatNumber value="${item.priceAtPurchase}" type="number" maxFractionDigits="0"/> VNĐ</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="d-flex justify-content-end mt-3">
                <h4>Tổng tiền: <span class="text-danger"><fmt:formatNumber value="${order.totalPrice}" type="number" maxFractionDigits="0"/> VNĐ</span></h4>
            </div>
        </div>
    </div>

</div>