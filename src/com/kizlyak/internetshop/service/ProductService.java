package com.kizlyak.internetshop.service;

import com.kizlyak.internetshop.domain.dto.ProductStoreDto;
import com.kizlyak.internetshop.domain.model.Category;
import com.kizlyak.internetshop.domain.model.Product;
import com.kizlyak.internetshop.infrastructure.UnitOfWork;
import java.math.BigDecimal;
import java.util.List;

public class ProductService {

    private final UnitOfWork uow;

    public ProductService(UnitOfWork uow) {
        this.uow = uow;
    }

    public void updateProductPrice(String id, BigDecimal newPrice) {
        // Шукаємо товар через stream, порівнюючи UUID як рядки
        uow.getProductRepository().findAll().stream()
              .filter(p -> p.getId().toString().equals(id))
              .findFirst()
              .ifPresent(product -> {
                  product.setPrice(newPrice);
                  uow.commit();
              });
    }

    public void updateProductStock(String id, int newQuantity) {
        uow.getProductRepository().findAll().stream()
              .filter(p -> p.getId().toString().equals(id))
              .findFirst()
              .ifPresent(product -> {
                  product.setStockQuantity(newQuantity);
                  uow.commit();
              });
    }

    public void updateProductDescription(String id, String newDescription) {
        uow.getProductRepository().findAll().stream()
              .filter(p -> p.getId().toString().equals(id))
              .findFirst()
              .ifPresent(product -> {
                  product.setDescription(
                        newDescription); // Переконайся, що в моделі Product є setDescription
                  uow.commit();
              });
    }

    public void update() {
        // Цей рядок запише всі змінені об'єкти з пам'яті в JSON
        uow.commit();
    }

    public void updateProductCategory(String id, String newCategoryName) {
        uow.getProductRepository().findAll().stream()
              .filter(p -> p.getId().toString().equals(id))
              .findFirst()
              .ifPresent(product -> {
                  product.setCategory(
                        new Category(newCategoryName)); // Переконайся, що в Product є setCategory
                  uow.commit();
              });
    }

    public void deleteProduct(String id) {
        // Знаходимо товар, щоб отримати його оригінальний UUID для видалення
        uow.getProductRepository().findAll().stream()
              .filter(p -> p.getId().toString().equals(id))
              .findFirst()
              .ifPresent(product -> {
                  uow.getProductRepository().delete(product.getId());
                  uow.commit();
              });
    }

    public void addProduct(ProductStoreDto dto) {
        Product product = new Product(
              dto.name(),
              dto.description(),
              dto.price(),
              dto.stockQuantity(),
              new Category(dto.categoryName())
        );
        uow.getProductRepository().save(product);
        uow.commit();
    }

    public List<Product> getAllProducts() {
        return uow.getProductRepository().findAll();
    }

    public List<Product> searchByName(String name) {
        if (name == null || name.isBlank()) {
            return getAllProducts();
        }
        return uow.getProductRepository().findAll().stream()
              .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
              .toList();
    }

    public List<Product> getProductsByCategoryName(String categoryName) {
        return uow.getProductRepository().findAll().stream()
              .filter(p -> p.getCategory() != null &&
                    p.getCategory().getName().equalsIgnoreCase(categoryName))
              .toList();
    }
}