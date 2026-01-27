package com.kizlyak.internetshop.service;

import com.kizlyak.internetshop.domain.model.Order;
import com.kizlyak.internetshop.infrastructure.UnitOfWork;
import com.kizlyak.internetshop.presentation.ConsoleColors;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class OrderService {

    private final UnitOfWork uow;

    public OrderService(UnitOfWork uow) {
        this.uow = uow;
    }

    public void showSalesStatistics() {
        List<Order> orders = getAllOrders();
        if (orders.isEmpty()) {
            System.out.println(ConsoleColors.YELLOW + "Продажів ще не було." + ConsoleColors.RESET);
            return;
        }

        // 1. Рахуємо загальну виручку
        BigDecimal totalRevenue = orders.stream()
              .map(Order::getTotalAmount)
              .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. Складаємо мапу: Назва товару -> Загальна кількість проданих штук
        java.util.Map<String, Integer> productSalesCount = new java.util.HashMap<>();

        orders.forEach(order -> {
            order.getItems().forEach(item -> {
                String productName = item.getProduct().getName();
                productSalesCount.put(productName,
                      productSalesCount.getOrDefault(productName, 0) + item.getQuantity());
            });
        });

        // --- ВИВІД СТАТИСТИКИ ---
        System.out.println(ConsoleColors.PURPLE + "\n========== ЗВІТ ПРО ПРОДАЖІ =========="
              + ConsoleColors.RESET);
        System.out.printf(
              "Загальна виручка:  " + ConsoleColors.GREEN + "%.2f грн%n" + ConsoleColors.RESET,
              totalRevenue);
        System.out.println("Кількість замовлень: " + orders.size());
        System.out.println("--------------------------------------");
        System.out.println("Продані товари (найменування | кількість):");

        if (productSalesCount.isEmpty()) {
            System.out.println("Дані про товари відсутні.");
        } else {
            productSalesCount.forEach((name, count) -> {
                System.out.printf("- %-25s | %d шт.%n", name, count);
            });
        }
        System.out.println(ConsoleColors.PURPLE + "======================================"
              + ConsoleColors.RESET);
    }

    public List<Order> getAllOrders() {
        return uow.getOrderRepository().findAll();
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        return uow.getOrderRepository().findByUserId(userId);
    }

    public void createOrder(Order order) {
        // Додаємо замовлення в репозиторій (це просто додає в List в пам'яті)
        uow.getOrderRepository().save(order);

        // Оновлюємо склад для кожного товару
        order.getItems().forEach(item -> {
            var product = item.getProduct();
            int newQuantity = product.getStockQuantity() - item.getQuantity();
            product.setStockQuantity(newQuantity);
            uow.getProductRepository().save(product);
        });

        // КРИТИЧНО ВАЖЛИВО: без цього рядка файли не зміняться!
        uow.commit();
    }
}