package com.kizlyak.internetshop.domain.repository;


import com.kizlyak.internetshop.domain.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    void save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email); // Важливо для логіну

    List<User> findAll();

    void delete(UUID id);
}