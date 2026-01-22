package com.kizlyak.internetshop.domain.model;

import java.util.Objects;

public class Category extends BaseEntity {

    private String name;
    private String description;

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
        return "Категорія: " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseEntity that = (BaseEntity) o;
        // Використовуємо метод getId() замість поля id
        return Objects.equals(this.getId(), that.getId());
    }
}