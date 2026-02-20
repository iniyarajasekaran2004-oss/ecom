package com.example.ecom.dto.requestDto;

import com.example.ecom.util.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO used to receive payment request details.
 * Contains the order ID for which payment is being made.
 */
@Data
public class PaymentRequestDto {
    /**
     * ID of the order to process payment for.
     * Must not be null.
     */
    @NotNull(message = "Order id is required")
    private Long orderId;

    @NotNull(message = "Payment method should not be null")
    private PaymentMethod paymentMethod;

}
