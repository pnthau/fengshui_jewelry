package com.fengshui.entity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

public class Cart {
    private Map<Integer, CartItem> items = new HashMap<>();

    public void addItem(Product product, int quantity) {
        int productId = product.getId();

        if (items.containsKey(productId)) {
            int currQuantity = items.get(productId).getQuantity() + quantity;
            items.get(productId).setQuantity(currQuantity);
        } else {
            items.put(productId, new CartItem(product, quantity));
        }
    }

    public void removeItem(int productId) {
        if (items.containsKey(productId)) {
            items.remove(productId);
        }
    }

    public BigDecimal getTotalCartPrice() {
        return items.values().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void changeQuantityItem(int productId, int quantity) {
        items.get(productId).setQuantity(quantity);
    }

    public Map<Integer, CartItem> getItems() {
        return items;
    }
}
