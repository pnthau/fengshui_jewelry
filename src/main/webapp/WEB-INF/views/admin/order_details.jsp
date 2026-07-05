<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<div class="container-fluid">
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show mt-4" role="alert">
            <i class="bi bi-exclamation-triangle-fill"></i> <strong>Lỗi:</strong> ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${param.success == 1}">
        <div class="alert alert-success alert-dismissible fade show mt-4" role="alert">
            <i class="bi bi-check-circle-fill"></i> Cập nhật trạng thái thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="card shadow mt-4">
        <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
            <h4 class="mb-0">Chi tiết đơn hàng #${order.id}</h4>
            <a href="${pageContext.request.contextPath}/admin/orders?action=list" class="btn btn-light btn-sm">Quay lại</a>
        </div>
        <div class="card-body">
            <div class="row">
                <div class="col-md-6">
                    <h5>Thông tin khách hàng</h5>
                    <p><strong>Họ tên:</strong> ${order.customerName}</p>
                    <p><strong>Số điện thoại:</strong> ${order.customerPhone}</p>
                    <p><strong>Địa chỉ:</strong> ${order.customerAddress}</p>
                </div>
                <div class="col-md-6">
                    <h5>Trạng thái đơn hàng</h5>
                    <form action="${pageContext.request.contextPath}/admin/orders" method="POST">
                        <input type="hidden" name="action" value="updateStatus">
                        <input type="hidden" name="id" value="${order.id}">
                        <div class="input-group">
                            <select name="status" class="form-select">
                                <option value="PENDING" ${order.status == 'PENDING' ? 'selected' : ''}>Chờ xử lý</option>
                                <option value="SHIPPING" ${order.status == 'SHIPPING' ? 'selected' : ''}>Đang giao</option>
                                <option value="SUCCESS" ${order.status == 'SUCCESS' ? 'selected' : ''}>Đã giao</option>
                                <option value="CANCELLED" ${order.status == 'CANCELLED' ? 'selected' : ''}>Đã hủy</option>
                            </select>
                            <button type="submit" class="btn btn-success">Cập nhật</button>
                        </div>
                    </form>
                </div>
            </div>

            <hr>

            <h5>Sản phẩm trong đơn</h5>
            <table class="table table-hover">
                <thead class="table-light">
                <tr>
                    <th>Tên sản phẩm</th>
                    <th>Số lượng</th>
                    <th>Giá mua</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${items}">
                    <tr>
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

            <div class="d-flex justify-content-end mt-3">
                <h4>Tổng tiền: <span class="text-danger"><fmt:formatNumber value="${order.totalPrice}" type="number" maxFractionDigits="0"/> VNĐ</span></h4>
            </div>
        </div>
    </div>

</div>
