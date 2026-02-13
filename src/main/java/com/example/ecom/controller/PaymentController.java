package com.example.ecom.controller;

import com.example.ecom.dto.requestDto.PaymentRequestDto;
import com.example.ecom.dto.responseDto.PaymentResponseDto;
import com.example.ecom.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing payments.
 * <p>
 * Provides endpoints to process and retrieve payment details.
 */
@Tag(name = "Payment Management")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    /**
     * Processes a payment for an order.
     *
     * @param paymentRequestDto payment request details
     * @return created payment response with HTTP 201 status
     */

    @PostMapping("/Payment")
    public ResponseEntity<?> makePayment(
            @Valid @RequestBody PaymentRequestDto paymentRequestDto) {
        return new ResponseEntity<>(
                paymentService.makePayment(paymentRequestDto),
                HttpStatus.CREATED);
    }

    /**
     * Retrieves payment details by ID.
     *
     * @param id payment ID
     * @return payment response
     */
    @GetMapping("/Payment/{id}")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }


}
