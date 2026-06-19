<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>${product == null ? 'Thêm sản phẩm mới' : 'Chỉnh sửa sản phẩm'}</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-4">

<div class="card shadow">
  <div class="card-header bg-primary text-white">
    <h3>${product == null ? 'Thêm sản phẩm mới' : 'Chỉnh sửa sản phẩm'}</h3>
  </div>
  <div class="card-body">
    <form action="${pageContext.request.contextPath}/admin/products" method="POST">
      <input type="hidden" name="action" value="${product == null ? 'add' : 'update'}">
      <input type="hidden" name="id" value="${product.id}">

      <div class="row">
        <div class="col-md-6 mb-3">
          <label class="form-label">Tên sản phẩm</label>
          <input type="text" name="name" class="form-control" value="${product.name}" required>
        </div>
        <div class="col-md-3 mb-3">
          <label class="form-label">Giá</label>
          <input type="number" step="0.01" name="price" class="form-control" value="${product.price}" required>
        </div>
        <div class="col-md-3 mb-3">
          <label class="form-label">Số lượng</label>
          <input type="number" name="quantity" class="form-control" value="${product.quantity}" required>
        </div>
      </div>

      <div class="row">
        <div class="col-md-6 mb-3">
          <label class="form-label">Chất liệu</label>
          <input type="text" name="material" class="form-control" value="${product.material}">
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Trạng thái</label>
          <input type="text" name="status" class="form-control" value="${product.status}">
        </div>
      </div>

      <div class="mb-3">
        <label class="form-label">Link Ảnh</label>
        <input type="text" name="imageUrl" class="form-control" value="${product.imageURL}">
      </div>

      <div class="mb-3">
        <label class="form-label">Link YouTube</label>
        <input type="text" name="youtubeUrl" class="form-control" value="${product.youtubeURL}">
      </div>

      <div class="mb-3">
        <label class="form-label">Mệnh hợp (Chọn nhiều):</label>
        <div class="form-control">
          <c:set var="allElements" value="${['KIM', 'MOC', 'THUY', 'HOA', 'THO']}" />
          <c:forEach var="el" items="${allElements}">
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="checkbox" name="elements" value="${el}" id="check_${el}"
                     <c:if test="${productElements != null && productElements.contains(el)}">checked</c:if>>
              <label class="form-check-label" for="check_${el}">${el}</label>
            </div>
          </c:forEach>
        </div>
      </div>

      <div class="mb-3">
        <label class="form-label">Mô tả</label>
        <textarea name="description" class="form-control" rows="4">${product.description}</textarea>
      </div>

      <button type="submit" class="btn btn-success">${product == null ? 'Thêm sản phẩm' : 'Cập nhật'}</button>
      <a href="${pageContext.request.contextPath}/admin/products?action=list" class="btn btn-secondary">Quay lại</a>
    </form>
  </div>
</div>
</body>
</html>