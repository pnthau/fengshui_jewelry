<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Quản lý đơn hàng - Phong Thủy Hậu</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light mt-4">
<div class="container">

  <c:choose>
    <%-- Giao diện chi tiết đơn hàng --%>
    <c:when test="${not empty order}">
      <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show border-danger shadow-sm mb-4" role="alert">
          <i class="bi bi-exclamation-triangle-fill me-2 fs-5"></i>
          <strong>⚠️ CẢNH BÁO:</strong> ${error}
          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
      </c:if>

      <div class="card shadow">
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
                    <option value="Chờ xử lý" ${order.status == 'Chờ xử lý' ? 'selected' : ''}>Chờ xử lý</option>
                    <option value="Đang giao" ${order.status == 'Đang giao' ? 'selected' : ''}>Đang giao</option>
                    <option value="Đã giao" ${order.status == 'Đã giao' ? 'selected' : ''}>Đã giao</option>
                    <option value="Đã hủy" ${order.status == 'Đã hủy' ? 'selected' : ''}>Đã hủy</option>
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
                <!-- Thay vì hiển thị mã ID sản phẩm khô khan, giờ đây chúng ta hiển thị tên sản phẩm cực kỳ trực quan -->
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
    </c:when>

    <%-- Giao diện danh sách đơn hàng --%>
    <c:otherwise>
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Danh sách đơn hàng</h2>
        <a href="${pageContext.request.contextPath}/admin/products?action=list" class="btn btn-outline-primary">
          <i class="bi bi-box-seam me-1"></i> Quản lý sản phẩm
        </a>
      </div>
      <table class="table table-bordered table-hover shadow-sm bg-white">
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
              <span class="badge ${o.status == 'Đã giao' ? 'bg-success' : o.status == 'Đang giao' ? 'bg-info' : o.status == 'Đã hủy' ? 'bg-danger' : 'bg-warning'}">
                  ${o.status}
              </span>
            </td>
            <td>
              <a href="${pageContext.request.contextPath}/admin/orders?action=details&id=${o.id}" class="btn btn-info btn-sm">
                <i class="bi bi-eye-fill me-1"></i> Chi tiết
              </a>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:otherwise>
  </c:choose>

</div>
</body>
</html>