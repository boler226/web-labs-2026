package com.university.productcatalog.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Request body for POST/PUT /api/products.
 * Kept separate from the entity so the API contract doesn't leak JPA internals.
 */
public record ProductRequest(

        @NotBlank(message = "Назва товару обов'язкова")
        @Size(max = 150, message = "Назва не може перевищувати 150 символів")
        String name,

        @Size(max = 1000, message = "Опис не може перевищувати 1000 символів")
        String description,

        @NotBlank(message = "Категорія обов'язкова")
        @Size(max = 100, message = "Категорія не може перевищувати 100 символів")
        String category,

        @NotNull(message = "Ціна обов'язкова")
        @DecimalMin(value = "0.0", inclusive = true, message = "Ціна не може бути від'ємною")
        BigDecimal price,

        @NotNull(message = "Кількість обов'язкова")
        @Min(value = 0, message = "Кількість не може бути від'ємною")
        Integer quantity
) {
}
