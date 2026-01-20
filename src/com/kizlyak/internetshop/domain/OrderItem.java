package com.kizlyak.internetshop.domain;

import java.math.BigDecimal;

public class OrderItem {

    private final Product product;
    private final int quantity;
    private final BigDecimal price; // ціна на момент продажу

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.price = product.getPrice();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}