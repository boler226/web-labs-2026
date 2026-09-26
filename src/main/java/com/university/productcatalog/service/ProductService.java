package com.university.productcatalog.service;

import com.university.productcatalog.entity.Product;
import com.university.productcatalog.exception.ProductNotFoundException;
import com.university.productcatalog.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll(String query) {
        if (StringUtils.hasText(query)) {
            return productRepository.search(query.trim());
        }
        return productRepository.findAllByOrderByNameAsc();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product update(Long id, Product changes) {
        Product existing = findById(id);
        existing.setName(changes.getName());
        existing.setDescription(changes.getDescription());
        existing.setCategory(changes.getCategory());
        existing.setPrice(changes.getPrice());
        existing.setQuantity(changes.getQuantity());
        return existing;
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }
}
