package com.kizlyak.internetshop.service.impl;

import com.kizlyak.internetshop.domain.Product;
import com.kizlyak.internetshop.domain.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductRepositoryImpl implements ProductRepository {

    // Список, де зберігаються всі товари (ваша тимчасова база даних)
    private final List<Product> products = new ArrayList<>();

    @Override
    public void save(Product product) {
        // Якщо товар з таким ID вже є, ми його замінюємо, якщо ні — додаємо новий
        findById(product.getId()).ifPresentOrElse(
              existingProduct -> {
                  int index = products.indexOf(existingProduct);
                  products.set(index, product);
              },
              () -> products.add(product)
        );
    }

    @Override
    public List<Product> findAll() {
        // Повертаємо копію списку, щоб оригінал не можна було змінити випадково
        return new ArrayList<>(products);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        // Шукаємо товар за його унікальним ідентифікатором
        return products.stream()
              .filter(p -> p.getId().equals(id))
              .findFirst();
    }

    @Override
    public void delete(UUID id) {
        // Видаляємо товар зі списку за його ID
        products.removeIf(p -> p.getId().equals(id));
    }
}