package com.kizlyak.internetshop.domain.impl;

import com.kizlyak.internetshop.domain.cache.IdentityMap;
import com.kizlyak.internetshop.domain.model.Order;
import com.kizlyak.internetshop.domain.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderRepositoryImpl implements OrderRepository {

    private final IdentityMap<Order> cache;

    public OrderRepositoryImpl(IdentityMap<Order> cache) {
        this.cache = cache;
    }

    @Override
    public void save(Order order) {
        cache.add(order);
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(cache.getAll().values());
    }

    @Override
    public List<Order> findByUserId(UUID userId) {
        return cache.getAll().values().stream()
              .filter(order -> order.getCustomer() != null &&
                    order.getCustomer().getId().equals(userId))
              .collect(Collectors.toList());
    }
}