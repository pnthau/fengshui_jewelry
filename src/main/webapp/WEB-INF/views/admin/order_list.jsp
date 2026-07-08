<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<div class="container-xl">
    <div class="card">
        <div class="card-header">
            <h3 class="card-title">Danh sách đơn hàng</h3>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table id="orderTable" class="table table-vcenter card-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Khách hàng</th>
                        <th>Điện thoại</th>
                        <th>Địa chỉ</th>
                        <th>Tổng tiền</th>
                        <th>Trạng thái</th>
                        <th class="w-1">Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="o" items="${orders}">
                        <tr>
                            <td>${o.id}</td>
                            <td>${o.customerName}</td>
                            <td>${o.customerPhone}</td>
                            <td>${o.customerAddress}</td>
                            <td><fmt:formatNumber value="${o.totalPrice}" type="number" maxFractionDigits="0"/> VNĐ</td>
                            <td>
                                <c:choose>
                                    <c:when test="${o.status == 'PENDING'}"><span class="badge bg-warning">Chờ xử lý</span></c:when>
                                    <c:when test="${o.status == 'DELIVERING'}"><span class="badge bg-info">Đang giao</span></c:when>
                                    <c:when test="${o.status == 'DELIVERED'}"><span class="badge bg-success">Đã giao</span></c:when>
                                    <c:when test="${o.status == 'CANCELLED'}"><span class="badge bg-danger">Đã hủy</span></c:when>
                                    <c:otherwise><span class="badge bg-secondary">Không xác định</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/orders?action=details&id=${o.id}" class="btn btn-icon btn-sm btn-info">
                                    <i class="ti ti-eye"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <%-- Đã loại bỏ khối c:if test="${empty orders}" để DataTables tự xử lý thông báo không có dữ liệu --%>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function() {
        // Check for success parameter in URL and show toast
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get('success') === '1') {
            if (typeof window.showToast === 'function') {
                window.showToast('success', 'Cập nhật trạng thái đơn hàng thành công!');
            }
        }
    });
</script>