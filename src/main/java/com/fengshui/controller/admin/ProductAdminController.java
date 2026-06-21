package com.fengshui.controller.admin;

import com.fengshui.entity.Product;
import com.fengshui.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@WebServlet("/admin/products")
public class ProductAdminController extends HttpServlet {
    private final ProductService productService = new ProductService();

    // Khai báo các hằng số hành động rõ ràng (Tránh lỗi gõ sai chính tả - Anti-typo)
    private static final String ACTION_LIST = "list";
    private static final String ACTION_CREATE = "create";
    private static final String ACTION_EDIT = "edit";
    private static final String ACTION_DELETE = "delete";
    private static final String ACTION_ADD = "add";
    private static final String ACTION_UPDATE = "update";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Thiết lập mã hóa ký tự UTF-8 cho request và response để không bị lỗi font tiếng Việt
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = ACTION_LIST;
        }

        // Sử dụng switch-case đồng bộ
        switch (action) {
            case ACTION_LIST:
                handleList(request, response);
                break;
            case ACTION_CREATE:
                handleCreate(request, response);
                break;
            case ACTION_EDIT:
                handleEdit(request, response);
                break;
            case ACTION_DELETE:
                handleDelete(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/admin/products?action=" + ACTION_LIST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Đảm bảo mã hóa UTF-8 cho luồng POST dữ liệu lên
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/admin/products?action=" + ACTION_LIST);
            return;
        }

        switch (action) {
            case ACTION_ADD:
            case ACTION_UPDATE:
                handleSaveProduct(request, response, action);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/admin/products?action=" + ACTION_LIST);
        }
    }

    // --- CÁC HÀM TRỢ GIÚP CHI TIẾT (HELPER METHODS) ---

    /**
     * Hiển thị danh sách toàn bộ trang sức phong thủy hiện có (GET)
     */
    private void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> products = productService.findAll();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/WEB-INF/views/admin/product_list.jsp").forward(request, response);
    }

    /**
     * Hiển thị màn hình giao diện thêm mới sản phẩm (GET)
     */
    private void handleCreate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/admin/product_form.jsp").forward(request, response);
    }

    /**
     * Hiển thị màn hình cập nhật thông tin sản phẩm dựa trên ID (GET)
     */
    private void handleEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/admin/products?action=" + ACTION_LIST);
                return;
            }

            int id = Integer.parseInt(idParam);
            Product p = productService.findByID(id);

            if (p == null) {
                response.sendRedirect(request.getContextPath() + "/admin/products?action=" + ACTION_LIST);
                return;
            }

            request.setAttribute("product", p);
            request.setAttribute("productElements", p.getElements());
            request.getRequestDispatcher("/WEB-INF/views/admin/product_form.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            // Ngăn chặn lỗi sập luồng 500 khi ID truyền vào là chuỗi không hợp lệ
            response.sendRedirect(request.getContextPath() + "/admin/products?action=" + ACTION_LIST);
        }
    }

    /**
     * Xử lý xóa sản phẩm dựa trên ID (GET)
     */
    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/admin/products?action=" + ACTION_LIST);
                return;
            }

            int delId = Integer.parseInt(idParam);
            productService.delete(delId);
            response.sendRedirect(request.getContextPath() + "/admin/products?action=" + ACTION_LIST);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/products?action=" + ACTION_LIST);
        }
    }

    /**
     * Xử lý lưu (Thêm mới/Cập nhật) sản phẩm phong thủy (POST)
     */
    private void handleSaveProduct(HttpServletRequest request, HttpServletResponse response, String action)
            throws IOException {
        Product p = mapRequestToProduct(request);

        if (ACTION_ADD.equals(action)) {
            productService.save(p);
        } else if (ACTION_UPDATE.equals(action)) {
            productService.update(p);
        }

        response.sendRedirect(request.getContextPath() + "/admin/products?action=" + ACTION_LIST);
    }

    /**
     * Ánh xạ thông tin an toàn từ Form Request sang đối tượng thực thể Product
     */
    private Product mapRequestToProduct(HttpServletRequest request) {
        Product p = new Product();

        // 1. Ánh xạ ID sản phẩm (Chỉ áp dụng cho trường hợp chỉnh sửa thông tin)
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                p.setId(Integer.parseInt(idStr));
            } catch (NumberFormatException e) {
                // Đóng vai trò phòng thủ bổ trợ cho dữ liệu đầu vào
                p.setId(0);
            }
        }

        // 2. Tên sản phẩm
        p.setName(request.getParameter("name"));

        // 3. Giá tiền (Xử lý an toàn và phòng tránh dữ liệu trống)
        String priceStr = request.getParameter("price");
        p.setPrice((priceStr != null && !priceStr.isEmpty()) ? new BigDecimal(priceStr) : BigDecimal.ZERO);

        // 4. Số lượng tồn kho (Xử lý an toàn)
        String qtyStr = request.getParameter("quantity");
        p.setQuantity((qtyStr != null && !qtyStr.isEmpty()) ? Integer.parseInt(qtyStr) : 0);

        // 5. Chất liệu chế tác (Cung cấp giá trị mặc định nếu để trống)
        String material = request.getParameter("material");
        p.setMaterial((material != null && !material.trim().isEmpty()) ? material : "Chưa xác định");

        // 6. Các thông tin mô tả và hình ảnh khác
        p.setImageURL(request.getParameter("imageUrl"));
        p.setDescription(request.getParameter("description"));

        // 7. Xử lý lưu các hệ mệnh ngũ hành hợp tương thích
        String[] elementsArray = request.getParameterValues("elements");
        if (elementsArray != null) {
            p.setElements(new HashSet<>(Arrays.asList(elementsArray)));
        } else {
            p.setElements(new HashSet<>());
        }

        return p;
    }
}