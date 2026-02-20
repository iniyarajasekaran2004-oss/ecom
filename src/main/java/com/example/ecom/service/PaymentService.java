package com.example.ecom.service;

import com.example.ecom.dto.requestDto.PaymentRequestDto;
import com.example.ecom.dto.responseDto.PaymentResponseDto;
import org.springframework.data.domain.Page;

/**
 * Service interface for managing Payment operations.
 *
 * Defines business operations related to payment processing
 * and retrieval of payment details.
 */
public interface PaymentService {
    /**
     * Processes payment for an order.
     *
     * @param paymentRequestDto payment request payload
     * @return payment response DTO
     */
    PaymentResponseDto makePayment(PaymentRequestDto paymentRequestDto);
    /**
     * Retrieves payment details by ID.
     *
     * @param id payment ID
     * @return payment response DTO
     */

    PaymentResponseDto getPaymentById(Long id);
    Page<PaymentResponseDto> getAllPayments(int page, int size);
}
