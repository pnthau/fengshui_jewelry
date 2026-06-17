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
import java.util.List;

@WebServlet(name = "ProductListController", value = "/products")
public class ProductListController extends HttpServlet {
    private final IProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("keyword");
        String element = request.getParameter("element");
        String year = request.getParameter("year");

        List<Product> products = null;

        if (keyword != null && !keyword.trim().isEmpty()) {
            products = productService.searchByName(keyword);
        } else if (element != null && !element.isEmpty()) {
            products = productService.findByElement(element);
        } else {
            products = productService.findAll();
        }

        request.setAttribute("products", products);
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedElement", element);
        request.setAttribute("year", year);
        request.getRequestDispatcher("/WEB-INF/views/user/product_list.jsp").forward(request, response);
    }
}
