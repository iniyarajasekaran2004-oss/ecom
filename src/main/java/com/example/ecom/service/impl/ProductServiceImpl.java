package com.example.ecom.service.impl;

import com.example.ecom.dto.requestDto.ProductRequestDto;
import com.example.ecom.dto.responseDto.ProductResponseDto;
import com.example.ecom.entity.Product;
import com.example.ecom.exception.ResourceNotFoundException;
import com.example.ecom.repository.ProductRepository;
import com.example.ecom.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;

import java.util.stream.Collectors;

/**
 * Service implementation for managing Product operations.
 * <p>
 * Provides business logic for creating, retrieving,
 * updating, and deleting products.
 * <p>
 * Uses ProductRepository for database interactions
 * and converts Product entities to ProductResponseDto.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    /**
     * Creates a new product.
     *
     * @param productRequestDto product request payload
     * @return created product response DTO
     */
    @Override
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        log.info("Creating Product:{}", productRequestDto.getName());
        Product product = Product.builder()
                .name(productRequestDto.getName())
                .stock(productRequestDto.getStock())
                .price(productRequestDto.getPrice())
                .build();

        Product saved = productRepository.save(product);

        return mapToResponse(saved);
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id product ID
     * @return product response DTO
     * @throws ResourceNotFoundException if product not found
     */

    @Override
    public ProductResponseDto getProductById(Long id) {
        log.info("Get Product by Id:{}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id:" + id));
        return mapToResponse(product);
    }

    /**
     * Retrieves all products from the database.
     *
     * @return list of product response DTOs
     */
    @Override
    public List<ProductResponseDto> getAllProducts() {
        log.info("Get all products");
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing product.
     *
     * @param id                product ID
     * @param productRequestDto updated product details
     * @return updated product response DTO
     * @throws RuntimeException if product not found
     */

    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        log.info("update Product by id :{}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
        product.setName(productRequestDto.getName());
        product.setStock(productRequestDto.getStock());
        product.setPrice(productRequestDto.getPrice());
        Product updated = productRepository.save(product);
        return mapToResponse(product);

    }

    /**
     * Deletes a product by its ID.
     *
     * @param id product ID
     */
    @Override
    public void deleteProduct(Long id) {
        log.info("Delete Product by id :{}", id);
        productRepository.deleteById(id);
    }

    /**
     * Converts Product entity to ProductResponseDto.
     *
     * @param product product entity
     * @return mapped response DTO
     */
    private ProductResponseDto mapToResponse(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .price(product.getPrice())
                .build();
    }
}
