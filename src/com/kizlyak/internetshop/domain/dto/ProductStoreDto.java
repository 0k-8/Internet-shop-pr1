package com.kizlyak.internetshop.domain.dto;

import com.kizlyak.internetshop.domain.exception.EntityValidationException;
import com.kizlyak.internetshop.domain.validator.ValidationError;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public record ProductStoreDto(
      String name,
      String description,
      BigDecimal price,
      int stockQuantity,
      String categoryName
) {

    public ProductStoreDto {
        List<ValidationError> errors = new ArrayList<>();

        if (name == null || name.isBlank()) {
            errors.add(new ValidationError("name", "Назва товару обов'язкова"));
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(new ValidationError("price", "Ціна має бути більшою за нуль"));
        }
        if (stockQuantity < 0) {
            errors.add(new ValidationError("stock", "Кількість не може бути від'ємною"));
        }
        // 2. Валідація для категорії
        if (categoryName == null || categoryName.isBlank()) {
            errors.add(new ValidationError("category", "Категорія обов'язкова"));
        }

        if (!errors.isEmpty()) {
            throw new EntityValidationException(errors);
        }
    }
}