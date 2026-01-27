package com.kizlyak.internetshop.presentation.forms;

import com.kizlyak.internetshop.domain.model.Order;
import com.kizlyak.internetshop.domain.model.Product;
import com.kizlyak.internetshop.domain.model.User;
import com.kizlyak.internetshop.presentation.ConsoleColors;
import com.kizlyak.internetshop.service.OrderService;
import com.kizlyak.internetshop.service.ProductService;
import com.kizlyak.internetshop.service.UserService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserForm {

    private final ProductService productService;
    private final User currentUser;
    private final Scanner scanner = new Scanner(System.in);
    private final List<Product> cart = new ArrayList<>();
    private final UserService userService;
    private final OrderService orderService;

    public UserForm(ProductService productService, OrderService orderService,
          UserService userService, User user) {
        this.productService = productService;
        this.currentUser = user;
        this.userService = userService;
        this.orderService = orderService;
    }

    public void render() {
        while (true) {
            System.out.println(
                  ConsoleColors.CYAN + "\n=== МЕНЮ КОРИСТУВАЧА [" + currentUser.getFirstName()
                        + "] ===" + ConsoleColors.RESET);
            System.out.println("1. Каталог товарів");
            System.out.println("2. Мій кошик (" + cart.size() + " шт.)");
            System.out.println("3. Історія замовлень");
            System.out.println("4. Налаштування профілю");
            System.out.println("0. Вийти в головне меню");
            System.out.print("Вибір: ");

            String choice = scanner.nextLine();
            if (choice.equals("0")) {
                break;
            }

            switch (choice) {
                case "1" -> showCatalogMenu();
                case "2" -> showCart();
                case "3" -> showOrderHistory();
                case "4" -> showProfileSettings(); // Додав виклик налаштувань
                default -> System.out.println(
                      ConsoleColors.RED + "Невірний вибір!" + ConsoleColors.RESET);
            }
        }
    }

    private void showCatalogMenu() {
        System.out.println("\n--- КАТАЛОГ ---");
        System.out.println("1. Показати всі товари");
        System.out.println("2. Пошук за назвою");
        System.out.println("3. Фільтр за категорією");
        System.out.print("Вибір: ");

        String subChoice = scanner.nextLine();

        switch (subChoice) {
            case "1" -> displayProductsAndAddToCart(productService.getAllProducts());
            case "2" -> {
                System.out.print("Введіть назву: ");
                String query = scanner.nextLine();
                displayProductsAndAddToCart(productService.searchByName(query));
            }
            case "3" -> showCategorySelection();
            default -> System.out.println("Невірний вибір.");
        }
    }

    // ОНОВЛЕНИЙ МЕТОД: Без відображення точної кількості
    private void displayProductsAndAddToCart(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("Товарів не знайдено.");
            return;
        }

        System.out.println("\n--- СПИСОК ТОВАРІВ ---");
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            String catName = p.getCategory() != null ? p.getCategory().getName() : "---";

            // Замість цифр використовуємо статус наявності
            String availability = p.getStockQuantity() > 0
                  ? ConsoleColors.GREEN + "Є в наявності" + ConsoleColors.RESET
                  : ConsoleColors.RED + "Немає в наявності" + ConsoleColors.RESET;

            System.out.printf("%d. [%s] %s | %s грн | %s%n",
                  i + 1, catName, p.getName(), p.getPrice(), availability);
        }

        System.out.print("\nВведіть номер товару для кошика (0 - вихід): ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index == -1) {
                return;
            }

            if (index >= 0 && index < products.size()) {
                Product selected = products.get(index);
                if (selected.getStockQuantity() > 0) {
                    cart.add(selected);
                    System.out.println(ConsoleColors.GREEN + "Додано: " + selected.getName()
                          + ConsoleColors.RESET);
                } else {
                    System.out.println(ConsoleColors.RED + "Цього товару немає в наявності!"
                          + ConsoleColors.RESET);
                }
            } else {
                System.out.println("Невірний номер.");
            }
        } catch (Exception e) {
            System.out.println("Некоректний ввід.");
        }
    }

    private void showCategorySelection() {
        List<Product> allProducts = productService.getAllProducts();
        List<String> categories = allProducts.stream()
              .map(p -> p.getCategory() != null ? p.getCategory().getName() : "Без категорії")
              .distinct()
              .sorted()
              .toList();

        if (categories.isEmpty()) {
            System.out.println("Категорій поки немає.");
            return;
        }

        System.out.println("\n=== ОБЕРІТЬ КАТЕГОРІЮ ===");
        for (int i = 0; i < categories.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, categories.get(i));
        }
        System.out.println("0. Назад");

        System.out.print("Ваш вибір: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) {
                return;
            }

            if (choice > 0 && choice <= categories.size()) {
                String selectedCategory = categories.get(choice - 1);
                List<Product> filtered = allProducts.stream()
                      .filter(p -> p.getCategory() != null && p.getCategory().getName()
                            .equals(selectedCategory))
                      .toList();
                displayProductsAndAddToCart(filtered);
            }
        } catch (Exception e) {
            System.out.println("Помилка вводу.");
        }
    }

    private void showOrderHistory() {
        System.out.println(
              ConsoleColors.PURPLE + "\n--- ВАША ІСТОРІЯ ЗАМОВЛЕНЬ ---" + ConsoleColors.RESET);
        List<Order> myOrders = orderService.getOrdersByUserId(currentUser.getId());

        if (myOrders.isEmpty()) {
            System.out.println("У вас ще немає замовлень.");
        } else {
            myOrders.forEach(order -> {
                System.out.println("-------------------------------------------");
                System.out.println("ID: " + order.getIdAsString().substring(0, 8)); // Скорочений ID
                System.out.println("Дата: " + order.getOrderDate());
                System.out.println("Адреса: " + order.getAddress());
                System.out.println("Товари:");
                order.getItems().forEach(item ->
                      System.out.printf("  - %s x%d (%.2f грн)%n",
                            item.getProduct().getName(), item.getQuantity(), item.getPrice()));
                System.out.println("СУМА: " + order.getTotalAmount() + " грн");
            });
        }
    }

    private void showCart() {
        while (true) {
            System.out.println(ConsoleColors.YELLOW + "\n--- ВАШ КОШИК ---" + ConsoleColors.RESET);
            if (cart.isEmpty()) {
                System.out.println("Кошик порожній.");
                return;
            }

            BigDecimal total = BigDecimal.ZERO;
            for (int i = 0; i < cart.size(); i++) {
                Product p = cart.get(i);
                System.out.printf("%d. %s | %.2f грн%n", i + 1, p.getName(), p.getPrice());
                total = total.add(p.getPrice());
            }
            System.out.println("-----------------");
            System.out.println("РАЗОМ: " + total + " грн");

            System.out.println("\n1. Оформити замовлення");
            System.out.println("2. Видалити товар з кошика");
            System.out.println("3. Очистити кошик");
            System.out.println("0. Назад");
            System.out.print("Вибір: ");

            String cartChoice = scanner.nextLine();
            switch (cartChoice) {
                case "1" -> {
                    processCheckout();
                    return;
                }
                case "2" -> removeFromCart();
                case "3" -> cart.clear();
                case "0" -> {
                    return;
                }
            }
        }
    }

    private void showProfileSettings() {
        System.out.println(
              ConsoleColors.PURPLE + "\n--- НАЛАШТУВАННЯ ПРОФІЛЮ ---" + ConsoleColors.RESET);
        System.out.println("1. Змінити ім'я (Зараз: " + currentUser.getFirstName() + ")");
        System.out.println("2. Змінити пароль");
        System.out.println("0. Назад");
        System.out.print("Вибір: ");

        String choice = scanner.nextLine();
        if (choice.equals("1")) {
            System.out.print("Нове ім'я: ");
            String newName = scanner.nextLine();
            if (!newName.isBlank()) {
                currentUser.setFirstName(newName);
                userService.update(currentUser);
                System.out.println(ConsoleColors.GREEN + "Оновлено!" + ConsoleColors.RESET);
            }
        } else if (choice.equals("2")) {
            System.out.print("Новий пароль: ");
            String newPass = scanner.nextLine();
            if (newPass.length() >= 6) {
                currentUser.setPassword(newPass);
                userService.update(currentUser);
                System.out.println(ConsoleColors.GREEN + "Пароль змінено!" + ConsoleColors.RESET);
            }
        }
    }

    private void removeFromCart() {
        System.out.print("Номер для видалення: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index >= 0 && index < cart.size()) {
                cart.remove(index);
            }
        } catch (Exception e) {
            System.out.println("Помилка.");
        }
    }

    private void processCheckout() {
        if (cart.isEmpty()) {
            return;
        }
        System.out.print("Адреса доставки: ");
        String address = scanner.nextLine();
        Order newOrder = new Order(currentUser, address);
        for (Product p : cart) {
            newOrder.addProduct(p, 1);
            productService.updateProductStock(p.getIdAsString(), p.getStockQuantity() - 1);
        }
        orderService.createOrder(newOrder);
        System.out.println(ConsoleColors.GREEN + "Замовлення оформлено!" + ConsoleColors.RESET);
        cart.clear();
    }
}