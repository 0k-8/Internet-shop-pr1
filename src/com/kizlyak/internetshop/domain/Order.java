package com.kizlyak.internetshop.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order extends BaseEntity {

    private final User customer;
    private final LocalDateTime orderDate;
    private List<OrderItem> items = new ArrayList<>();

    public Order(User customer) {
        super();
        this.customer = customer;
        this.orderDate = LocalDateTime.now();
        this.items = new ArrayList<>();
    }

    // ЦЕЙ МЕТОД ВИПРАВЛЯЄ ВАШУ ПОМИЛКУ
    public User getCustomer() {
        return customer;
    }

    public void addProduct(Product product, int quantity) {
        if (product.getStockQuantity() >= quantity) {
            items.add(new OrderItem(product, quantity));
            product.setStockQuantity(product.getStockQuantity() - quantity);
        }
    }

    public BigDecimal getTotalAmount() {
        return items.stream()
              .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
              .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}