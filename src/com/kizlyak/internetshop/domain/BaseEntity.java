package com.kizlyak.internetshop.domain;

import java.util.Objects;
import java.util.UUID;

public abstract class BaseEntity implements Entity {

    private final UUID id;

    public BaseEntity() {
        this.id = UUID.randomUUID();
    }

    @Override
    public UUID getId() {
        return id;
    }

    // БЕЗ ЦЬОГО МЕТОДУ equals() БУДЕ ПРАЦЮВАТИ НЕКОРЕКТНО
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