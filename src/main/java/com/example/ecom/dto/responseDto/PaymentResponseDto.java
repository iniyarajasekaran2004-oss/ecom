package com.example.ecom.dto.responseDto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO used to send payment details in API responses.
 * Contains payment ID, order reference, amount, and payment date.
 */
@Data
@Builder
public class PaymentResponseDto {
    private Long id;
    private Long orderId;
    private Double amount;
    private LocalDateTime paymentDate;
}
