<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container-xl">
  <div class="card mt-4">
    <div class="card-header">
      <h3 class="card-title">${product == null ? 'Thêm sản phẩm mới' : 'Chỉnh sửa thông tin sản phẩm'}</h3>
    </div>
    <div class="card-body">
      <%-- Thay thế alert bằng script gọi showToast --%>
      <c:if test="${not empty error}">
        <script>
          $(document).ready(function() {
            if (typeof window.showToast === 'function') {
              window.showToast('danger', '${error}');
            }
          });
        </script>
      </c:if>

      <form action="${pageContext.request.contextPath}/admin/products" method="POST" enctype="multipart/form-data">
        <input type="hidden" name="action" value="${product == null ? 'add' : 'update'}">
        <c:if test="${product != null}">
          <input type="hidden" name="id" value="${product.id}">
        </c:if>

        <div class="row">
          <div class="col-md-6 mb-3">
            <label class="form-label">Tên sản phẩm</label>
            <input type="text" name="name" class="form-control" value="${product.name}" required>
          </div>
          <div class="col-md-3 mb-3">
            <label class="form-label">Giá bán (VNĐ)</label>
            <input type="number" name="price" class="form-control" value="${product.price}" required>
          </div>

          <%-- Conditional fields for Quantity and Cost Price --%>
          <c:choose>
            <c:when test="${product == null}"> <%-- Add New Product --%>
              <div class="col-md-3 mb-3">
                <label class="form-label">Số lượng nhập ban đầu</label>
                <input type="number" name="initialQuantity" class="form-control" value="0" min="0" required>
              </div>
              <div class="col-md-3 mb-3">
                <label class="form-label">Giá nhập sỉ (VNĐ)</label>
                <input type="number" name="costPrice" class="form-control" value="0" min="0" required>
              </div>
            </c:when>
            <c:otherwise> <%-- Edit Existing Product --%>
              <div class="col-md-3 mb-3">
                <label class="form-label">Số lượng tồn hiện tại</label>
                <input type="number" name="quantity" class="form-control" value="${product.quantity}" disabled>
              </div>
            </c:otherwise>
          </c:choose>
        </div>

        <div class="mb-3">
          <label class="form-label">Chất liệu</label>
          <input type="text" name="material" class="form-control" value="${product.material}">
        </div>

        <!-- Phần chọn Mệnh -->
        <div class="mb-3">
          <label class="form-label d-block">Mệnh hợp (Chọn nhiều):</label>
          <div class="form-fieldset">
            <c:set var="allElements" value="${['KIM', 'MOC', 'THUY', 'HOA', 'THO']}" />
            <c:forEach var="el" items="${allElements}">
              <div class="form-check form-check-inline">
                <input class="form-check-input" type="checkbox" name="elements" value="${el}" id="check_${el}"
                       <c:if test="${productElements != null && productElements.contains(el)}">checked</c:if>>
                <label class="form-check-label" for="check_${el}">Mệnh ${el}</label>
              </div>
            </c:forEach>
          </div>
        </div>

        <div class="mb-3">
          <label class="form-label">Tải lên ảnh sản phẩm</label>
          <input type="file" name="imageFile" class="form-control" accept="image/*">
          <!-- Giữ lại URL cũ trong ô ẩn để nếu người dùng không up ảnh mới thì không bị mất ảnh cũ -->
          <input type="hidden" name="existingImageUrl" value="${product.imageURL}">
          <c:if test="${product != null && not empty product.imageURL}">
            <div class="mt-2 d-flex align-items-center">
              <%-- Sửa URL hình ảnh: bỏ ${pageContext.request.contextPath} --%>
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
          <label class="form-label">Link YouTube</label>
          <input type="text" name="youtubeUrl" class="form-control" value="${product.youtubeURL}">
        </div>

        <div class="mb-3">
          <label class="form-label">Mô tả</label>
          <textarea name="description" class="form-control" rows="3">${product.description}</textarea>
        </div>

        <div class="d-flex justify-content-between mt-4">
          <a href="${pageContext.request.contextPath}/admin/products?action=list" class="btn btn-secondary">
            <i class="ti ti-arrow-left me-2"></i> Quay lại
          </a>
          <button type="submit" class="btn btn-primary">
            <i class="ti ti-device-floppy me-2"></i> Lưu thông tin
          </button>
        </div>
      </form>
    </div>
  </div>
</div>