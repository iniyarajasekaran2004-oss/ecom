package com.example.ecom.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * DTO used to receive product details from client requests.
 * Contains product name, stock quantity, and price information.
 */
@Data
public class ProductRequestDto {
    /**
     * Name of the product.
     * Must not be blank.
     */
    @NotBlank(message = "Product name must not be blank")
    private String name;
    /**
     * Available stock quantity of the product.
     * Must be greater than 0.
     */
    @NotNull(message = "Stock is required")
    @Positive(message = "Stock must be greater than 0")
    private Integer stock;
    /**
     * Price of the product.
     * Must be greater than 0.
     */

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;
}

