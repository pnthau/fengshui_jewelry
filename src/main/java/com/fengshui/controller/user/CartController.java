package com.fengshui.controller.user;

import com.fengshui.entity.Cart;
import com.fengshui.entity.CartItem;
import com.fengshui.entity.Product;
import com.fengshui.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CartController", value = "/cart/*")
public class CartController extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        req.getRequestDispatcher("product_list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String action = req.getPathInfo();
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "/add":
                addCartItem(req, resp);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().print("OK");
                break;
            case "/remove":
                removeCartItem(req, resp);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().print("OK");
                break;

            case "/update":
                changeQuantityCartItem(req, resp);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().print("OK");
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy chức năng");
                break;
        }
    }

    private void addCartItem(HttpServletRequest req, HttpServletResponse resp) {
        String productIdStr = req.getParameter("productId");
        String quantityStr = req.getParameter("quantity");

        int productId = Integer.parseInt(productIdStr);
        int quantity = Integer.parseInt(quantityStr);

        HttpSession session = req.getSession();

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        Product product = productService.findByID(productId);
        if (product != null) {
            cart.addItem(product, quantity);
        }
    }

    private void removeCartItem(HttpServletRequest req, HttpServletResponse resp) {
        String productIdStr = req.getParameter("productId");

        int productId = Integer.parseInt(productIdStr);

        HttpSession session = req.getSession();

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.removeItem(productId);
        }
    }

    private void changeQuantityCartItem(HttpServletRequest req, HttpServletResponse resp) {
        String productIdStr = req.getParameter("productId");
        String quantityStr = req.getParameter("quantity");

        int productId = Integer.parseInt(productIdStr);
        int quantity = Integer.parseInt(quantityStr);

        HttpSession session = req.getSession();

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.changeQuantityItem(productId, quantity);
        }
    }
}
