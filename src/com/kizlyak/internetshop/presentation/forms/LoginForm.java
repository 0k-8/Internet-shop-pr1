package com.kizlyak.internetshop.presentation.forms;

import com.kizlyak.internetshop.presentation.ConsoleColors;
import java.util.Scanner;

public class LoginForm {

    private final Scanner scanner = new Scanner(System.in);

    public LoginData render() {
        System.out.println(ConsoleColors.CYAN + "\n=== ВХІД У СИСТЕМУ ===" + ConsoleColors.RESET);
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();
        return new LoginData(email, password);
    }

    public record LoginData(String email, String password) {

    }
}