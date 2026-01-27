package com.kizlyak.internetshop.domain.impl;

import com.kizlyak.internetshop.domain.cache.IdentityMap;
import com.kizlyak.internetshop.domain.model.User;
import com.kizlyak.internetshop.domain.repository.UserRepository;
import com.kizlyak.internetshop.infrastructure.JsonDataMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepository {

    // Тимчасове сховище користувачів
    private final JsonDataMapper<User> userMapper;
    private final List<User> users = new ArrayList<>();
    private final IdentityMap<User> cache;

    public UserRepositoryImpl(JsonDataMapper<User> userMapper, IdentityMap<User> cache) {
        this.userMapper = userMapper;
        this.cache = cache;
    }

    @Override
    public void save(User user) {
        userMapper.writeToFile(findAll());
        // Викликаємо метод add у нашого cache
        if (user != null) {
            cache.add(user);
        }
    }

    @Override
    public Optional<User> findById(UUID id) {
        return users.stream()
              .filter(u -> u.getId().equals(id))
              .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        // Тепер findAll() поверне свіжі дані з файлу
        return findAll().stream()
              .filter(u -> u.getEmail().trim().equalsIgnoreCase(email.trim()))
              .findFirst();
    }


    @Override
    public List<User> findAll() {
        // Читаємо напряму з файлу, щоб точно не було 0
        List<User> users = userMapper.readFromFile();

        // Синхронізуємо з кешем, щоб IdentityMap теж знав про них
        users.forEach(cache::add);

        return users;
    }

    @Override
    public void delete(UUID id) {
        users.removeIf(u -> u.getId().equals(id));
    }
}