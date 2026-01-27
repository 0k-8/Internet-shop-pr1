package com.kizlyak.internetshop.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Order extends BaseEntity implements Entity {

    private final User customer;
    private final LocalDateTime orderDate;
    private final String address; // Додаємо адресу
    private final List<OrderItem> items;

    public Order(User customer, String address) {
        super();
        this.customer = customer;
        this.address = address;
        this.orderDate = LocalDateTime.now();
        this.items = new ArrayList<>();

    }


    public void addProduct(Product product, int quantity) {
        this.items.add(new OrderItem(product, quantity));
    }

    // Метод для отримання списку категорій для JSON
    public List<Category> getCategories() {
        return items.stream()
              .map(item -> item.getProduct().getCategory())
              .distinct()
              .collect(Collectors.toList()).reversed();
    }

    public BigDecimal getTotalAmount() {
        return items.stream()
              .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
              .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Геттери для мапера
    public User getCustomer() {
        return customer;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getAddress() {
        return address;
    }

    public List<OrderItem> getItems() {
        return items;
    }


}