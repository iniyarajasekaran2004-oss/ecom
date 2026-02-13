package com.example.ecom.repository;

import com.example.ecom.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Product entity.
 * <p>
 * Extends JpaRepository to provide standard CRUD operations
 * for product persistence and database interactions.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
