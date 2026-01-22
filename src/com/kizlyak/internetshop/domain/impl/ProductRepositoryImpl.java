package com.kizlyak.internetshop.domain.impl;

import com.kizlyak.internetshop.domain.model.Product;
import com.kizlyak.internetshop.domain.cache.IdentityMap;
import com.kizlyak.internetshop.domain.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductRepositoryImpl implements ProductRepository {

    private final IdentityMap<Product> cache;

    public ProductRepositoryImpl(IdentityMap<Product> cache) {
        this.cache = cache;
    }

    @Override
    public void save(Product product) {
        cache.add(product);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(cache.getAll().values());
    }

    @Override
    public void delete(UUID id) {
        cache.remove(id);
    }
}