package com.example.ecom.controller;

import com.example.ecom.dto.requestDto.OrderRequestDto;
import com.example.ecom.dto.responseDto.OrderResponseDto;
import com.example.ecom.service.OrderService;
import com.example.ecom.util.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * REST controller for managing orders.
 * <p>
 * Provides endpoints to create, retrieve, and update order status.
 */
@Tag(name = "Order Management")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;


    @Operation(
            summary = "Create a new order",
            description = "Creates an order with payment details and returns the created order information"
    )
    /**
     * Creates a new order.
     *
     * @param orderRequestDto order request details
     * @return created order with HTTP 201 status
     */

    @PostMapping("/order")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderRequestDto orderRequestDto) {
        return new ResponseEntity<>(
                orderService.createOrder(orderRequestDto),
                HttpStatus.CREATED);
    }

    /**
     * Retrieves all orders.
     *
     * @return list of orders
     */
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderResponseDto>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return ResponseEntity.ok(orderService.getAllOrders(page, size));
    }

    /**
     * Retrieves an order by ID.
     */
    @GetMapping("/order/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
    /**
     * Retrieves orders filtered by status.
     *
     * @param status order status
     * @return list of orders with given status
     */
    @GetMapping("/order/status/{status}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersBystatus(
            @PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    /**
     * Updates the status of an order.
     *
     * @param id     order ID
     * @param status new order status
     * @return updated order response
     */
    @PatchMapping("/order/{id}/status")
    public ResponseEntity<OrderResponseDto> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

}



