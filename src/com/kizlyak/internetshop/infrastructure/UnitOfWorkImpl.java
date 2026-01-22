package com.kizlyak.internetshop.infrastructure;

import com.google.gson.reflect.TypeToken;
import com.kizlyak.internetshop.domain.model.Product;
import com.kizlyak.internetshop.domain.model.User;
import com.kizlyak.internetshop.domain.cache.IdentityMap;
import java.util.ArrayList;
import java.util.List;

public class UnitOfWorkImpl implements UnitOfWork {

    private final IdentityMap<Product> productCache = new IdentityMap<>();
    private final IdentityMap<User> userCache = new IdentityMap<>();

    // Мапери з використанням TypeToken для GSON
    private final JsonDataMapper<Product> productMapper = new JsonDataMapper<>(
          "products.json", new TypeToken<List<Product>>() {
    }.getType());
    private final JsonDataMapper<User> userMapper = new JsonDataMapper<>(
          "users.json", new TypeToken<List<User>>() {
    }.getType());

    public UnitOfWorkImpl() {
        load(); // Автоматичне завантаження при старті
    }

    private void load() {
        productMapper.readFromFile().forEach(productCache::add);
        userMapper.readFromFile().forEach(userCache::add);
    }

    public IdentityMap<Product> getProductCache() {
        return productCache;
    }

    public IdentityMap<User> getUserCache() {
        return userCache;
    }

    @Override
    public void commit() {
        productMapper.writeToFile(new ArrayList<>(productCache.getAll().values()));
        userMapper.writeToFile(new ArrayList<>(userCache.getAll().values()));
        System.out.println("Усі зміни синхронізовано з JSON файлами.");
    }
}