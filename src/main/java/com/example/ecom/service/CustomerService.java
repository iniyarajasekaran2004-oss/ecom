package com.example.ecom.service;

import com.example.ecom.dto.requestDto.CustomerRequestDto;
import com.example.ecom.dto.responseDto.CustomerResponseDto;


import java.util.List;

/**
 * Service interface for managing Customer operations.
 * <p>
 * Defines business operations related to customer
 * creation, retrieval, update, and deletion.
 */
public interface CustomerService {
    /**
     * Creates a new customer.
     *
     * @param customerResquestDto request DTO containing customer details
     * @return created customer response DTO
     */
    CustomerResponseDto createCustomer(CustomerRequestDto customerResquestDto);

    /**
     * Retrieves a customer by ID.
     *
     * @param id customer ID
     * @return customer response DTO
     */
    CustomerResponseDto getCustomerById(Long id);

    /**
     * Retrieves all customers.
     *
     * @return list of customer response DTOs
     */
    List<CustomerResponseDto> getAllCustomers();

    /**
     * Updates an existing customer.
     *
     * @param id                  customer ID
     * @param customerResquestDto updated customer details
     * @return updated customer response DTO
     */
    CustomerResponseDto updateCustomer(Long id, CustomerRequestDto customerResquestDto);

    /**
     * Deletes a customer by ID.
     *
     * @param id customer ID
     */
    void deleteCustomer(Long id);


}
