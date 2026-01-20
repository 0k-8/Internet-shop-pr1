package com.kizlyak.internetshop.service.impl; // Переконайтеся, що папка відповідає пакету

import com.kizlyak.internetshop.domain.User;
import com.kizlyak.internetshop.domain.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepository {

    // Тимчасове сховище користувачів
    private final List<User> users = new ArrayList<>();

    @Override
    public void save(User user) {
        // Якщо користувач вже є (за ID) — видаляємо старий запис і додаємо новий (оновлення)
        users.removeIf(u -> u.getId().equals(user.getId()));
        users.add(user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return users.stream()
              .filter(u -> u.getId().equals(id))
              .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.stream()
              .filter(u -> u.getEmail().equalsIgnoreCase(email))
              .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public void delete(UUID id) {
        users.removeIf(u -> u.getId().equals(id));
    }
}