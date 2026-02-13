package com.example.ecom.dto.requestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for customer request data.
 */
@Data
public class CustomerRequestDto {
    /**
     * Customer name.
     */
    @NotBlank(message = "Customer name should not be blank")
    private String name;
    /**
     * Customer email.
     */
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email should not be blank")
    private String email;
}
