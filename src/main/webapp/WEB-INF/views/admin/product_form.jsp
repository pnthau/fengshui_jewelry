<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${product == null ? 'Thêm trang sức mới' : 'Chỉnh sửa thông tin'}</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light mt-4">
<div class="container">
  <div class="card shadow-sm border-0">
    <div class="card-header bg-white py-3">
      <h4 class="mb-0">${product == null ? 'Thêm trang sức mới' : 'Chỉnh sửa thông tin'}</h4>
    </div>
    <div class="card-body p-4">
      <form action="${pageContext.request.contextPath}/admin/products" method="POST">
        <input type="hidden" name="action" value="${product == null ? 'add' : 'update'}">
        <c:if test="${product != null}">
          <input type="hidden" name="id" value="${product.id}">
        </c:if>

        <div class="row">
          <div class="col-md-6 mb-3">
            <label class="form-label fw-bold">Tên sản phẩm</label>
            <input type="text" name="name" class="form-control" value="${product.name}" required>
          </div>
          <!-- Trường nhập liệu cho kho -->
          <div class="col-md-3 mb-3">
            <label class="form-label fw-bold">Giá bán (VNĐ)</label>
            <input type="number" name="price" class="form-control" value="${product.price}" required>
          </div>
          <div class="col-md-3 mb-3">
            <label class="form-label fw-bold">Số lượng tồn</label>
            <input type="number" name="quantity" class="form-control" value="${product.quantity != null ? product.quantity : 0}" required>
          </div>
        </div>

        <div class="mb-3">
          <label class="form-label fw-bold">Chất liệu</label>
          <input type="text" name="material" class="form-control" value="${product.material}">
        </div>

        <!-- Phần chọn Mệnh -->
        <div class="mb-3">
          <label class="form-label fw-bold d-block">Mệnh hợp (Chọn nhiều):</label>
          <div class="p-3 border rounded bg-light">
            <c:set var="allElements" value="${['KIM', 'MOC', 'THUY', 'HOA', 'THO']}" />
            <c:forEach var="el" items="${allElements}">
              <div class="form-check form-check-inline me-3">
                <input class="form-check-input" type="checkbox" name="elements" value="${el}" id="check_${el}"
                       <c:if test="${productElements != null && productElements.contains(el)}">checked</c:if>>
                <label class="form-check-label" for="check_${el}">Mệnh ${el}</label>
              </div>
            </c:forEach>
          </div>
        </div>

        <div class="mb-3">
          <label class="form-label fw-bold">Link hình ảnh</label>
          <input type="text" name="imageUrl" class="form-control" value="${product.imageURL}">
        </div>

        <div class="mb-3">
          <label class="form-label fw-bold">Mô tả</label>
          <textarea name="description" class="form-control" rows="3">${product.description}</textarea>
        </div>

        <div class="d-flex justify-content-between pt-3 border-top">
          <a href="${pageContext.request.contextPath}/admin/products?action=list" class="btn btn-secondary">Quay lại</a>
          <button type="submit" class="btn btn-primary">Lưu thông tin</button>
        </div>
      </form>
    </div>
  </div>
</div>
</body>
</html>