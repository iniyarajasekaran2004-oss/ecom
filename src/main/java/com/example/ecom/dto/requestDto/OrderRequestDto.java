package com.example.ecom.dto.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * DTO used to receive order details from client requests.
 * Contains customer ID and list of ordered items.
 */
@Data
@Schema(description = "Order request payload")
public class OrderRequestDto {
    /**
     * ID of the customer placing the order.
     * Must not be null.
     */
    @Schema(description = "Customer ID placing the order", example = "1")
    @NotNull(message = "Customer id is required")
    private Long customerId;

    /**
     * List of products included in the order.
     * Must contain at least one item.
     */
    @Size(min = 1, message = "Order must contain at least one product")
    private List<OrderItemRequestDto> items;
}
