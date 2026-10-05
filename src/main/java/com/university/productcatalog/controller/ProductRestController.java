package com.university.productcatalog.controller;

import com.university.productcatalog.dto.ProductRequest;
import com.university.productcatalog.dto.ProductResponse;
import com.university.productcatalog.entity.Product;
import com.university.productcatalog.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponse> findAll(@RequestParam(required = false) String query) {
        return productService.findAll(query).stream()
                .map(ProductResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable Long id) {
        return ProductResponse.from(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request,
                                                    UriComponentsBuilder uriBuilder) {
        Product created = productService.create(toEntity(request));
        var location = uriBuilder.path("/api/products/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(ProductResponse.from(created));
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        Product updated = productService.update(id, toEntity(request));
        return ProductResponse.from(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Product toEntity(ProductRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setCategory(request.category());
        product.setPrice(request.price());
        product.setQuantity(request.quantity());
        return product;
    }
}
