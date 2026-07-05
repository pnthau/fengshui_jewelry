<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mt-4 mb-3">
        <h2>Danh sách sản phẩm</h2>
        <a href="${pageContext.request.contextPath}/admin/products?action=create" class="btn btn-primary">
            <i class="bi bi-plus-circle me-1"></i> Thêm sản phẩm mới
        </a>
    </div>

    <table id="productTable" class="table table-bordered table-hover shadow-sm bg-white">
        <thead class="table-dark">
        <tr>
            <th>ID</th>
            <th>Tên</th>
            <th>Giá</th>
            <th>Số lượng</th>
            <th>Thao tác</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="p" items="${products}">
            <tr>
                <td>${p.id}</td>
                <td>${p.name}</td>
                <td><fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/> VNĐ</td>
                <td>${p.quantity}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/products?action=edit&id=${p.id}" class="btn btn-warning btn-sm">
                        <i class="bi bi-pencil-square"></i> Sửa
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/products?action=delete&id=${p.id}"
                       class="btn btn-danger btn-sm"
                       onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này không?')">
                        <i class="bi bi-trash"></i> Xóa
                    </a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty products}">
            <tr>
                <td colspan="5" class="text-center py-4 text-muted">
                    <i class="bi bi-box-seam fs-1 d-block mb-2"></i> Chưa có sản phẩm nào.
                </td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>
