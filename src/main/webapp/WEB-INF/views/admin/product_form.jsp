<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container-fluid">
  <div class="card shadow-sm border-0 mt-4">
    <div class="card-header bg-white py-3">
      <h4 class="mb-0">${product == null ? 'Thêm trang sức mới' : 'Chỉnh sửa thông tin'}</h4>
    </div>
    <div class="card-body p-4">
      <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
          <i class="bi bi-exclamation-triangle-fill"></i> <strong>Lỗi:</strong> ${error}
          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
      </c:if>

      <form action="${pageContext.request.contextPath}/admin/products" method="POST" enctype="multipart/form-data">
        <input type="hidden" name="action" value="${product == null ? 'add' : 'update'}">
        <c:if test="${product != null}">
          <input type="hidden" name="id" value="${product.id}">
        </c:if>

        <div class="row">
          <div class="col-md-6 mb-3">
            <label class="form-label fw-bold">Tên sản phẩm</label>
            <input type="text" name="name" class="form-control" value="${product.name}" required>
          </div>
          <div class="col-md-3 mb-3">
            <label class="form-label fw-bold">Giá bán (VNĐ)</label>
            <input type="number" name="price" class="form-control" value="${product.price}" required>
          </div>

          <%-- Conditional fields for Quantity and Cost Price --%>
          <c:choose>
            <c:when test="${product == null}"> <%-- Add New Product --%>
              <div class="col-md-3 mb-3">
                <label class="form-label fw-bold">Số lượng nhập ban đầu</label>
                <input type="number" name="initialQuantity" class="form-control" value="0" min="0" required>
              </div>
              <div class="col-md-3 mb-3">
                <label class="form-label fw-bold">Giá nhập sỉ (VNĐ):</label>
                <input type="number" name="costPrice" class="form-control" value="0" min="0" required>
              </div>
            </c:when>
            <c:otherwise> <%-- Edit Existing Product --%>
              <div class="col-md-3 mb-3">
                <label class="form-label fw-bold">Số lượng tồn hiện tại</label>
                <input type="number" name="quantity" class="form-control" value="${product.quantity}" disabled>
              </div>
            </c:otherwise>
          </c:choose>
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
          <label class="form-label fw-bold">Tải lên ảnh sản phẩm</label>
          <input type="file" name="imageFile" class="form-control" accept="image/*">
          <!-- Giữ lại URL cũ trong ô ẩn để nếu người dùng không up ảnh mới thì không bị mất ảnh cũ -->
          <input type="hidden" name="existingImageUrl" value="${product.imageURL}">
          <c:if test="${product != null && not empty product.imageURL}">
            <div class="mt-2 d-flex align-items-center">
              <img src="${product.imageURL}" alt="Current Image" style="max-height: 100px; border-radius: 5px;" class="me-3" />
              <div class="form-check">
                <input class="form-check-input" type="checkbox" name="deleteCurrentImage" value="true" id="deleteCurrentImage">
                <label class="form-check-label text-danger" for="deleteCurrentImage">
                  Xóa ảnh hiện tại
                </label>
              </div>
            </div>
          </c:if>
        </div>

        <div class="mb-3">
          <label class="form-label fw-bold">Link YouTube</label>
          <input type="text" name="youtubeUrl" class="form-control" value="${product.youtubeURL}">
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