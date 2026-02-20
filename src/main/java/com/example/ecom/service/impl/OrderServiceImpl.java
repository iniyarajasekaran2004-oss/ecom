package com.example.ecom.service.impl;

import com.example.ecom.dto.responseDto.OrderItemResponseDto;
import com.example.ecom.dto.requestDto.OrderRequestDto;
import com.example.ecom.dto.responseDto.OrderResponseDto;
import com.example.ecom.entity.Customer;
import com.example.ecom.entity.Order;
import com.example.ecom.entity.OrderItem;
import com.example.ecom.entity.Product;
import com.example.ecom.exception.InsufficientStockException;
import com.example.ecom.exception.InvalidOrderStatusTransitionException;
import com.example.ecom.exception.ResourceNotFoundException;
import com.example.ecom.mapper.OrderItemMapper;
import com.example.ecom.mapper.OrderMapper;
import com.example.ecom.repository.CustomerRepository;
import com.example.ecom.repository.OrderRepository;
import com.example.ecom.repository.ProductRepository;
import com.example.ecom.service.OrderService;
import com.example.ecom.util.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;




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

        Order order = orderMapper.toEntity(orderRequestDto);

        Customer customer = customerRepository.findById(orderRequestDto.getCustomerId())
                .orElseThrow(() ->{
                    log.error("Customer not found with id: {}", orderRequestDto.getCustomerId());
                    return new ResourceNotFoundException("Customer not found with id: "
                        + orderRequestDto.getCustomerId());
                });
//        Order order = Order.builder()
//                .customer(customer)
//                .status(OrderStatus.CREATED)
//                .build();

        order.setCustomer(customer);
        order.setStatus(CREATED);

        List<OrderItem> orderItems = orderRequestDto.getItems()
                .stream()
                .map(itemDto -> {
                    Product product = productRepository.findById(itemDto.getProductId())
                            .orElseThrow(() ->{
                                log.error("Product not found with id: {}", itemDto.getProductId());
                                return new ResourceNotFoundException("Product not with id: " + itemDto.getProductId());
                            });

                    if (product.getStock() < itemDto.getQuantity()) {
                        log.error("Insufficient stock for product: {}, Available: {}, Requested: {}",
                                product.getName(), product.getStock(), itemDto.getQuantity());
                        throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
                    }

                    //reduce stock
                    log.debug("Reducing stock for product: {}. Old stock: {}, Quantity ordered: {}",
                            product.getName(), product.getStock(), itemDto.getQuantity());
                    product.setStock(product.getStock() - itemDto.getQuantity());

                    OrderItem orderItem = orderItemMapper.toEntity(itemDto);

                    orderItem.setOrder(order);
                    orderItem.setProduct(product);
                    orderItem.setPrice(product.getPrice());

                    return orderItem;
                })
//                    return OrderItem.builder()
//                            .order(order)
//                            .product(product)
//                            .quantity(itemDto.getQuantity())
//                            .price(product.getPrice())
//                            .build();
//                })
                .collect(Collectors.toList());
        order.setItems(orderItems);
        double totalAmount = orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with id: {}", savedOrder.getId());
        return orderMapper.toDto(savedOrder);

    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id order ID
     * @return order response DTO
     * @throws ResourceNotFoundException if order not found
     */

    @Override
    @Transactional
    public OrderResponseDto getOrderById(Long id) {
        log.info("Fetching order with id: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {log.error("Order not found with id: {}", id);
                   return new ResourceNotFoundException("Order not found with id: " + id);
                });
        return orderMapper.toDto(order);
    }

    /**
     * Retrieves all orders from the database.
     *
     * @return list of order response DTOs
     */
    @Override
    public Page<OrderResponseDto> getAllOrders(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        log.debug("Fetching orders with page: {}, size: {}", page, size);
        Page<Order> orderPage = orderRepository.findAll(pageable);

        return orderPage.map(orderMapper::toDto);
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
                .map(orderMapper::toDto)
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
            log.error("Invalid status transition from {} to {} for order id: {}",
                    currentStatus, status, id);
            throw new InvalidOrderStatusTransitionException( currentStatus,status);
        }
        log.debug("Order status changed from {} to {} for order id: {}",
                currentStatus, status, id);
        order.setStatus(status);
        Order updated = orderRepository.save(order);
        return orderMapper.toDto(updated);
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

//    private OrderResponseDto mapToResponse(Order order) {
//        List<OrderItemResponseDto> items = order.getItems().stream()
//                .map(item -> OrderItemResponseDto.builder()
//                        .productId(item.getProduct().getId())
//                        .productName(item.getProduct().getName())
//                        .quantity(item.getQuantity())
//                        .price(item.getQuantity() * item.getPrice())
//                        .build())
//                .collect(Collectors.toList());
//        return OrderResponseDto.builder()
//                .orderId(order.getId())
//                .customerId(order.getCustomer().getId())
//                .status(order.getStatus())
//                .items(items)
//                .totalAmount(order.getTotalAmount())
//                .build();
//
//    }

}
