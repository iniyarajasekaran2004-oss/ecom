package com.example.ecom.dto.responseDto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO used to send order item details in API responses.
 * Contains product information, quantity, and price.
 */
@Data
@Builder
public class OrderItemResponseDto {
    private Long productId;
    private String productName;
    private int quantity;
    private Double price;
}
