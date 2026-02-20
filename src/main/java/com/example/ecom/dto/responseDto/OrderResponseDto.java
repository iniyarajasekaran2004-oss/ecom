package com.example.ecom.dto.responseDto;

import com.example.ecom.util.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * DTO used to send order details in API responses.
 * Contains order information, customer ID, status, and ordered items.
 */
@Data
@Builder
public class OrderResponseDto {
    private Long orderId;
    private Long customerId;
    private OrderStatus status;
    private List<OrderItemResponseDto> items;
    private Double totalAmount;


}
