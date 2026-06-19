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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                List<Product> products = productService.findAll();
                request.setAttribute("products", products);
                request.getRequestDispatcher("/WEB-INF/views/admin/product_list.jsp").forward(request, response);
                break;
            case "create":
                // Màn hình thêm mới
                request.getRequestDispatcher("/WEB-INF/views/admin/product_form.jsp").forward(request, response);
                break;
            case "edit":
                int id = Integer.parseInt(request.getParameter("id"));
                Product p = productService.findByID(id);
                request.setAttribute("product", p);
                request.setAttribute("productElements", p.getElements());
                request.getRequestDispatcher("/WEB-INF/views/admin/product_form.jsp").forward(request, response);
                break;
            case "delete":
                int delId = Integer.parseInt(request.getParameter("id"));
                productService.delete(delId);
                response.sendRedirect(request.getContextPath() + "/admin/products?action=list");
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        // 1. Ánh xạ dữ liệu từ request vào đối tượng Product
        Product p = mapRequestToProduct(request);

        // 2. Xử lý logic dựa trên action
        if ("add".equals(action)) {
            productService.save(p);
        } else if ("update".equals(action)) {
            productService.update(p);
        }

        response.sendRedirect(request.getContextPath() + "/admin/products?action=list");
    }

    private Product mapRequestToProduct(HttpServletRequest request) {
        Product p = new Product();

        // 1. ID (Chỉ lấy khi update)
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            p.setId(Integer.parseInt(idStr));
        }

        // 2. Tên sản phẩm
        p.setName(request.getParameter("name"));

        // 3. Giá tiền (Xử lý an toàn)
        String priceStr = request.getParameter("price");
        p.setPrice((priceStr != null && !priceStr.isEmpty()) ? new BigDecimal(priceStr) : BigDecimal.ZERO);

        // 4. Số lượng (Xử lý an toàn)
        String qtyStr = request.getParameter("quantity");
        p.setQuantity((qtyStr != null && !qtyStr.isEmpty()) ? Integer.parseInt(qtyStr) : 0);

        // 5. Chất liệu (Sửa lỗi: Đảm bảo không bao giờ là null)
        String material = request.getParameter("material");
        p.setMaterial((material != null && !material.trim().isEmpty()) ? material : "Chưa xác định");

        // 6. Các trường khác
        p.setImageURL(request.getParameter("imageUrl"));
        p.setDescription(request.getParameter("description"));

        // 7. Xử lý checkbox mệnh (Đã an toàn)
        String[] elementsArray = request.getParameterValues("elements");
        if (elementsArray != null) {
            p.setElements(new HashSet<>(Arrays.asList(elementsArray)));
        } else {
            p.setElements(new HashSet<>());
        }

        return p;
    }}