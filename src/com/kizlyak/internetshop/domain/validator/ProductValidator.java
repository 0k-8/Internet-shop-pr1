package com.kizlyak.internetshop.domain.validator;

import com.kizlyak.internetshop.domain.model.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductValidator {

    /**
     * Метод для перевірки даних товару перед збереженням або продажем.
     */
    public List<ValidationError> validate(Product product) {
        List<ValidationError> errors = new ArrayList<>();

        // 1. Перевірка назви
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            errors.add(new ValidationError("name", "Назва товару не може бути порожньою."));
        } else if (product.getName().length() < 3) {
            errors.add(new ValidationError("name", "Назва занадто коротка (мінімум 3 символи)."));
        }

        // 2. Перевірка ціни
        if (product.getPrice() == null) {
            errors.add(new ValidationError("price", "Ціна має бути вказана."));
        } else if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(new ValidationError("price", "Ціна повинна бути більшою за нуль."));
        }

        // 3. Перевірка кількості на складі
        if (product.getStockQuantity() < 0) {
            errors.add(
                  new ValidationError("stockQuantity", "Кількість товару не може бути від'ємною."));
        }

        return errors;
    }
}