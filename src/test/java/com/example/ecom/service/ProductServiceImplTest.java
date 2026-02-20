package com.example.ecom.service;

import com.example.ecom.dto.requestDto.ProductRequestDto;
import com.example.ecom.dto.responseDto.ProductResponseDto;
import com.example.ecom.entity.Product;
import com.example.ecom.mapper.ProductMapper;
import com.example.ecom.repository.ProductRepository;
import com.example.ecom.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductMapper productMapper;

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    void serviceShouldNotBeNull() {
        assertNotNull(productService);
    }

    //createProduct_shouldReturnResponse
    @Test
    void createProduct() {


        ProductRequestDto request = new ProductRequestDto();
        request.setName("Laptop");
        request.setPrice(50000.0);

        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(50000.0);

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Laptop");
        savedProduct.setPrice(50000.0);

        ProductResponseDto response = ProductResponseDto.builder()
                .id(1L)
                .name("Laptop")
                .price(50000.0)
                .stock(10)
                .build();

        when(productMapper.toEntity(request)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.toDto(savedProduct)).thenReturn(response);


        ProductResponseDto result = productService.createProduct(request);

        System.out.println(result);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());
    }

    // getProductById_shouldReturnProduct
    @Test
    void getProductById() {

        Long id = 1L;


        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(50000.0);

        ProductResponseDto response = ProductResponseDto.builder()
                .id(1L)
                .name("Laptop")
                .price(50000.0)
                .stock(10)
                .build();


        when(productRepository.findById(id)).thenReturn(java.util.Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(response);

        ProductResponseDto result = productService.getProductById(id);
        System.out.println(result);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());
    }

    //getProductById_shouldThrowException_whenProductNotFound
    @Test
    void getProductById_ProductNotFound_Exception() {

        Long id = 1L;


        when(productRepository.findById(id))
                .thenReturn(java.util.Optional.empty());


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productService.getProductById(id)
        );



        assertEquals("Product not found with id:1", exception.getMessage());
    }

    @Test
    void updateProduct_Success() {

        Long id = 1L;

        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("Updated Product");
        requestDto.setPrice(200.0);
        requestDto.setStock(5);

        Product existingProduct = new Product();
        existingProduct.setId(id);
        existingProduct.setName("Old Product");
        existingProduct.setPrice(100.0);
        existingProduct.setStock(2);

        Product updatedProduct = new Product();
        updatedProduct.setId(id);
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(200.0);
        updatedProduct.setStock(5);

        ProductResponseDto responseDto = ProductResponseDto.builder()
                .id(id)
                .name("Updated Product")
                .price(200.0)
                .stock(5)
                .build();

        when(productRepository.findById(id))
                .thenReturn(Optional.of(existingProduct));

        when(productRepository.save(existingProduct))
                .thenReturn(updatedProduct);

        when(productMapper.toDto(updatedProduct))
                .thenReturn(responseDto);

        ProductResponseDto result = productService.updateProduct(id, requestDto);

        assertEquals("Updated Product", result.getName());
        assertEquals(200.0, result.getPrice());
    }

    @Test
    void updateProduct_ProductNotFound_Exception() {

        Long id = 1L;

        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("Updated Product");
        requestDto.setPrice(200.0);
        requestDto.setStock(5);

        when(productRepository.findById(id))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productService.updateProduct(id, requestDto)
        );

        assertTrue(exception.getMessage().contains("Product not found"));
    }



}
