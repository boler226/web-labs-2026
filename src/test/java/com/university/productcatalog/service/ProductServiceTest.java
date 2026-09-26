package com.university.productcatalog.service;

import com.university.productcatalog.entity.Product;
import com.university.productcatalog.exception.ProductNotFoundException;
import com.university.productcatalog.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository);
    }

    @Test
    void findByIdReturnsProductWhenPresent() {
        Product product = sampleProduct(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.findById(1L);

        assertThat(result.getName()).isEqualTo("Keyboard");
    }

    @Test
    void findByIdThrowsWhenMissing() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(99L))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void createDelegatesToRepository() {
        Product product = sampleProduct(null);
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.create(product);

        assertThat(result).isEqualTo(product);
        verify(productRepository).save(product);
    }

    @Test
    void deleteThrowsWhenProductMissing() {
        when(productRepository.existsById(5L)).thenReturn(false);

        assertThatThrownBy(() -> productService.delete(5L))
                .isInstanceOf(ProductNotFoundException.class);
    }

    private Product sampleProduct(Long id) {
        Product product = new Product();
        product.setId(id);
        product.setName("Keyboard");
        product.setCategory("Electronics");
        product.setPrice(BigDecimal.valueOf(49.99));
        product.setQuantity(10);
        return product;
    }
}
