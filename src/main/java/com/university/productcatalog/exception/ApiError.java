package com.university.productcatalog.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Uniform JSON error shape returned by the REST API.
 * {@code fieldErrors} is populated only for validation failures (400).
 */
public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String, String> fieldErrors
) {

    public static ApiError of(int status, String error, String message) {
        return new ApiError(LocalDateTime.now(), status, error, message, null);
    }

    public static ApiError validation(int status, String error, Map<String, String> fieldErrors) {
        return new ApiError(LocalDateTime.now(), status, error, "Помилка валідації полів", fieldErrors);
    }
}
