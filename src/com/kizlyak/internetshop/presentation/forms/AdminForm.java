package com.kizlyak.internetshop.presentation.forms;

import com.kizlyak.internetshop.domain.dto.ProductStoreDto;
import com.kizlyak.internetshop.domain.model.Order;
import com.kizlyak.internetshop.domain.model.Product;
import com.kizlyak.internetshop.domain.model.User;
import com.kizlyak.internetshop.presentation.ConsoleColors;
import com.kizlyak.internetshop.service.OrderService;
import com.kizlyak.internetshop.service.ProductService;
import com.kizlyak.internetshop.service.UserService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class AdminForm {

    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;
    private final Scanner scanner = new Scanner(System.in);

    public AdminForm(ProductService productService, OrderService orderService,
          UserService userService) {
        this.productService = productService;
        this.orderService = orderService;
        this.userService = userService;
    }

    public void render() {
        while (true) {
            System.out.println(
                  ConsoleColors.RED + "\n=== ПАНЕЛЬ АДМІНІСТРАТОРА ===" + ConsoleColors.RESET);
            System.out.println("1. Додати новий товар");
            System.out.println("2. Редагувати товар (ціна/кількість)");
            System.out.println("3. Видалити товар");
            System.out.println("4. Переглянути всі замовлення");
            System.out.println("5. Статистика продажів");
            System.out.println("6. Список користувачів");
            System.out.println("7. Стан складу (залишки)");
            System.out.println("0. Вихід");
            System.out.print("Вибір: ");

            String choice = scanner.nextLine();
            if (choice.equals("0")) {
                break;
            }

            switch (choice) {
                case "1" -> addNewProduct();
                case "2" -> editProduct();
                case "3" -> deleteProduct();
                case "4" -> showAllOrders();
                case "5" -> orderService.showSalesStatistics();
                case "6" -> showAllUsers();
                case "7" -> showStockStatus();
                default -> System.out.println("Невірний вибір.");
            }
        }
    }

    private void showStockStatus() {
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            System.out.println(ConsoleColors.YELLOW + "Склад порожній." + ConsoleColors.RESET);
            return;
        }

        System.out.println(ConsoleColors.CYAN + "\n" + "=".repeat(95) + ConsoleColors.RESET);
        System.out.printf(
              ConsoleColors.CYAN + "| %-30s | %-25s | %-12s | %-10s |%n" + ConsoleColors.RESET,
              "Назва товару", "Категорія", "Ціна", "Залишок");
        System.out.println("-".repeat(95));

        BigDecimal totalValue = BigDecimal.ZERO;

        for (Product p : products) {
            String name =
                  p.getName().length() > 30 ? p.getName().substring(0, 27) + "..." : p.getName();
            String category = p.getCategory().getName().length() > 25 ?
                  p.getCategory().getName().substring(0, 22) + "..." : p.getCategory().getName();

            // ОНОВЛЕНА ЛОГІКА КОЛЬОРІВ
            String stockColor;
            int qty = p.getStockQuantity();

            if (qty <= 5) {
                stockColor = ConsoleColors.RED;    // 5 і менше - ЧЕРВОНИЙ
            } else if (qty <= 20) {
                stockColor = ConsoleColors.YELLOW; // 6 - 20 - ЖОВТИЙ
            } else {
                stockColor = ConsoleColors.GREEN;  // 21 і більше - ЗЕЛЕНИЙ
            }

            System.out.printf(
                  "| %-30s | %-25s | %-12.2f | " + stockColor + "%-10d" + ConsoleColors.RESET
                        + " |%n",
                  name, category, p.getPrice(), qty);

            totalValue = totalValue.add(p.getPrice().multiply(BigDecimal.valueOf(qty)));
        }

        System.out.println("-".repeat(95));
        System.out.printf(ConsoleColors.GREEN + "ЗАГАЛЬНА ВАРТІСТЬ ТОВАРІВ НА СКЛАДІ: %.2f грн%n"
              + ConsoleColors.RESET, totalValue);
        System.out.println(ConsoleColors.CYAN + "=".repeat(95) + ConsoleColors.RESET);
    }

    private void addNewProduct() {
        try {
            System.out.println("\n--- Додавання товару ---");
            System.out.print("Назва: ");
            String name = scanner.nextLine();
            System.out.print("Опис: ");
            String desc = scanner.nextLine();
            System.out.print("Ціна: ");
            BigDecimal price = new BigDecimal(scanner.nextLine());
            System.out.print("Кількість: ");
            int stock = Integer.parseInt(scanner.nextLine());

            String cat = selectCategory();

            productService.addProduct(new ProductStoreDto(name, desc, price, stock, cat));
            System.out.println(
                  ConsoleColors.GREEN + "Товар додано до категорії: " + cat + ConsoleColors.RESET);
        } catch (Exception e) {
            System.out.println(
                  ConsoleColors.RED + "Помилка: Некоректні дані." + ConsoleColors.RESET);
        }
    }

    private void editProduct() {
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("Список товарів порожній.");
            return;
        }

        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            System.out.printf("%d. %s [Категорія: %s] (Ціна: %s, Залишок: %d)%n",
                  i + 1, p.getName(), p.getCategory().getName(), p.getPrice(),
                  p.getStockQuantity());
        }

        System.out.print("\nОберіть номер товару для редагування (0 - вихід): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine()) - 1;
            if (idx == -1) {
                return;
            }

            Product p = products.get(idx);
            String productId = p.getIdAsString();

            System.out.print("Нова ціна (поточна: " + p.getPrice() + ", Enter - пропустити): ");
            String priceStr = scanner.nextLine();
            if (!priceStr.isBlank()) {
                productService.updateProductPrice(productId, new BigDecimal(priceStr));
            }

            System.out.print(
                  "Нова кількість (поточна: " + p.getStockQuantity() + ", Enter - пропустити): ");
            String stockStr = scanner.nextLine();
            if (!stockStr.isBlank()) {
                productService.updateProductStock(productId, Integer.parseInt(stockStr));
            }

            System.out.print("Новий опис (Enter - пропустити): ");
            String descStr = scanner.nextLine();
            if (!descStr.isBlank()) {
                productService.updateProductDescription(productId, descStr);
            }

            System.out.println("Бажаєте змінити категорію? (1 - так, Enter - пропустити)");
            String catChoice = scanner.nextLine();
            if (catChoice.equals("1")) {
                String newCat = selectCategory();
                productService.updateProductCategory(productId, newCat);
            }

            System.out.println(
                  ConsoleColors.GREEN + "Товар успішно оновлено!" + ConsoleColors.RESET);

        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Помилка при редагуванні: " + e.getMessage()
                  + ConsoleColors.RESET);
        }
        productService.update();
    }

    private void showAllUsers() {
        System.out.println(
              ConsoleColors.CYAN + "\n--- СПИСОК КОРИСТУВАЧІВ ---" + ConsoleColors.RESET);
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("Користувачів не знайдено.");
            return;
        }

        System.out.printf("%-20s | %-25s | %-10s%n", "Ім'я", "Email", "Роль");
        System.out.println("------------------------------------------------------------");
        for (User u : users) {
            System.out.printf("%-20s | %-25s | %-10s%n",
                  u.getFirstName(), u.getEmail(), u.getRole());
        }
    }

    private String selectCategory() {
        List<Product> products = productService.getAllProducts();
        List<String> categories = products.stream()
              .map(p -> p.getCategory().getName())
              .distinct()
              .toList();

        if (categories.isEmpty()) {
            System.out.print("Категорій ще немає. Введіть назву нової категорії: ");
            return scanner.nextLine();
        }

        System.out.println("\nОберіть категорію:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, categories.get(i));
        }
        System.out.println("0. Додати нову категорію");
        System.out.print("Ваш вибір: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0 && choice <= categories.size()) {
                return categories.get(choice - 1);
            } else if (choice == 0) {
                System.out.print("Введіть назву нової категорії: ");
                return scanner.nextLine();
            }
        } catch (Exception e) {
            System.out.println("Невірний ввід. Вибрано 'Загальне'");
        }
        return "Загальне";
    }

    private void deleteProduct() {
        List<Product> products = productService.getAllProducts();
        for (int i = 0; i < products.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, products.get(i).getName());
        }
        try {
            int idx = Integer.parseInt(scanner.nextLine()) - 1;
            productService.deleteProduct(products.get(idx).getIdAsString());
            System.out.println("Видалено.");
        } catch (Exception e) {
            System.out.println("Помилка.");
        }
    }

    private void showAllOrders() {
        System.out.println("\n--- УСІ ЗАМОВЛЕННЯ ---");
        List<Order> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("Замовлень ще немає.");
            return;
        }
        for (Order order : orders) {
            String shortId = order.getIdAsString().substring(0, 8);
            String email = order.getCustomer().getEmail();
            System.out.printf("Замовлення #%s | Клієнт: %s | Сума: %.2f грн%n",
                  shortId, email, order.getTotalAmount());
        }
    }
}