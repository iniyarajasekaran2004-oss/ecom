package com.example.ecom.controller;

import com.example.ecom.dto.requestDto.CustomerRequestDto;
import com.example.ecom.dto.responseDto.CustomerResponseDto;
import com.example.ecom.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing customers.
 * Provides endpoints to create and manage customer details.
 */
@Tag(name = "Customer Management")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    /**
     * Creates a new customer.
     *
     * @param customerRequestDto customer details from request body
     * @return created customer response with HTTP 201 status
     */

    @PostMapping("/customer")
    public ResponseEntity<CustomerResponseDto> createCustomer(
            @Valid @RequestBody CustomerRequestDto customerRequestDto) {

        CustomerResponseDto response = customerService.createCustomer(customerRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/customer/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PutMapping("/customer/{id}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequestDto customerRequestDto) {

        return ResponseEntity.ok(
                customerService.updateCustomer(id, customerRequestDto));
    }

    @DeleteMapping("/customer/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
