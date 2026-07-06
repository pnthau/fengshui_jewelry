<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${title != null ? title : 'Admin Panel'}" /></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/admin_style.css" rel="stylesheet">
</head>
<body>
<div class="d-flex" id="wrapper">
    <!-- Sidebar -->
    <div class="bg-dark border-right" id="sidebar-wrapper">
        <div class="sidebar-heading">Fengshui Jewelry Admin</div>
        <div class="list-group list-group-flush">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="list-group-item list-group-item-action bg-dark text-light">
                <i class="bi bi-speedometer2 me-2"></i>Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/admin/products?action=list" class="list-group-item list-group-item-action bg-dark text-light">
                <i class="bi bi-box-seam me-2"></i>Sản phẩm
            </a>
            <a href="${pageContext.request.contextPath}/admin/orders?action=list" class="list-group-item list-group-item-action bg-dark text-light">
                <i class="bi bi-receipt-cutoff me-2"></i>Đơn hàng
            </a>
            <a href="${pageContext.request.contextPath}/admin/inventory?action=list" class="list-group-item list-group-item-action bg-dark text-light">
                <i class="bi bi-journal-album me-2"></i>Nhật ký kho
            </a>
            <a href="#" class="list-group-item list-group-item-action bg-dark text-light">
                <i class="bi bi-people me-2"></i>Khách hàng
            </a>
            <a href="#" class="list-group-item list-group-item-action bg-dark text-light">
                <i class="bi bi-gear me-2"></i>Cài đặt
            </a>
        </div>
    </div>
    <!-- /#sidebar-wrapper -->

    <!-- Page Content -->
    <div id="page-content-wrapper">
        <nav class="navbar navbar-expand-lg navbar-light bg-light border-bottom">
            <button class="btn btn-primary" id="sidebarToggle"><i class="bi bi-list"></i></button>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav ms-auto mt-2 mt-lg-0">
                    <li class="nav-item active">
                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard">Home <i class="bi bi-house"></i></a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <c:out value="${sessionScope.currentUser.username}" />
                        </a>
                        <div class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item" href="#">Profile</a>
                            <a class="dropdown-item" href="#">Settings</a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Logout</a>
                        </div>
                    </li>
                </ul>
            </div>
        </nav>

        <div class="container-fluid p-4">
            <!-- Content will be injected here -->
            <jsp:include page="${requestScope.contentPage}" />
        </div>
    </div>
    <!-- /#page-content-wrapper -->
</div>
<!-- /#wrapper -->

<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- DataTables JS -->
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/admin_common.js"></script>
</body>
</html>
