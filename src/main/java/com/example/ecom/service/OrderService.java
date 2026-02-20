package com.example.ecom.service;

import com.example.ecom.dto.requestDto.OrderRequestDto;
import com.example.ecom.dto.responseDto.OrderResponseDto;
import com.example.ecom.util.OrderStatus;
import org.springframework.data.domain.Page;
import java.util.List;

/**
 * Service interface for managing Order operations.
 * <p>
 * Defines business operations related to order creation,
 * retrieval, filtering, and status updates.
 */
public interface OrderService {
    /**
     * Creates a new order.
     *
     * @param orderRequestDto order request payload
     * @return created order response DTO
     */
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);

    /**
     * Retrieves an order by its ID.
     *
     * @param id order ID
     * @return order response DTO
     */
    OrderResponseDto getOrderById(Long id);

    /**
     * Retrieves all orders.
     *
     * @return list of order response DTOs
     */

    Page<OrderResponseDto> getAllOrders(int page, int size);
    /**
     * Retrieves orders filtered by status.
     *
     * @param status order status
     * @return list of matching orders
     */
    List<OrderResponseDto> getOrdersByStatus(OrderStatus status);

    /**
     * Updates the status of an order.
     *
     * @param id     order ID
     * @param status new order status
     * @return updated order response DTO
     */

    OrderResponseDto updateOrderStatus(Long id, OrderStatus status);
}
