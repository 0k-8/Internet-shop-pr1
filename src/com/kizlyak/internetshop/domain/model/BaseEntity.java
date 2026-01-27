package com.kizlyak.internetshop.domain.model;

import java.util.Objects;
import java.util.UUID;

public abstract class BaseEntity {

    private final UUID id;

    public BaseEntity() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    // Додаємо цей метод для зручності відображення в консолі
    public String getIdAsString() {
        return id.toString();
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}