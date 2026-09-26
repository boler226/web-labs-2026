package com.university.productcatalog.repository;

import com.university.productcatalog.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByOrderByNameAsc();

    // Simple case-insensitive search by name or category, used by the catalog search box
    @Query("""
            SELECT p FROM Product p
            WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(p.category) LIKE LOWER(CONCAT('%', :query, '%'))
            ORDER BY p.name ASC
            """)
    List<Product> search(@Param("query") String query);
}
