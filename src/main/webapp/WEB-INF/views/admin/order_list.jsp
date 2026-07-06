<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mt-4 mb-4">
        <h2>Danh sách đơn hàng</h2>
    </div>
    <table id="orderTable" class="table table-bordered table-hover shadow-sm bg-white">
        <thead class="table-dark">
        <tr>
            <th>ID</th>
            <th>Khách hàng</th>
            <th>Điện thoại</th>
            <th>Địa chỉ</th>
            <th>Tổng tiền</th>
            <th>Trạng thái</th>
            <th>Thao tác</th>
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
                        <c:when test="${o.status == 'SUCCESS'}"><span class="badge bg-success">Đã giao</span></c:when>
                        <c:when test="${o.status == 'SHIPPING'}"><span class="badge bg-info">Đang giao</span></c:when>
                        <c:when test="${o.status == 'CANCELLED'}"><span class="badge bg-danger">Đã hủy</span></c:when>
                        <c:otherwise><span class="badge bg-warning">Chờ xử lý</span></c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/orders?action=details&id=${o.id}" class="btn btn-info btn-sm">
                        <i class="bi bi-eye-fill me-1"></i> Chi tiết
                    </a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty orders}">
            <tr>
                <td colspan="7" class="text-center py-4 text-muted">
                    <i class="bi bi-inbox fs-1 d-block mb-2"></i> Chưa có đơn hàng nào.
                </td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>
