package com.kizlyak.internetshop.domain.repository;

import com.kizlyak.internetshop.domain.model.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    void save(Product product);

    List<Product> findAll();

    Optional<Product> findById(UUID id);

    void delete(UUID id);
}