package com.kizlyak.internetshop.infrastructure;

import com.kizlyak.internetshop.domain.repository.OrderRepository;
import com.kizlyak.internetshop.domain.repository.ProductRepository;
import com.kizlyak.internetshop.domain.repository.UserRepository;

public interface UnitOfWork {

    ProductRepository getProductRepository();

    UserRepository getUserRepository();

    OrderRepository getOrderRepository();

    void commit(); // Зберегти всі зміни у файли
}