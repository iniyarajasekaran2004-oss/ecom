package com.example.ecom.service;

import com.example.ecom.dto.requestDto.ProductRequestDto;
import com.example.ecom.dto.responseDto.ProductResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service interface for managing Product operations.
 * <p>
 * Defines business operations for creating, retrieving,
 * updating, and deleting products.
 */
public interface ProductService {
    /**
     * Creates a new product.
     *
     * @param productRequestDto product request payload
     * @return created product response DTO
     */
    ProductResponseDto createProduct(ProductRequestDto productRequestDto);

    /**
     * Retrieves a product by its ID.
     *
     * @param id product ID
     * @return product response DTO
     */
    ProductResponseDto getProductById(Long id);

    /**
     * Retrieves all products.
     *
     * @return list of product response DTOs
     */
    Page<ProductResponseDto> getAllProducts(int page, int size);

    /**
     * Updates an existing product.
     *
     * @param id                product ID
     * @param productRequestDto updated product details
     * @return updated product response DTO
     */
    ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto);

    /**
     * Deletes a product by its ID.
     *
     * @param id product ID
     */
    void deleteProduct(Long id);
}
