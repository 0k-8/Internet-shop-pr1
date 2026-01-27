package com.kizlyak.internetshop.domain.model;

import java.math.BigDecimal;

public class Product extends BaseEntity implements Entity {

    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private Category category;

    // Порожній конструктор для GSON
    public Product() {
        super();
    }

    // Правильний конструктор: використовуємо super() для ID та ініціалізуємо категорію
    public Product(String name, String description, BigDecimal price, int stockQuantity,
          Category category) {
        super(); // Тут BaseEntity сам створює UUID id
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
    }

    // Геттери
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    // Сеттери
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}