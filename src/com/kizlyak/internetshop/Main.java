package com.kizlyak.internetshop;

import com.kizlyak.internetshop.domain.Category;
import com.kizlyak.internetshop.domain.Order;
import com.kizlyak.internetshop.domain.Product;
import com.kizlyak.internetshop.domain.User;
import com.kizlyak.internetshop.domain.enums.UserRole;
import com.kizlyak.internetshop.domain.repository.OrderRepository;
import com.kizlyak.internetshop.domain.repository.ProductRepository;
import com.kizlyak.internetshop.domain.repository.UserRepository;
import com.kizlyak.internetshop.domain.util.ProductValidator;
import com.kizlyak.internetshop.domain.util.UserValidator;
import com.kizlyak.internetshop.domain.util.ValidationError;
import com.kizlyak.internetshop.service.ShopService;
import com.kizlyak.internetshop.service.impl.OrderRepositoryImpl;
import com.kizlyak.internetshop.service.impl.ProductRepositoryImpl;
import com.kizlyak.internetshop.service.impl.UserRepositoryImpl;
import java.math.BigDecimal;
import java.util.List;

public class Main {

    static void main(String[] args) {
        System.out.println("--- ЗАПУСК СИСТЕМИ ОБЛІКУ ЕЛЕКТРОНІКИ ---");

        // 1. Ініціалізація репозиторіїв
        ProductRepository productRepo = new ProductRepositoryImpl();
        UserRepository userRepo = new UserRepositoryImpl();
        OrderRepository orderRepo = new OrderRepositoryImpl();

        // 2. Ініціалізація валідаторів
        ProductValidator productValidator = new ProductValidator();
        UserValidator userValidator = new UserValidator();

        // 3. Створення сервісу (Важливо: 4 аргументи в правильному порядку!)
        ShopService shopService = new ShopService(productRepo, userRepo, orderRepo,
              productValidator);

        // 4. Тестування КАТЕГОРІЙ та ТОВАРІВ
        Category laptops = new Category("Ноутбуки", "Потужні лептопи для роботи");
        Product macbook = new Product("MacBook Air M2", new BigDecimal("48000"), 5);
        macbook.setCategory(laptops);

        try {
            shopService.addNewProduct(macbook);
            System.out.println("Товар успішно додано до системи: " + macbook.getName());
        } catch (Exception e) {
            System.err.println("Помилка додавання товару: " + e.getMessage());
        }

        // 5. Тестування КОРРИСТУВАЧА
        User customer = new User("kizlyak_ivan", "ivan@test.com", "Іван", "Кізляк", UserRole.USER);

        // Валідація перед збереженням
        List<ValidationError> userErrors = userValidator.validate(customer);
        if (!userErrors.isEmpty()) {
            System.out.println("\n[ВАЛІДАЦІЯ] Знайдено помилки:");
            userErrors.forEach(err -> System.out.println("- " + err.getMessage()));
        } else {
            userRepo.save(customer);
            System.out.println("Користувач збережений: " + customer.getLastName());
        }

        // 6. Тестування ПРОДАЖУ (ORDER)
        System.out.println("\n--- ОФОРМЛЕННЯ ПРОДАЖУ ---");
        try {
            // Купуємо 2 одиниці товару
            Order order = shopService.createOrder(customer.getId(), macbook.getId(), 2);

            System.out.println("Замовлення успішно створено!");
            System.out.println(
                  "Покупець: " + customer.getFirstName() + " " + customer.getLastName());
            System.out.println("Сума до сплати: " + order.getTotalAmount() + " грн.");
            System.out.println("Залишок на складі: " + macbook.getStockQuantity() + " шт.");

        } catch (Exception e) {
            System.err.println("Помилка оформлення продажу: " + e.getMessage());
        }

        System.out.println("\n--- РОБОТУ ЗАВЕРШЕНО ---");
    }
}