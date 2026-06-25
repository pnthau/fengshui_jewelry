package com.fengshui.controller.user;

import com.fengshui.entity.Product;
import com.fengshui.service.IProductService;
import com.fengshui.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ProductDetailController", value = "/product-detail")
public class ProductDetailController extends HttpServlet {
    private final IProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");

        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            Product product = productService.findByID(id);

            if (product != null) {
                request.setAttribute("product", product);
                request.getRequestDispatcher("/WEB-INF/views/user/product_detail.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/products");
            }


        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/products");
        }
    }

}
