package com.kizlyak.internetshop.domain.model;

import java.util.Objects;

public class Category extends BaseEntity {

    private String name;
    private String description;

    // 1. Порожній конструктор для GSON (щоб дані правильно читалися з JSON)
    public Category() {
        super();
    }

    // 2. Конструктор тільки з ім'ям (саме він виправить помилку в DTO!)
    public Category(String name) {
        super();
        this.name = name;
        this.description = ""; // Опис за замовчуванням порожній
    }

    // 3. Твій оригінальний конструктор
    public Category(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

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

    @Override
    public String toString() {
        return name; // Повертаємо тільки назву для зручності у списках
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Category category = (Category) o;
        // Порівнюємо за ID, якщо вони є, або за назвою
        return Objects.equals(getId(), category.getId()) || Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), name);
    }
}