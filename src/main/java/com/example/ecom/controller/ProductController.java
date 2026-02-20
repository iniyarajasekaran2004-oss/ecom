package com.example.ecom.controller;

import com.example.ecom.dto.requestDto.ProductRequestDto;
import com.example.ecom.dto.responseDto.ProductResponseDto;
import com.example.ecom.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;


/**
 * REST controller for managing products.
 * <p>
 * Provides endpoints to create, retrieve, update, and delete products.
 */
@Tag(name = "Product Management")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /**
     * Creates a new product.
     *
     * @param productRequestDto product details
     * @return created product with HTTP 201 status
     */

    @PostMapping("/product")
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        return new ResponseEntity<>(productService.createProduct(productRequestDto), HttpStatus.CREATED);
    }

    /**
     * Retrieves a product by ID.
     *
     * @param id product ID
     * @return product details
     */

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * Retrieves all products.
     *
     * @return list of products
     */

    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return ResponseEntity.ok(productService.getAllProducts(page, size));
    }


    /**
     * Updates a product by ID.
     *
     * @param id  product ID
     * @param dto updated product details
     * @return updated product response
     */
    @PutMapping("/product/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    /**
     * Deletes a product by ID.
     *
     * @param id product ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
