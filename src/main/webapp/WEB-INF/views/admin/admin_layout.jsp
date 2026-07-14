<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${title != null ? title : 'Admin Panel'}"/></title>
    <!-- Tabler Core CSS -->
    <link href="https://cdn.jsdelivr.net/npm/@tabler/core@1.0.0-beta17/dist/css/tabler.min.css" rel="stylesheet">
    <!-- Tabler Icons CSS -->
    <link href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@latest/tabler-icons.min.css" rel="stylesheet">
    <!-- Custom Admin CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/admin_style.css" rel="stylesheet">
    <!-- DataTables CSS (for Bootstrap 5 compatibility) -->
    <link href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css" rel="stylesheet">
</head>
<body class="layout-fluid">
<div class="page">
    <!-- Sidebar -->
    <aside class="navbar navbar-vertical navbar-expand-lg navbar-dark">
        <div class="container-fluid">
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#sidebar-menu"
                    aria-controls="sidebar-menu" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <h1 class="navbar-brand navbar-brand-autodark">
                <a href="${pageContext.request.contextPath}/admin/dashboard">
                    <!-- Thay thế logo.svg bằng văn bản -->
                    Fengshui Admin
                </a>
            </h1>
            <div class="navbar-nav flex-row d-lg-none">
                <div class="nav-item dropdown">
                    <a href="#" class="nav-link d-flex lh-1 text-reset p-0" data-bs-toggle="dropdown"
                       aria-label="Open user menu">
                        <!-- Thay thế 000m.jpg bằng avatar placeholder -->
                        <span class="avatar avatar-sm bg-blue-lt">
                            <i class="ti ti-user"></i>
                        </span>
                        <div class="d-none d-xl-block ps-2">
                            <div><c:out value="${sessionScope.currentUser.username}"/></div>
                            <div class="mt-1 small text-muted">Administrator</div>
                        </div>
                    </a>
                    <div class="dropdown-menu dropdown-menu-end dropdown-menu-arrow">
                        <a href="#" class="dropdown-item">Profile</a>
                        <a href="#" class="dropdown-item">Settings</a>
                        <a href="${pageContext.request.contextPath}/logout" class="dropdown-item">Logout</a>
                    </div>
                </div>
            </div>
            <div class="collapse navbar-collapse" id="sidebar-menu">
                <ul class="navbar-nav pt-lg-3">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard">
                            <span class="nav-link-icon d-md-none d-lg-inline-block">
                                <i class="ti ti-dashboard"></i>
                            </span>
                            <span class="nav-link-title">Dashboard</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/products?action=list">
                            <span class="nav-link-icon d-md-none d-lg-inline-block">
                                <i class="ti ti-box"></i>
                            </span>
                            <span class="nav-link-title">Sản phẩm</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/orders?action=list">
                            <span class="nav-link-icon d-md-none d-lg-inline-block">
                                <i class="ti ti-receipt-2"></i>
                            </span>
                            <span class="nav-link-title">Đơn hàng</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/inventory?action=list">
                            <span class="nav-link-icon d-md-none d-lg-inline-block">
                                <i class="ti ti-clipboard-list"></i>
                            </span>
                            <span class="nav-link-title">Nhật ký kho</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">
                            <span class="nav-link-icon d-md-none d-lg-inline-block">
                                <i class="ti ti-users"></i>
                            </span>
                            <span class="nav-link-title">Khách hàng</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">
                            <span class="nav-link-icon d-md-none d-lg-inline-block">
                                <i class="ti ti-settings"></i>
                            </span>
                            <span class="nav-link-title">Cài đặt</span>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </aside>

    <div class="page-wrapper">
        <!-- Navbar -->
        <header class="navbar navbar-expand-md navbar-light d-print-none">
            <div class="container-xl">
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbar-menu"
                        aria-controls="navbar-menu" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="navbar-nav flex-row order-md-last">
                    <!-- CÁI CHUÔNG THÔNG BÁO -->
                    <div class="nav-item dropdown d-none d-md-flex me-3">
                        <a href="#" class="nav-link px-0" data-bs-toggle="dropdown" tabindex="-1" aria-label="Show notifications">
                            <i class="ti ti-bell" style="font-size: 1.5rem;"></i>
                            <span id="notiCount" class="badge bg-red badge-blink" style="display: none; position: absolute; top: 0px; right: 0px;">0</span>
                        </a>
                        <div class="dropdown-menu dropdown-menu-arrow dropdown-menu-end dropdown-menu-card" style="width: 300px; max-height: 400px; overflow-y: auto;">
                            <div class="card">
                                <div class="card-header">
                                    <h3 class="card-title">Lịch sử thông báo</h3>
                                </div>
                                <div class="list-group list-group-flush list-group-hoverable" id="notiHistoryList">
                                    <div class="list-group-item text-center text-muted p-3" id="emptyNoti">Chưa có thông báo nào</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- HẾT CÁI CHUÔNG -->

                    <div class="nav-item dropdown">
                        <a href="#" class="nav-link d-flex lh-1 text-reset p-0" data-bs-toggle="dropdown"
                           aria-label="Open user menu">
                            <!-- Thay thế 000m.jpg bằng avatar placeholder -->
                            <span class="avatar avatar-sm bg-blue-lt">
                                <i class="ti ti-user"></i>
                            </span>
                            <div class="d-none d-xl-block ps-2">
                                <div><c:out value="${sessionScope.currentUser.username}"/></div>
                                <div class="mt-1 small text-muted">Administrator</div>
                            </div>
                        </a>
                        <div class="dropdown-menu dropdown-menu-end dropdown-menu-arrow">
                            <a href="#" class="dropdown-item">Profile</a>
                            <a href="#" class="dropdown-item">Settings</a>
                            <a href="${pageContext.request.contextPath}/logout" class="dropdown-item">Logout</a>
                        </div>
                    </div>
                </div>
                <div class="collapse navbar-collapse" id="navbar-menu">
                    <!-- Breadcrumbs or other navbar content can go here -->
                    <ol class="breadcrumb breadcrumb-alt" aria-label="breadcrumbs">
                        <li class="breadcrumb-item"><a
                                href="${pageContext.request.contextPath}/admin/dashboard">Home</a></li>
                        <li class="breadcrumb-item active" aria-current="page"><a href="#"><c:out
                                value="${title != null ? title : 'Dashboard'}"/></a></li>
                    </ol>
                </div>
            </div>
        </header>

        <div class="page-body">
            <div class="container-xl">
                <!-- Content will be injected here -->
                <jsp:include page="${requestScope.contentPage}"/>
            </div>
        </div>

        <footer class="footer footer-transparent d-print-none">
            <div class="container-xl">
                <div class="row text-center align-items-center flex-row-reverse">
                    <div class="col-lg-auto ms-lg-auto">
                        <ul class="list-inline list-inline-dots mb-0">
                            <li class="list-inline-item"><a href="#" class="link-secondary">Documentation</a></li>
                            <li class="list-inline-item"><a href="#" class="link-secondary">License</a></li>
                            <li class="list-inline-item">
                                <a href="https://github.com/sponsors/codecalm" target="_blank" class="link-secondary"
                                   rel="noopener">
                                    <i class="ti ti-heart text-red"></i>
                                    Sponsor
                                </a>
                            </li>
                        </ul>
                    </div>
                    <div class="col-12 col-lg-auto mt-3 mt-lg-0">
                        <ul class="list-inline list-inline-dots mb-0">
                            <li class="list-inline-item">
                                Copyright &copy; 2023
                                <a href="." class="link-secondary">CodeGym</a>.
                                All rights reserved.
                            </li>
                            <li class="list-inline-item">
                                <a href="#" class="link-secondary" rel="noopener">
                                    v1.0.0
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </footer>
    </div>
</div>

<!-- Toast Container -->
<div class="toast-container position-fixed top-0 end-0 p-3" style="z-index: 1100">
    <!-- Toasts will be appended here -->
</div>

<!-- Custom Confirmation Modal -->
<div class="modal modal-blur fade" id="confirmationModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-sm modal-dialog-centered" role="document">
        <div class="modal-content">
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            <div class="modal-status bg-danger"></div>
            <div class="modal-body text-center py-4">
                <i class="ti ti-alert-triangle icon mb-2 text-danger icon-lg"></i>
                <h3>Bạn có chắc chắn?</h3>
                <div class="text-muted" id="confirmationModalMessage">Bạn có thực sự muốn xóa mục này không? Dữ liệu đã
                    xóa sẽ không thể phục hồi!
                </div>
            </div>
            <div class="modal-footer">
                <div class="w-100">
                    <div class="row">
                        <div class="col">
                            <a href="#" class="btn w-100" data-bs-dismiss="modal">
                                Hủy
                            </a>
                        </div>
                        <div class="col">
                            <a href="#" class="btn btn-danger w-100" id="confirmActionButton">
                                Xác nhận
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Libs JS -->
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
<!-- Tabler Core JS -->
<script src="https://cdn.jsdelivr.net/npm/@tabler/core@1.0.0-beta17/dist/js/tabler.min.js"></script>
<!-- DataTables JS -->
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>
<script>
    // Define contextPath globally for use in JavaScript
    const contextPath = "${pageContext.request.contextPath}";
</script>
<!-- Custom Admin JS -->
<script src="${pageContext.request.contextPath}/assets/js/admin_common.js"></script>
<script>
    var wsUrl = "ws://" + window.location.host + "/fengshui_jewelry/notifications";
    var socket = new WebSocket(wsUrl);

    socket.onopen = function () {
        console.log("Da ket noi WebSocket thanh cong!");
    };

    socket.onclose = function () {
        console.log("Mat ket noi WebSocket.");
    };

    function saveToHistory(orderData) {
        var history = [];
        try {
            var rawData = localStorage.getItem('notificationHistory');
            if (rawData) history = JSON.parse(rawData);
            if (!Array.isArray(history)) history = [];
        } catch(e) {
            history = [];
        }
        history.unshift(orderData);
        if (history.length > 50) history.pop();
        try {
            localStorage.setItem('notificationHistory', JSON.stringify(history));
        } catch(e) {}
    }

    function loadNotificationHistory() {
        var history = [];
        try {
            var rawData = localStorage.getItem('notificationHistory');
            if (rawData) history = JSON.parse(rawData);
            if (!Array.isArray(history)) history = [];
        } catch(e) {
            history = [];
        }

        var historyList = document.getElementById("notiHistoryList");
        var emptyNoti = document.getElementById("emptyNoti");

        if (history.length > 0 && emptyNoti) {
            emptyNoti.style.display = 'none';
        }

        if (historyList && history.length > 0) {
            var html = "";
            for (var i = 0; i < history.length; i++) {
                var od = history[i];
                html += '<div class="list-group-item">'
                    + '<div class="row align-items-center">'
                    + '<div class="col-auto"><span class="status-dot bg-secondary d-block"></span></div>'
                    + '<div class="col text-truncate">'
                    + '<a href="#" class="text-body d-block">Don hang #' + (od.orderId || 'N/A') + '</a>'
                    + '<div class="d-block text-muted text-truncate mt-n1">'
                    + 'Khach: ' + (od.customerName || 'N/A') + ' - Gia: ' + (od.totalPrice || '0') + ' VND'
                    + '</div></div></div></div>';
            }
            historyList.insertAdjacentHTML('beforeend', html);
        }
    }

    function buildNotiRow(od) {
        return '<div class="list-group-item">'
            + '<div class="row align-items-center">'
            + '<div class="col-auto"><span class="status-dot status-dot-animated bg-red d-block"></span></div>'
            + '<div class="col text-truncate">'
            + '<a href="#" class="text-body d-block">Don hang moi #' + od.orderId + '</a>'
            + '<div class="d-block text-muted text-truncate mt-n1">'
            + 'Khach: ' + od.customerName + ' - Gia: ' + od.totalPrice + ' VND'
            + '</div></div></div></div>';
    }

    function buildActionBtn(orderId) {
        return '<a href="' + contextPath + '/admin/orders?action=details&id=' + orderId + '" class="btn btn-icon btn-sm btn-info"><i class="ti ti-eye"></i></a>';
    }

    socket.onmessage = function (event) {
        var orderData = JSON.parse(event.data);

        var message = "Don hang moi #" + orderData.orderId
            + " tu khach " + orderData.customerName
            + " (" + orderData.totalPrice + " VND)";
        showToast("success", message);

        saveToHistory(orderData);

        if ($.fn.DataTable.isDataTable('#orderTable')) {
            var table = $('#orderTable').DataTable();
            var formattedPrice = new Intl.NumberFormat('vi-VN').format(orderData.totalPrice) + " VND";

            table.row.add([
                orderData.orderId,
                orderData.customerName,
                orderData.customerPhone || "",
                orderData.customerAddress || "",
                formattedPrice,
                '<span class="badge bg-warning">Cho xu ly</span>',
                buildActionBtn(orderData.orderId)
            ]).draw(false);
        }

        var notiCountBadge = document.getElementById("notiCount");
        if (notiCountBadge) {
            var currentCount = parseInt(notiCountBadge.innerText) || 0;
            notiCountBadge.innerText = currentCount + 1;
            notiCountBadge.style.display = 'inline-block';
        }

        var emptyNoti = document.getElementById("emptyNoti");
        if (emptyNoti) emptyNoti.style.display = 'none';

        var historyList = document.getElementById("notiHistoryList");
        if (historyList) {
            var newRow = buildNotiRow(orderData);
            if (emptyNoti && emptyNoti.nextSibling) {
                emptyNoti.insertAdjacentHTML('afterend', newRow);
            } else {
                historyList.insertAdjacentHTML('beforeend', newRow);
            }
        }
    };

    document.addEventListener("DOMContentLoaded", function() {
        loadNotificationHistory();

        var bellIcon = document.querySelector('[aria-label="Show notifications"]');
        if (bellIcon) {
            bellIcon.addEventListener('click', function() {
                var badge = document.getElementById("notiCount");
                if (badge) {
                    badge.innerText = "0";
                    badge.style.display = "none";
                }
            });
        }
    });
</script>
</body>
</html>