package com.example.ecom.service;

import com.example.ecom.dto.requestDto.OrderItemRequestDto;
import com.example.ecom.dto.requestDto.OrderRequestDto;
import com.example.ecom.dto.responseDto.OrderResponseDto;
import com.example.ecom.entity.Customer;
import com.example.ecom.entity.Order;
import com.example.ecom.entity.OrderItem;
import com.example.ecom.exception.ResourceNotFoundException;
import com.example.ecom.mapper.OrderItemMapper;
import com.example.ecom.mapper.OrderMapper;
import com.example.ecom.repository.CustomerRepository;
import com.example.ecom.repository.OrderRepository;
import com.example.ecom.repository.ProductRepository;
import com.example.ecom.service.impl.OrderServiceImpl;
import com.example.ecom.util.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.ecom.dto.requestDto.OrderItemRequestDto;
import com.example.ecom.entity.Product;
import java.util.List;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)

public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;
    @Mock
    private OrderItemMapper orderItemMapper;

    @Test
    void createOrder_Success() {

        OrderRequestDto requestDto = new OrderRequestDto();
        //requestDto.setStatus(OrderStatus.CREATED);
        requestDto.setCustomerId(1L);
        OrderItemRequestDto itemDto = new OrderItemRequestDto();
        itemDto.setProductId(1L);
        itemDto.setQuantity(2);

        requestDto.setItems(List.of(itemDto));

        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setStatus(OrderStatus.CREATED);

        OrderResponseDto responseDto = OrderResponseDto.builder()
                .orderId(1L)
                .status(OrderStatus.CREATED)
                .build();
        Customer customer = new Customer();
        customer.setId(1L);
        Product product = new Product();
        product.setId(1L);
        product.setStock(10);
        product.setPrice(100.0);

        when(customerRepository.findById(requestDto.getCustomerId()))
                .thenReturn(Optional.of(customer));

        when(orderMapper.toEntity(requestDto)).thenReturn(order);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderItemMapper.toEntity(any()))
                .thenReturn(new OrderItem());
        when(orderRepository.save(order)).thenReturn(savedOrder);
        when(orderMapper.toDto(any(Order.class))).thenReturn(responseDto);

        OrderResponseDto result = orderService.createOrder(requestDto);

        assertEquals(OrderStatus.CREATED, result.getStatus());
    }


    @Test
    void getOrderById_Success() {

        Long id = 1L;

        Order order = new Order();
        order.setId(id);
        order.setStatus(OrderStatus.PAID);

        OrderResponseDto responseDto = OrderResponseDto.builder()
                .orderId(id)
                        .status(OrderStatus.PAID)
                                .build();


        when(orderRepository.findById(id))
                .thenReturn(Optional.of(order));

        when(orderMapper.toDto(any(Order.class)))
                .thenReturn(responseDto);

        OrderResponseDto result = orderService.getOrderById(id);

        assertEquals(OrderStatus.PAID, result.getStatus());
    }
    @Test
    void getOrderById_NotFound_Exception() {

        Long id = 1L;

        when(orderRepository.findById(id))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.getOrderById(id)
        );

        assertTrue(exception.getMessage().contains("Order not found"));
    }

    @Test
    void updateOrder_Success() {

        Long id = 1L;

        OrderRequestDto requestDto = new OrderRequestDto();


        Order existingOrder = new Order();
        existingOrder.setId(id);
        existingOrder.setStatus(OrderStatus.CREATED);

        Order updatedOrder = new Order();
        updatedOrder.setId(id);
        updatedOrder.setStatus(OrderStatus.PAID);

        OrderResponseDto responseDto = OrderResponseDto.builder()
                .orderId(id)
                .status(OrderStatus.PAID)
                .build();


        when(orderRepository.findById(id))
                .thenReturn(Optional.of(existingOrder));

        when(orderRepository.save(existingOrder))
                .thenReturn(updatedOrder);

        when(orderMapper.toDto(any(Order.class)))
                .thenReturn(responseDto);

        OrderResponseDto result =
                orderService.updateOrderStatus(id,OrderStatus.PAID);

        assertEquals(OrderStatus.PAID, result.getStatus());
    }
    @Test
    void updateOrder_NotFound_Exception() {

        Long id = 1L;

        OrderRequestDto requestDto = new OrderRequestDto();


        when(orderRepository.findById(id))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.updateOrderStatus(id, OrderStatus.SHIPPED)
        );

        assertTrue(exception.getMessage().contains("Order not found"));
    }


}
