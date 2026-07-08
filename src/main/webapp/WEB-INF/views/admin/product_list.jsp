<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<div class="container-xl">
    <div class="card">
        <div class="card-header">
            <h3 class="card-title">Danh sách sản phẩm</h3>
            <div class="card-actions">
                <a href="${pageContext.request.contextPath}/admin/products?action=create" class="btn btn-primary">
                    <i class="ti ti-plus me-1"></i> Thêm sản phẩm mới
                </a>
            </div>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table id="productTable" class="table table-vcenter card-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tên</th>
                        <th>Hình ảnh</th>
                        <th>Giá</th>
                        <th>Số lượng</th>
                        <th class="w-1">Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="p" items="${products}">
                        <tr>
                            <td>${p.id}</td>
                            <td>${p.name}</td>
                            <td>
                                <%-- Sửa URL hình ảnh: bỏ ${pageContext.request.contextPath} --%>
                                <span class="avatar avatar-sm" style="background-image: url(${p.imageURL}); background-size: cover; background-position: center;"
                                      onerror="this.style.backgroundImage='url(https://via.placeholder.com/40x40?text=No+Image)'"></span>
                            </td>
                            <td><fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/> VNĐ</td>
                            <td>${p.quantity}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/products?action=edit&id=${p.id}" class="btn btn-icon btn-sm btn-warning">
                                    <i class="ti ti-edit"></i>
                                </a>
                                <a href="#" class="btn btn-icon btn-sm btn-danger delete-product-btn"
                                   data-id="${p.id}"
                                   data-name="${p.name}">
                                    <i class="ti ti-trash"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <%-- Đã loại bỏ hoàn toàn khối c:choose/c:otherwise. DataTables sẽ tự động hiển thị thông báo khi không có dữ liệu. --%>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function() {
        $('.delete-product-btn').on('click', function(e) {
            e.preventDefault();
            const productId = $(this).data('id');
            const productName = $(this).data('name');
            const deleteUrl = `${contextPath}/admin/products?action=delete&id=${productId}`;
            const message = `Bạn có chắc muốn xóa sản phẩm <strong>${productName}</strong> (ID: ${productId}) không? Dữ liệu đã xóa sẽ không thể phục hồi!`;

            window.showConfirmationModal(message, function() {
                window.location.href = deleteUrl;
            });
        });
    });
</script>