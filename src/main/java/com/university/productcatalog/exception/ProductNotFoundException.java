package com.university.productcatalog.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Товар з id=%d не знайдено".formatted(id));
    }
}
