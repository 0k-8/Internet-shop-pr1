package com.kizlyak.internetshop.infrastructure;

import com.google.gson.reflect.TypeToken;
import com.kizlyak.internetshop.domain.cache.IdentityMap;
import com.kizlyak.internetshop.domain.impl.OrderRepositoryImpl;
import com.kizlyak.internetshop.domain.impl.ProductRepositoryImpl;
import com.kizlyak.internetshop.domain.impl.UserRepositoryImpl;
import com.kizlyak.internetshop.domain.model.Order;
import com.kizlyak.internetshop.domain.model.Product;
import com.kizlyak.internetshop.domain.model.User;
import com.kizlyak.internetshop.domain.repository.OrderRepository;
import com.kizlyak.internetshop.domain.repository.ProductRepository;
import com.kizlyak.internetshop.domain.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;

public class UnitOfWorkImpl implements UnitOfWork {

    private final IdentityMap<Product> productCache = new IdentityMap<>();
    private final IdentityMap<User> userCache = new IdentityMap<>();
    private final IdentityMap<Order> orderCache = new IdentityMap<>();

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    private final JsonDataMapper<Product> productMapper = new JsonDataMapper<>("data/products.json",
          new TypeToken<List<Product>>() {
          }.getType());
    private final JsonDataMapper<User> userMapper = new JsonDataMapper<>("data/users.json",
          new TypeToken<List<User>>() {
          }.getType());
    private final JsonDataMapper<Order> orderMapper = new JsonDataMapper<>("data/orders.json",
          new TypeToken<List<Order>>() {
          }.getType());

    public UnitOfWorkImpl() {
        this.productRepository = new ProductRepositoryImpl(productCache);
        this.userRepository = new UserRepositoryImpl(userMapper, userCache);
        this.orderRepository = new OrderRepositoryImpl(orderCache);

        load();
    }

    private void load() {
        // Очищуємо і завантажуємо
        productMapper.readFromFile().forEach(productCache::add);
        userMapper.readFromFile().forEach(userCache::add);
        orderMapper.readFromFile().forEach(orderCache::add);

    }

    @Override
    public ProductRepository getProductRepository() {
        return productRepository;
    }

    @Override
    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    @Override
    public void commit() {
        // Отримуємо списки з кешу
        List<Product> productsToSave = new ArrayList<>(productCache.getAll().values());
        List<User> usersToSave = new ArrayList<>(userCache.getAll().values());
        List<Order> ordersToSave = new ArrayList<>(orderCache.getAll().values());

        // Записуємо у файли
        productMapper.writeToFile(productsToSave);
        userMapper.writeToFile(usersToSave);
        orderMapper.writeToFile(ordersToSave);
    }
}