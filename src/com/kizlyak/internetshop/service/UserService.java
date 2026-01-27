package com.kizlyak.internetshop.service;

import com.kizlyak.internetshop.domain.model.User;
import com.kizlyak.internetshop.infrastructure.UnitOfWork;
import java.util.List;

public class UserService {

    private final UnitOfWork uow;

    public UserService(UnitOfWork uow) {
        this.uow = uow;
    }

    // Метод для збереження будь-яких змін користувача
    public void update(User user) {
        this.uow.commit();
    }

    public List<User> getAllUsers() {
        return uow.getUserRepository().findAll();
    }
}