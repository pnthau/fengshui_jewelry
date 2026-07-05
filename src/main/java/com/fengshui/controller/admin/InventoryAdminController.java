package com.fengshui.controller.admin;

import com.fengshui.entity.InventoryTransaction;
import com.fengshui.entity.Product;
import com.fengshui.DTO.InventoryTransactionDTO;
import com.fengshui.service.InventoryTransactionService;
import com.fengshui.service.ProductService;
import com.fengshui.service.IProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.time.format.DateTimeFormatter;

@WebServlet("/admin/inventory")
public class InventoryAdminController extends HttpServlet {
    private final InventoryTransactionService inventoryService = new InventoryTransactionService();
    private final IProductService productService = new ProductService();

    private static final String ACTION_LIST = "list";
    private static final String ACTION_SUBMIT = "submit";
    private static final String ACTION_VOID = "void";
    private static final String ACTION_EXPORT = "export";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = ACTION_LIST;
        }

        switch (action) {
            case ACTION_LIST:
                handleList(request, response);
                break;
            case ACTION_EXPORT:
                handleExportCSV(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/admin/inventory?action=" + ACTION_LIST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        // Tối ưu hóa cấu trúc xử lý POST bằng switch-case đồng bộ với luồng GET
        switch (action) {
            case ACTION_SUBMIT:
                handleSubmitTransaction(request, response);
                break;
            case ACTION_VOID:
                handleVoidTransaction(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/admin/inventory?action=" + ACTION_LIST);
                break;
        }
    }

    private void handleVoidTransaction(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        String idParam = request.getParameter("id");
        try {
            int id = Integer.parseInt(idParam);
            boolean success = inventoryService.voidTransaction(id);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/inventory?action=list&success=void");
            }
        } catch (Exception e) {
            reloadDataWithError(request, response, "Hủy không thành công: " + e.getMessage());
        }
    }

    private void handleExportCSV(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"inventory_report.csv\"");
        
        // Thêm BOM để Excel nhận diện UTF-8
        response.getOutputStream().write(0xEF);
        response.getOutputStream().write(0xBB);
        response.getOutputStream().write(0xBF);

        try (PrintWriter writer = new PrintWriter(response.getOutputStream())) {
            writer.println("ID,Sản phẩm,Loại,Số lượng,Giá,Lý do,Ngày tạo,Trạng thái");
            List<InventoryTransactionDTO> list = inventoryService.getAllTransactionsDTO();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            for (InventoryTransactionDTO d : list) {
                writer.printf("%d,%s,%s,%d,%s,%s,%s,%s\n",
                    d.getId(), d.getProductName(), d.getTransactionType(),
                    d.getQuantity(), d.getPrice().toString(), d.getReason(),
                    d.getCreatedAt().format(formatter), d.getStatus());
            }
        }
    }

    /**
     * Hiển thị bảng nhật ký kho & danh sách sản phẩm để chuẩn bị nhập/xuất kho (GET)
     */
    private void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        loadInventoryData(request);
        request.setAttribute("title", "Nhật ký kho");
        request.setAttribute("contentPage", "/WEB-INF/views/admin/inventory_list.jsp");
        request.getRequestDispatcher("/WEB-INF/views/admin/admin_layout.jsp").forward(request, response);
    }

    /**
     * Nạp dữ liệu cần thiết (transactions và products) vào request attribute.
     * Giúp tái sử dụng code và dễ bảo trì.
     */
    private void loadInventoryData(HttpServletRequest request) {
        List<InventoryTransactionDTO> transactions = inventoryService.getAllTransactionsDTO();
        List<Product> products = productService.findAll();
        request.setAttribute("transactions", transactions);
        request.setAttribute("products", products);
    }

    /**
     * Xử lý gửi Form yêu cầu Nhập kho hoặc Xuất kho thủ công (POST)
     */
    private void handleSubmitTransaction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productIdParam = request.getParameter("productId");
        String type = request.getParameter("transactionType");
        String quantityParam = request.getParameter("quantity");
        String priceParam = request.getParameter("price");
        String reason = request.getParameter("reason");

        try {
            // 1. Kiểm tra đầu vào an toàn
            if (productIdParam == null || quantityParam == null || priceParam == null) {
                throw new IllegalArgumentException("Missing required fields for inventory transaction!");
            }

            int productId = Integer.parseInt(productIdParam);
            int quantity = Integer.parseInt(quantityParam);
            BigDecimal price = new BigDecimal(priceParam);

            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero!");
            }

            // Giả định AdminId người thực hiện tạm thời là 1 (hoặc lấy từ Account Session của bạn sau này)
            int adminId = 1;

            // 2. Tạo đối tượng Entity và nạp thông tin
            InventoryTransaction tx = new InventoryTransaction();
            tx.setProductId(productId);
            tx.setTransactionType(type);
            tx.setQuantity(quantity);
            tx.setPrice(price);
            tx.setReason(reason != null ? reason.trim() : "");
            tx.setCreatedBy(adminId);

            // 3. Thực thi nghiệp vụ đồng bộ qua Service
            boolean success = inventoryService.executeStockTransaction(tx);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/inventory?action=" + ACTION_LIST + "&success=true");
            } else {
                throw new RuntimeException("Database error occurred while processing inventory transaction.");
            }
        } catch (NumberFormatException e) {
            // Lỗi khi người dùng nhập chữ vào trường số hoặc để trống định dạng số
            reloadDataWithError(request, response, "Invalid number format for Quantity or Price.");
        } catch (IllegalArgumentException e) {
            // Lỗi nghiệp vụ đầu vào
            reloadDataWithError(request, response, e.getMessage());
        } catch (RuntimeException e) {
            // Lỗi nghiệp vụ từ Database (Ví dụ: Giảm kho vượt quá hàng tồn hiện có)
            reloadDataWithError(request, response, e.getMessage());
        }
    }

    /**
     * Nạp lại dữ liệu và truyền thông điệp lỗi ra giao diện JSP
     */
    private void reloadDataWithError(HttpServletRequest request, HttpServletResponse response, String errorMsg)
            throws ServletException, IOException {
        loadInventoryData(request);
        request.setAttribute("error", errorMsg); // Gửi thông điệp lỗi tiếng Anh về JSP
        request.setAttribute("title", "Nhật ký kho");
        request.setAttribute("contentPage", "/WEB-INF/views/admin/inventory_list.jsp");
        request.getRequestDispatcher("/WEB-INF/views/admin/admin_layout.jsp").forward(request, response);
    }
}
