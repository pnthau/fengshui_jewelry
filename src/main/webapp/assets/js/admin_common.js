/* admin_common.js */

$(document).ready(function() {
    // Toggle the sidebar
    $("#sidebarToggle").on("click", function(e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled");
    });

    // Function to initialize DataTables
    function initializeDataTable(tableId, columnDefs) {
        if ($(tableId).length) {
            $(tableId).DataTable({
                "language": {
                    "url": "//cdn.datatables.net/plug-ins/1.13.6/i18n/vi.json" // Việt hóa giao diện
                },
                "order": [[0, "desc"]], // Mặc định sắp xếp theo ID giảm dần
                "pageLength": 10, // Số dòng trên mỗi trang
                "columnDefs": columnDefs,
                "dom": '<"d-flex justify-content-between align-items-center p-3"<"d-flex align-items-center"l>>t<"d-flex justify-content-between align-items-center p-3"ip>'
            });
        }
    }

    // Initialize DataTables for inventoryTable
    initializeDataTable('#inventoryTable', [
        { "orderable": false, "targets": 6 }, // Vô hiệu hóa sắp xếp cột "Thao tác"
        { "visible": true, "targets": [1, 2, 6] }
    ]);

    // Initialize DataTables for productTable
    initializeDataTable('#productTable', [
        { "orderable": false, "targets": 4 } // Vô hiệu hóa sắp xếp cột "Thao tác"
    ]);

    // Initialize DataTables for orderTable
    initializeDataTable('#orderTable', [
        { "orderable": false, "targets": 6 } // Vô hiệu hóa sắp xếp cột "Thao tác"
    ]);


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

    // Auto-hide alerts after 3 seconds
    setTimeout(function() {
        $(".alert").fadeOut('slow');
    }, 3000);
});
