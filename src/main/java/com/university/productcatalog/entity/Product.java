package com.university.productcatalog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Назва товару обов'язкова")
    @Size(max = 150, message = "Назва не може перевищувати 150 символів")
    @Column(nullable = false, length = 150)
    private String name;

    @Size(max = 1000, message = "Опис не може перевищувати 1000 символів")
    @Column(length = 1000)
    private String description;

    @NotBlank(message = "Категорія обов'язкова")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String category;

    @NotNull(message = "Ціна обов'язкова")
    @DecimalMin(value = "0.0", inclusive = true, message = "Ціна не може бути від'ємною")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @NotNull(message = "Кількість обов'язкова")
    @Min(value = 0, message = "Кількість не може бути від'ємною")
    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
