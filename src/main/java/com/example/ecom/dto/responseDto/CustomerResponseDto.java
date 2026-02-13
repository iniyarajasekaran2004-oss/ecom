package com.example.ecom.dto.responseDto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO used to send customer details in API responses.
 * Contains customer ID, name, and email information.
 */
@Data
@Builder
public class CustomerResponseDto {
    private Long id;
    private String name;
    private String email;

}
