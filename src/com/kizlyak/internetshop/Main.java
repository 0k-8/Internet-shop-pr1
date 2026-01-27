package com.kizlyak.internetshop;

import com.kizlyak.internetshop.infrastructure.UnitOfWorkImpl;
import com.kizlyak.internetshop.presentation.pages.MainView;
import com.kizlyak.internetshop.service.AuthService;
import com.kizlyak.internetshop.service.OrderService;
import com.kizlyak.internetshop.service.ProductService;
import com.kizlyak.internetshop.service.UserService;

public class Main {

    static void main(String[] args) {
        // 1. Ініціалізація інфраструктури
        UnitOfWorkImpl uow = new UnitOfWorkImpl();

        // 2. Ініціалізація сервісів
        AuthService authService = new AuthService(uow);
        ProductService productService = new ProductService(uow);
        UserService userService = new UserService(uow);

        // 3. Запуск інтерфейсу
        OrderService orderService = new OrderService(uow);
        MainView mainView = new MainView(authService, productService, orderService, userService);
        mainView.show();

    }
}