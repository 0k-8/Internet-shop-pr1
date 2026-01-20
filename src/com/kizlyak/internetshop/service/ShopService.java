package com.kizlyak.internetshop.service;

import com.kizlyak.internetshop.domain.Order;
import com.kizlyak.internetshop.domain.Product;
import com.kizlyak.internetshop.domain.User;
import com.kizlyak.internetshop.domain.exception.EntityValidationException;
import com.kizlyak.internetshop.domain.repository.OrderRepository;
import com.kizlyak.internetshop.domain.repository.ProductRepository;
import com.kizlyak.internetshop.domain.repository.UserRepository;
import com.kizlyak.internetshop.domain.util.ProductValidator;
import com.kizlyak.internetshop.domain.util.ValidationError;
import java.util.List;
import java.util.UUID;

public class ShopService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductValidator productValidator; // Додаємо поле для валідатора

    public ShopService(ProductRepository productRepository,
          UserRepository userRepository,
          OrderRepository orderRepository, // ДОДАЙТЕ ЦЕЙ РЯДОК СЮДИ
          ProductValidator productValidator) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository; // Тепер помилки не буде
        this.productValidator = productValidator;
    }

    public void addNewProduct(Product product) {
        // Використовуємо об'єкт productValidator, а не назву класу
        List<ValidationError> errors = productValidator.validate(product);

        // Тепер .isEmpty() буде працювати, бо це java.util.List
        if (!errors.isEmpty()) {
            throw new EntityValidationException(errors);
        }

        productRepository.save(product);
    }

    public Order createOrder(UUID userId, UUID productId, int quantity) {
        User user = userRepository.findById(userId)
              .orElseThrow(() -> new RuntimeException("Користувача не знайдено"));

        Product product = productRepository.findById(productId)
              .orElseThrow(() -> new RuntimeException("Товар не знайдено"));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Недостатньо товару на складі.");
        }

        Order order = new Order(user);
        order.addProduct(product, quantity);

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        if (orderRepository != null) {
            orderRepository.save(order);
        }

        return order;
    }
}