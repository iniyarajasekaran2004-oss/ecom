package com.example.ecom.service;

import com.example.ecom.dto.requestDto.PaymentRequestDto;
import com.example.ecom.dto.responseDto.PaymentResponseDto;
import com.example.ecom.entity.Order;
import com.example.ecom.entity.OrderItem;
import com.example.ecom.entity.Payment;
import com.example.ecom.exception.ResourceNotFoundException;
import com.example.ecom.mapper.PaymentMapper;
import com.example.ecom.repository.OrderRepository;
import com.example.ecom.repository.PaymentRepository;
import com.example.ecom.service.impl.PaymentServiceImpl;
import com.example.ecom.util.OrderStatus;
import com.example.ecom.util.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order order;
    private Payment payment;
    private PaymentRequestDto requestDto;
    private PaymentResponseDto responseDto;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.CREATED);

        OrderItem item = new OrderItem();
        item.setQuantity(2);
        item.setPrice(100.0);
        order.setItems(List.of(item));

        payment = new Payment();
        payment.setId(10L);
        payment.setOrder(order);
        payment.setAmount(200.0);

        requestDto = new PaymentRequestDto();
        requestDto.setOrderId(1L);
        requestDto.setPaymentMethod(PaymentMethod.UPI);

        responseDto = PaymentResponseDto.builder()
                .id(10L)
                .orderId(1L)
                .amount(200.0)
                .build();
    }

    @Test
    void makePayment_ShouldReturnResponse_WhenValid() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(paymentMapper.toResponse(payment)).thenReturn(responseDto);

        PaymentResponseDto result = paymentService.makePayment(requestDto);

        assertNotNull(result);
        assertEquals(200.0, result.getAmount());
        assertEquals(OrderStatus.PAID, order.getStatus());

        verify(paymentRepository).save(any(Payment.class));
    }


    @Test
    void makePayment_ShouldThrowException_WhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.makePayment(requestDto));
    }

    @Test
    void makePayment_ShouldThrowException_WhenOrderNotInCreatedState() {
        order.setStatus(OrderStatus.PAID);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class,
                () -> paymentService.makePayment(requestDto));
    }

    @Test
    void makePayment_ShouldThrowException_WhenPaymentAlreadyExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentRepository.findByOrderId(1L))
                .thenReturn(Optional.of(payment));

        assertThrows(RuntimeException.class,
                () -> paymentService.makePayment(requestDto));
    }


    @Test
    void getPaymentById_ShouldReturnPayment() {
        when(paymentRepository.findById(10L))
                .thenReturn(Optional.of(payment));
        when(paymentMapper.toResponse(payment))
                .thenReturn(responseDto);

        PaymentResponseDto result = paymentService.getPaymentById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
    }


    @Test
    void getPaymentById_ShouldThrowException_WhenNotFound() {
        when(paymentRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.getPaymentById(10L));
    }


    @Test
    void getAllPayments_ShouldReturnPage() {
        Page<Payment> paymentPage =
                new PageImpl<>(List.of(payment));

        when(paymentRepository.findAll(any(Pageable.class)))
                .thenReturn(paymentPage);
        when(paymentMapper.toResponse(payment))
                .thenReturn(responseDto);

        Page<PaymentResponseDto> result =
                paymentService.getAllPayments(0, 10);

        assertEquals(1, result.getTotalElements());
    }
}
