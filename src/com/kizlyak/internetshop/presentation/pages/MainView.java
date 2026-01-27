package com.kizlyak.internetshop.presentation.pages;

import com.kizlyak.internetshop.domain.dto.UserRegistrationDto;
import com.kizlyak.internetshop.domain.model.User;
import com.kizlyak.internetshop.domain.model.enums.UserRole;
import com.kizlyak.internetshop.presentation.ConsoleColors;
import com.kizlyak.internetshop.presentation.forms.AdminForm;
import com.kizlyak.internetshop.presentation.forms.LoginForm;
import com.kizlyak.internetshop.presentation.forms.SignUpForm;
import com.kizlyak.internetshop.presentation.forms.UserForm;
import com.kizlyak.internetshop.service.AuthService;
import com.kizlyak.internetshop.service.OrderService;
import com.kizlyak.internetshop.service.ProductService;
import com.kizlyak.internetshop.service.UserService;
import java.util.Scanner;

public class MainView {

    private final AuthService authService;
    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService; // ДОДАНО
    private final Scanner scanner = new Scanner(System.in);
    private User currentUser = null;

    // ОНОВЛЕНО КОНСТРУКТОР
    public MainView(AuthService authService, ProductService productService,
          OrderService orderService, UserService userService) {
        this.authService = authService;
        this.productService = productService;
        this.orderService = orderService;
        this.userService = userService; // ДОДАНО
    }

    public void show() {
        while (true) {
            renderHeader();
            renderMenu();
            String choice = scanner.nextLine().toUpperCase();
            handleChoice(choice);
        }
    }

    private void renderHeader() {
        System.out.println(
              ConsoleColors.BLUE + "\n*********************************" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "      INTERNET SHOP v1.0" + ConsoleColors.RESET);
        if (currentUser != null) {
            System.out.println(" Вітаємо, " + ConsoleColors.GREEN + currentUser.getFirstName() + " "
                  + currentUser.getLastName() + ConsoleColors.RESET + "!");
        }
        System.out.println(
              ConsoleColors.BLUE + "*********************************" + ConsoleColors.RESET);
    }

    private void renderMenu() {
        if (currentUser == null) {
            System.out.println(
                  "1. " + ConsoleColors.CYAN + "Зареєструватися" + ConsoleColors.RESET);
            System.out.println("2. " + ConsoleColors.CYAN + "Увійти" + ConsoleColors.RESET);
            System.out.println(
                  "E. " + ConsoleColors.RED + "Вийти з програми" + ConsoleColors.RESET);
        }
        System.out.print("\nВаш вибір: ");
    }

    private void handleChoice(String choice) {
        if (currentUser == null) {
            switch (choice) {
                case "1" -> handleSignUp();
                case "2" -> handleLogin();
                case "E" -> System.exit(0);
                default -> System.out.println(
                      ConsoleColors.RED + "Невірний вибір!" + ConsoleColors.RESET);
            }
        } else {
            startUserSession();
        }
    }

    private void handleLogin() {
        try {
            LoginForm loginForm = new LoginForm();
            LoginForm.LoginData data = loginForm.render();

            this.currentUser = authService.login(data.email(), data.password());

            if (currentUser != null) {
                System.out.println(
                      ConsoleColors.GREEN + "Вхід успішний! Вітаємо, " + currentUser.getFirstName()
                            + ConsoleColors.RESET);
                startUserSession();
            }
        } catch (Exception e) {
            System.out.println(
                  ConsoleColors.RED + "Помилка входу: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    private void startUserSession() {
        if (currentUser == null) {
            return;
        }

        if (currentUser.getRole() == UserRole.ADMIN) {
            // ТЕПЕР ПЕРЕДАЄМО ТРИ СЕРВІСИ
            new AdminForm(productService, orderService, userService).render();
        } else {
            // Додаємо userService також у UserForm, щоб там працювало оновлення профілю
            new UserForm(productService, orderService, userService, currentUser).render();
        }

        System.out.println(ConsoleColors.YELLOW + "Сесію завершено. Повернення до головного меню."
              + ConsoleColors.RESET);
        this.currentUser = null;
    }

    private void handleSignUp() {
        SignUpForm form = new SignUpForm(authService);
        UserRegistrationDto result = form.render();
        if (result != null) {
            System.out.println(
                  ConsoleColors.GREEN + "Тепер ви можете увійти в систему." + ConsoleColors.RESET);
        }
    }
}