package com.kizlyak.internetshop.presentation.forms;

import com.kizlyak.internetshop.domain.dto.UserRegistrationDto;
import com.kizlyak.internetshop.domain.exception.EntityValidationException;
import com.kizlyak.internetshop.presentation.ConsoleColors;
import com.kizlyak.internetshop.service.AuthService;
import java.util.Scanner;

public class SignUpForm {

    private final Scanner scanner = new Scanner(System.in);
    private final AuthService authService;

    public SignUpForm(AuthService authService) {
        this.authService = authService;
    }

    public UserRegistrationDto render() {
        System.out.println(ConsoleColors.CYAN + "\n=== ФОРМА РЕЄСТРАЦІЇ ===" + ConsoleColors.RESET);

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Логін: ");
        String username = scanner.nextLine();

        System.out.print("Пароль: ");
        String password = scanner.nextLine();

        System.out.print("Ім'я: ");
        String fName = scanner.nextLine();

        System.out.print("Прізвище: ");
        String lName = scanner.nextLine();

        try {
            // 1. Створюємо DTO ВСЕРЕДИНІ try. Якщо пароль короткий — ми потрапимо в catch
            UserRegistrationDto dto = new UserRegistrationDto(username, email, password, fName,
                  lName);

            // 2. Починаємо реєстрацію (надсилаємо код)
            authService.startRegistration(email);

            System.out.println(
                  ConsoleColors.YELLOW
                        + "\n[!] Код підтвердження надіслано на вашу пошту."
                        + ConsoleColors.RESET);
            System.out.print("Введіть код підтвердження: ");
            String code = scanner.nextLine();

            // 3. Перевіряємо код
            if (authService.confirmCode(email, code)) {
                authService.register(dto);
                System.out.println(ConsoleColors.GREEN + "Реєстрація успішна! Ласкаво просимо."
                      + ConsoleColors.RESET);
                return dto;
            } else {
                System.out.println(
                      ConsoleColors.RED + "Невірний код підтвердження!" + ConsoleColors.RESET);
            }

        } catch (EntityValidationException e) {
            // Спеціальна обробка для помилок валідації (короткий пароль тощо)
            System.out.println(
                  ConsoleColors.RED + "\nПомилка заповнення форми:" + ConsoleColors.RESET);
            e.getErrors().forEach(err -> System.out.println("- " + err.getMessage()));
        } catch (Exception e) {
            // Обробка всіх інших помилок
            System.out.println(
                  ConsoleColors.RED + "Сталася помилка: " + e.getMessage() + ConsoleColors.RESET);
        }

        return null; // Повертаємо null, якщо реєстрація не вдалася, програма при цьому НЕ вилітає
    }
}