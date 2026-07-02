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
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/admin/inventory")
public class InventoryAdminController extends HttpServlet {
    private final InventoryTransactionService inventoryService = new InventoryTransactionService();
    private final IProductService productService = new ProductService();

    private static final String ACTION_LIST = "list";
    private static final String ACTION_SUBMIT = "submit";

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
            default:
                response.sendRedirect(request.getContextPath() + "/admin/inventory?action=" + ACTION_LIST);
                break;
        }
    }

    /**
     * Hiển thị bảng nhật ký kho & danh sách sản phẩm để chuẩn bị nhập/xuất kho (GET)
     */
    private void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy lịch sử kho dạng DTO liên bảng (chứa cả tên và ảnh sản phẩm)
        List<InventoryTransactionDTO> transactions = inventoryService.getAllTransactionsDTO();

        // Lấy danh sách sản phẩm phục vụ cho thanh chọn sản phẩm (Select Option) trong Form Nhập/Xuất kho nhanh
        List<Product> products = productService.findAll();

        request.setAttribute("transactions", transactions);
        request.setAttribute("products", products);
        request.getRequestDispatcher("/WEB-INF/views/admin/inventory_list.jsp").forward(request, response);
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
        List<InventoryTransactionDTO> transactions = inventoryService.getAllTransactionsDTO();
        List<Product> products = productService.findAll();

        request.setAttribute("transactions", transactions);
        request.setAttribute("products", products);
        request.setAttribute("error", errorMsg); // Gửi thông điệp lỗi tiếng Anh về JSP

        request.getRequestDispatcher("/WEB-INF/views/admin/inventory_list.jsp").forward(request, response);
    }
}