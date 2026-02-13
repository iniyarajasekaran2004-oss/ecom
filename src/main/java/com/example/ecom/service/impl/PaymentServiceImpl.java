package com.example.ecom.service.impl;

import com.example.ecom.dto.requestDto.PaymentRequestDto;
import com.example.ecom.dto.responseDto.PaymentResponseDto;
import com.example.ecom.entity.Order;
import com.example.ecom.entity.Payment;
import com.example.ecom.exception.ResourceNotFoundException;
import com.example.ecom.repository.OrderRepository;
import com.example.ecom.repository.PaymentRepository;
import com.example.ecom.service.PaymentService;
import com.example.ecom.util.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service implementation for handling Payment operations.
 * <p>
 * Contains business logic for processing payments,
 * preventing duplicate payments, calculating total amount,
 * and updating order status after successful payment.
 * <p>
 * Uses transactional support to ensure data consistency.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    /**
     * Processes payment for an order.
     * <p>
     * Validates order existence, ensures order is in CREATED state,
     * prevents duplicate payments, calculates total amount,
     * saves payment, and updates order status to PAID.
     *
     * @param paymentRequestDto payment request payload
     * @return payment response DTO
     * @throws ResourceNotFoundException if order not found
     * @throws RuntimeException          if invalid order status or payment already exists
     */
    @Override
    @Transactional
    public PaymentResponseDto makePayment(PaymentRequestDto paymentRequestDto) {
        log.info("Processing payment for order id: {}", paymentRequestDto.getOrderId());
        Order order = orderRepository.findById(paymentRequestDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + paymentRequestDto.getOrderId()));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new RuntimeException("Payment cannot be processed. Order status is: " + order.getStatus());
        }

        //prevent double payment
        paymentRepository.findByOrderId(order.getId())
                .ifPresent(p -> {
                    throw new RuntimeException("Payment aldready exists for this order");
                });
        // calculate total amount
        double totalAmount = order.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();

        Payment payment = Payment.builder()
                .order(order)
                .amount(totalAmount)
                .paymentDate(LocalDateTime.now())
                .build();
        Payment savedPayment = paymentRepository.save(payment);

        //Update order status to PAID
        order.setStatus(OrderStatus.PAID);
        log.info("Payment successful. Order id: {},Amount: {}", order.getId(), totalAmount);
        return PaymentResponseDto.builder()
                .id(savedPayment.getId())
                .orderId(savedPayment.getOrder().getId())
                .amount(savedPayment.getAmount())
                .paymentDate(savedPayment.getPaymentDate())
                .build();
    }

    /**
     * Retrieves payment details by payment ID.
     *
     * @param id payment ID
     * @return payment response DTO
     * @throws ResourceNotFoundException if payment not found
     */
    @Override
    public PaymentResponseDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        return PaymentResponseDto.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}
