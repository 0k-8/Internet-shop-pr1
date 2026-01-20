package com.kizlyak.internetshop.domain.repository;


import com.kizlyak.internetshop.domain.Order;
import java.util.List;
import java.util.UUID;

public interface OrderRepository {

    void save(Order order);

    List<Order> findAll();

    List<Order> findByUserId(UUID userId); // Корисно для звіту по клієнту
}