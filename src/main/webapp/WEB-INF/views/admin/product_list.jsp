<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Quản lý sản phẩm</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-4">

<div class="d-flex justify-content-between align-items-center mb-3">
  <h2>Danh sách sản phẩm</h2>
  <%-- Nút chuyển hướng sang trang thêm mới --%>
  <a href="${pageContext.request.contextPath}/admin/products?action=create" class="btn btn-primary">Thêm sản phẩm mới</a>
</div>

<table class="table table-bordered table-hover">
  <thead class="table-light">
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
      <td>${p.price}</td>
      <td>${p.quantity}</td>
      <td>
          <%-- Nút Chỉnh sửa --%>
        <a href="${pageContext.request.contextPath}/admin/products?action=edit&id=${p.id}" class="btn btn-warning btn-sm">Sửa</a>

          <%-- Nút Xóa --%>
        <a href="${pageContext.request.contextPath}/admin/products?action=delete&id=${p.id}"
           class="btn btn-danger btn-sm"
           onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này không?')">Xóa</a>
      </td>
    </tr>
  </c:forEach>
  </tbody>
</table>

</body>
</html>