package com.kizlyak.internetshop.domain.impl;

import com.kizlyak.internetshop.domain.model.Order;
import com.kizlyak.internetshop.domain.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderRepositoryImpl implements OrderRepository {

    // Список усіх продажів у системі
    private final List<Order> orders = new ArrayList<>();

    @Override
    public void save(Order order) {
        orders.add(order);
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders);
    }

    @Override
    public List<Order> findByUserId(UUID userId) {
        // Фільтруємо замовлення, щоб знайти лише ті, що належать конкретному юзеру
        return orders.stream()
              .filter(order -> order.getCustomer().getId().equals(userId))
              .collect(Collectors.toList());
    }
}