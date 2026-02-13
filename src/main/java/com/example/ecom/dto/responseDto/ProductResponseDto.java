package com.example.ecom.dto.responseDto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO used to send product details in API responses.
 * Contains product ID, name, stock, and price information.
 */
@Data
@Builder
public class ProductResponseDto {
    private Long id;
    private String name;
    private int stock;
    private Double price;

}
