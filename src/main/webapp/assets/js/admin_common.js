/* admin_common.js */

$(document).ready(function() {
    // Function to initialize DataTables
    // Thêm tham số customEmptyMessage
    function initializeDataTable(tableId, columnDefs, customEmptyMessage = "Không có dữ liệu nào được tìm thấy") {
        if ($(tableId).length) {
            $(tableId).DataTable({
                "language": {
                    "emptyTable": customEmptyMessage, // Sử dụng thông báo tùy chỉnh
                    "info": "Hiển thị _START_ đến _END_ của _TOTAL_ mục",
                    "infoEmpty": "Hiển thị 0 đến 0 của 0 mục",
                    "infoFiltered": "(được lọc từ _MAX_ tổng số mục)",
                    "lengthMenu": "Hiển thị _MENU_ mục",
                    "loadingRecords": "Đang tải...",
                    "processing": "Đang xử lý...",
                    "search": "Tìm kiếm:",
                    "zeroRecords": "Không tìm thấy kết quả phù hợp",
                    "paginate": {
                        "first": "Đầu",
                        "last": "Cuối",
                        "next": "Tiếp",
                        "previous": "Trước"
                    }
                    // Nếu muốn Việt hóa đầy đủ, bạn có thể tải file vi.json về host cục bộ và uncomment dòng dưới:
                    // "url": "${pageContext.request.contextPath}/assets/i18n/vi.json"
                },
                "order": [[0, "desc"]], // Mặc định sắp xếp theo ID giảm dần
                "pageLength": 10, // Số dòng trên mỗi trang
                "columnDefs": columnDefs,
                // Loại bỏ tùy chọn 'dom' để tránh xung đột với Tabler/Bootstrap 5 CSS
            });
        }
    }

    // Initialize DataTables for productTable
    initializeDataTable(
        '#productTable',
        [{ "orderable": false, "targets": 5 }], // Vô hiệu hóa sắp xếp cột "Thao tác" (cột thứ 6, index 5)
        `
        <div class="text-center py-4 text-muted">
            <i class="ti ti-box-off fs-1 d-block mb-2"></i>
            Chưa có sản phẩm nào.
            <a href="${contextPath}/admin/products?action=create" class="btn btn-primary mt-2">
                <i class="ti ti-plus me-1"></i> Thêm sản phẩm mới ngay
            </a>
        </div>
        `
    );

    // Initialize DataTables for orderTable
    initializeDataTable(
        '#orderTable',
        [{ "orderable": false, "targets": 6 }], // Vô hiệu hóa sắp xếp cột "Thao tác"
        `
        <div class="text-center py-4 text-muted">
            <i class="ti ti-inbox-off fs-1 d-block mb-2"></i>
            Chưa có đơn hàng nào được ghi nhận.
        </div>
        `
    );

    // Initialize DataTables for inventoryTable
    initializeDataTable(
        '#inventoryTable',
        [{ "orderable": false, "targets": 6 }], // Vô hiệu hóa sắp xếp cột "Thao tác"
        `
        <div class="text-center py-4 text-muted">
            <i class="ti ti-clipboard-off fs-1 d-block mb-2"></i>
            Chưa có giao dịch xuất nhập kho nào được ghi nhận.
            <button type="button" class="btn btn-primary mt-2" data-bs-toggle="modal" data-bs-target="#createTxModal">
                <i class="ti ti-plus me-1"></i> Tạo Phiếu Kho ngay
            </button>
        </div>
        `
    );


    // Inventory specific filters (only apply if inventoryTable exists)
    if ($('#inventoryTable').length) {
        var inventoryTable = $('#inventoryTable').DataTable();

        $('#searchProduct').on('keyup', function() {
            inventoryTable.column(1).search(this.value).draw();
        });

        $('#filterType').on('change', function() {
            var val = $(this).val();
            inventoryTable.column(2).search(val ? '^' + val + '$' : '', true, false).draw();
        });

        $('#filterStatus').on('change', function() {
            var val = $(this).val();
            inventoryTable.column(6).search(val ? '^' + val + '$' : '', true, false).draw();
        });

        $('#resetFilter').on('click', function() {
            $('#searchProduct').val('');
            $('#filterType').val('');
            $('#filterStatus').val('');
            inventoryTable.columns().search('').draw();
        });
    }

    // --- Toast Notification System ---
    // Function to show a Bootstrap Toast
    window.showToast = function(type, message) {
        let iconClass = '';
        let bgColorClass = '';
        let title = '';

        switch (type) {
            case 'success':
                iconClass = 'ti ti-check';
                bgColorClass = 'bg-success text-white';
                title = 'Thành công';
                break;
            case 'error':
            case 'danger':
                iconClass = 'ti ti-alert-triangle';
                bgColorClass = 'bg-danger text-white';
                title = 'Lỗi';
                break;
            case 'warning':
                iconClass = 'ti ti-alert-circle';
                bgColorClass = 'bg-warning text-dark';
                title = 'Cảnh báo';
                break;
            case 'info':
            default:
                iconClass = 'ti ti-info-circle';
                bgColorClass = 'bg-info text-white';
                title = 'Thông báo';
                break;
        }

        const toastHtml = `
            <div class="toast align-items-center ${bgColorClass} border-0" role="alert" aria-live="assertive" aria-atomic="true" data-bs-delay="3000">
                <div class="d-flex">
                    <div class="toast-body">
                        <i class="${iconClass} me-2"></i>
                        <strong>${title}:</strong> ${message}
                    </div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
            </div>
        `;

        const toastElement = $(toastHtml);
        $('.toast-container').append(toastElement);
        const toast = new bootstrap.Toast(toastElement[0]);
        toast.show();

        // Remove toast from DOM after it hides
        toastElement.on('hidden.bs.toast', function () {
            $(this).remove();
        });
    };

    // Convert existing alerts to toasts on page load
    $('.alert').each(function() {
        const alertElement = $(this);
        let type = 'info'; // Default type
        if (alertElement.hasClass('alert-success')) type = 'success';
        else if (alertElement.hasClass('alert-danger')) type = 'danger';
        else if (alertElement.hasClass('alert-warning')) type = 'warning';

        const message = alertElement.text().trim().replace(/^(Lỗi:|THÀNH CÔNG:|Cảnh báo:|Thông báo:)\s*/, ''); // Remove title if present
        if (message) {
            showToast(type, message);
        }
        alertElement.remove(); // Remove the original alert
    });

    // --- Loading State for Forms ---
    // Function to handle form submission, disable button and show spinner
    window.handleFormSubmission = function(formElement) {
        const $form = $(formElement);
        const $submitButton = $form.find('button[type="submit"]');

        if ($submitButton.length) {
            const originalButtonText = $submitButton.html();
            $submitButton.prop('disabled', true);
            $submitButton.html('<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Đang xử lý...');

            // Re-enable button and restore text if form submission fails (e.g., validation error)
            // This is a basic fallback. For full robustness, server-side validation errors should also re-enable.
            $form.one('submit', function() {
                setTimeout(function() { // Give a small delay to ensure server response starts
                    if (!$form.data('submitted')) { // Check if form was actually submitted successfully
                        $submitButton.prop('disabled', false);
                        $submitButton.html(originalButtonText);
                    }
                }, 500);
            });
            $form.data('submitted', true); // Mark form as submitted
        }
    };

    // Attach handleFormSubmission to all forms that don't have a specific handler
    $('form').on('submit', function() {
        // Only apply if the form doesn't have a specific 'onsubmit' attribute already
        // or if it's not explicitly opted out
        if (!$(this).attr('onsubmit') && !$(this).hasClass('no-loading-state')) {
            window.handleFormSubmission(this);
        }
    });

    // --- Custom Confirmation Modal ---
    let confirmCallback = null; // Biến để lưu trữ hàm callback

    window.showConfirmationModal = function(message, callback) {
        confirmCallback = callback;
        $('#confirmationModalMessage').html(message);
        const confirmationModal = new bootstrap.Modal(document.getElementById('confirmationModal'));
        confirmationModal.show();
    };

    $('#confirmActionButton').on('click', function() {
        if (confirmCallback) {
            confirmCallback();
        }
        const confirmationModal = bootstrap.Modal.getInstance(document.getElementById('confirmationModal'));
        confirmationModal.hide();
    });
});