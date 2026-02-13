package com.example.ecom.service.impl;

import com.example.ecom.dto.responseDto.OrderItemResponseDto;
import com.example.ecom.dto.requestDto.OrderRequestDto;
import com.example.ecom.dto.responseDto.OrderResponseDto;
import com.example.ecom.entity.Customer;
import com.example.ecom.entity.Order;
import com.example.ecom.entity.OrderItem;
import com.example.ecom.entity.Product;
import com.example.ecom.exception.ResourceNotFoundException;
import com.example.ecom.repository.CustomerRepository;
import com.example.ecom.repository.OrderRepository;
import com.example.ecom.repository.ProductRepository;
import com.example.ecom.service.OrderService;
import com.example.ecom.util.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.ecom.util.OrderStatus.*;

/**
 * Service implementation for handling Order-related operations.
 * <p>
 * Contains business logic for order creation, retrieval,
 * status updates, and stock management.
 * <p>
 * Ensures valid order status transitions and maintains
 * product stock consistency using transactional support.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    /**
     * Creates a new order for a customer.
     * <p>
     * Validates customer existence, product availability,
     * reduces product stock, and persists the order.
     *
     * @param orderRequestDto order request payload
     * @return created order response DTO
     * @throws ResourceNotFoundException if customer or product not found
     * @throws RuntimeException          if insufficient stock
     */
    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        log.info("Creating Order for customer id: {}", orderRequestDto.getCustomerId());
        Customer customer = customerRepository.findById(orderRequestDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: "
                        + orderRequestDto.getCustomerId()));
        Order order = Order.builder()
                .customer(customer)
                .status(OrderStatus.CREATED)
                .build();
        List<OrderItem> orderItems = orderRequestDto.getItems()
                .stream()
                .map(itemDto -> {
                    Product product = productRepository.findById(itemDto.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not with id: " + itemDto.getProductId()));

                    if (product.getStock() < itemDto.getQuantity()) {
                        throw new RuntimeException("Insufficient stock for product: " + product.getName());
                    }

                    //reduce stock
                    product.setStock(product.getStock() - itemDto.getQuantity());
                    return OrderItem.builder()
                            .order(order)
                            .product(product)
                            .quantity(itemDto.getQuantity())
                            .build();
                })
                .collect(Collectors.toList());
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with id: {}", savedOrder.getId());
        return mapToResponse(savedOrder);

    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id order ID
     * @return order response DTO
     * @throws ResourceNotFoundException if order not found
     */

    @Override
    public OrderResponseDto getOrderById(Long id) {
        log.info("Fetching order with id: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return mapToResponse(order);
    }

    /**
     * Retrieves all orders from the database.
     *
     * @return list of order response DTOs
     */
    @Override
    public List<OrderResponseDto> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves orders filtered by status.
     *
     * @param status order status
     * @return list of matching orders
     */
    @Override
    public List<OrderResponseDto> getOrdersByStatus(OrderStatus status) {
        log.info("Fetching orders with status: {}", status);
        return orderRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Updates the status of an existing order.
     * <p>
     * Validates allowed status transitions before updating.
     *
     * @param id     order ID
     * @param status new order status
     * @return updated order response DTO
     * @throws ResourceNotFoundException if order not found
     * @throws IllegalStateException     if transition is invalid
     */
    @Override
    public OrderResponseDto updateOrderStatus(Long id, OrderStatus status) {
        log.info("updating order status.Order id: {}, New status: {}", id, status);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        OrderStatus currentStatus = order.getStatus();
        if (!isValidTransition(currentStatus, status)) {
            throw new IllegalStateException("Invalid status Transition from" + currentStatus + " to " + status);
        }
        order.setStatus(status);
        Order updated = orderRepository.save(order);
        return mapToResponse(updated);
    }

    /**
     * Validates allowed order status transitions.
     *
     * @param currentStatus current order status
     * @param status        requested new status
     * @return true if transition is valid, otherwise false
     */
    private boolean isValidTransition(OrderStatus currentStatus, OrderStatus status) {
        return switch (currentStatus) {
            case CREATED -> status == PAID;
            case PAID -> status == SHIPPED;
            case SHIPPED -> status == DELIVERED;
            case DELIVERED -> false;
        };
    }

    /**
     * Converts Order entity to OrderResponseDto.
     *
     * @param order order entity
     * @return mapped response DTO
     */

    private OrderResponseDto mapToResponse(Order order) {
        List<OrderItemResponseDto> items = order.getItems().stream()
                .map(item -> OrderItemResponseDto.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .price(item.getQuantity() * item.getProduct().getPrice())
                        .build())
                .collect(Collectors.toList());
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .customerId(order.getCustomer().getId())
                .status(order.getStatus())
                .items(items)
                .build();

    }

}
