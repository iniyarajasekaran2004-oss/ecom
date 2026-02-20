package com.example.ecom.dto.requestDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * DTO used to receive order item details from client requests.
 * Contains product ID and quantity information.
 */
@Data
public class OrderItemRequestDto {
    /**
     * ID of the product to be added to the order.
     * Must not be null.
     */
    @NotNull(message = "Product id is required")
    private Long productId;
    /**
     * Quantity of the selected product.
     * Must be at least 1.
     */
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

//    List<OrderItemRequestDto> items;

}
