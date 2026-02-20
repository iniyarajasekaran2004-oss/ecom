package com.example.ecom.service.impl;

import com.example.ecom.dto.requestDto.ProductRequestDto;
import com.example.ecom.dto.responseDto.ProductResponseDto;
import com.example.ecom.entity.Product;
import com.example.ecom.exception.ProductInUseException;
import com.example.ecom.exception.ResourceNotFoundException;
import com.example.ecom.mapper.ProductMapper;
import com.example.ecom.repository.OrderItemRepository;
import com.example.ecom.repository.ProductRepository;
import com.example.ecom.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;

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
    private final OrderItemRepository orderItemRepository;
    private final ProductMapper productMapper;


    /**
     * Creates a new product.
     *
     * @param productRequestDto product request payload
     * @return created product response DTO
     */
    @Override
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        log.info("Creating Product:{}", productRequestDto.getName());
//        Product product = Product.builder()
//                .name(productRequestDto.getName())
//                .stock(productRequestDto.getStock())
//                .price(productRequestDto.getPrice())
//                .build();
        Product product = productMapper.toEntity(productRequestDto);
        Product saved = productRepository.save(product);

//        return mapToResponse(saved);
        log.debug("Product saved with id: {}", saved.getId());
        return productMapper.toDto(saved);

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
                .orElseThrow(() -> {
                    log.error("Product not found with id: {}", id);
                    return new ResourceNotFoundException("Product not found with id:" + id);
                });
//        return mapToResponse(product);
        return productMapper.toDto(product);
    }

    /**
     * Retrieves all products from the database.
     *
     * @return list of product response DTOs
     */
    //@Override
//    public List<ProductResponseDto> getAllProducts() {
//        log.info("Get all products");
//        return productRepository.findAll()
//                .stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
    @Override
    public Page<ProductResponseDto> getAllProducts(int page, int size) {

        log.debug("Fetching products with pagination - page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage = productRepository.findAll(pageable);

        return productPage.map(productMapper::toDto);
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
                .orElseThrow(() -> {
                    log.error("Update failed. Product not found with id: {}", id);
                    return new ResourceNotFoundException("Product not found with id " + id);
                });
        product.setName(productRequestDto.getName());
        product.setStock(productRequestDto.getStock());
        product.setPrice(productRequestDto.getPrice());
        Product updated = productRepository.save(product);
        return productMapper.toDto(product);

    }

    /**
     * Deletes a product by its ID.
     *
     * @param id product ID
     */
    @Override
    public void deleteProduct(Long id) {
        log.info("Delete Product by id :{}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));

        if (orderItemRepository.existsByProductId(id)) {
            throw new ProductInUseException(
                    "Product cannot be deleted because it exists in ordered item");
        }
        productRepository.deleteById(id);
    }

    /**
     * Converts Product entity to ProductResponseDto.
     *
     * @param product product entity
     * @return mapped response DTO
     */
//    private ProductResponseDto mapToResponse(Product product) {
//        return ProductResponseDto.builder()
//                .id(product.getId())
//                .name(product.getName())
//                .stock(product.getStock())
//                .price(product.getPrice())
//                .build();
//    }
}
