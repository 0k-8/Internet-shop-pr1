package com.kizlyak.internetshop.domain.cache;

import com.kizlyak.internetshop.domain.model.Entity;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IdentityMap<T extends Entity> {

    private final Map<UUID, T> entities = new HashMap<>();

    public void add(T entity) {
        if (entity != null && entity.getId() != null) {
            entities.put(entity.getId(), entity);
        }
    }

    public void remove(UUID id) {
        entities.remove(id);
    }

    public T get(UUID id) {
        return entities.get(id);
    }

    public Map<UUID, T> getAll() {
        return entities;
    }
}