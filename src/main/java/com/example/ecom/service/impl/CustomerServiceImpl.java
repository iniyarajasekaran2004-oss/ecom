package com.example.ecom.service.impl;

import com.example.ecom.dto.requestDto.CustomerRequestDto;
import com.example.ecom.dto.responseDto.CustomerResponseDto;
import com.example.ecom.entity.Customer;
import com.example.ecom.exception.ResourceNotFoundException;
import com.example.ecom.repository.CustomerRepository;
import com.example.ecom.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing Customer operations.
 * <p>
 * Provides business logic for creating, retrieving, updating,
 * and deleting customers. Handles duplicate email validation
 * and converts Entity objects to Response DTOs.
 * <p>
 * Uses CustomerRepository for database interactions.
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    /**
     * Creates a new customer after validating duplicate email.
     *
     * @param customerResquestDto request DTO containing customer details
     * @return created customer response DTO
     * @throws IllegalArgumentException if email already exists
     */
    @Override
    public CustomerResponseDto createCustomer(CustomerRequestDto customerResquestDto) {
        log.info("Creating customer with email:{}", customerResquestDto.getEmail());
        // to check the duplicate id
        if (customerRepository.existsByEmail(customerResquestDto.getEmail())) {
            throw new IllegalArgumentException("Customer already exists with email: " + customerResquestDto.getEmail());
        }
        Customer customer = Customer.builder()
                .name(customerResquestDto.getName())
                .email(customerResquestDto.getEmail())
                .build();
        Customer saved = customerRepository.save(customer);
        log.info("Customer created with id: {}", saved.getId());
        return mapToResponse(saved);
    }

    /**
     * Retrieves a customer by ID.
     *
     * @param id customer ID
     * @return customer response DTO
     * @throws RuntimeException if customer not found
     */

    @Override
    public CustomerResponseDto getCustomerById(Long id) {
        log.info("Fetching customer with id : {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id:" + id));
        return mapToResponse(customer);
    }

    /**
     * Retrieves all customers from the database.
     *
     * @return list of customer response DTOs
     */
    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        log.info("Fetching all customers");
        return customerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing customer.
     *
     * @param id                  customer ID
     * @param customerResquestDto updated customer details
     * @return updated customer response DTO
     * @throws ResourceNotFoundException if customer not found
     */

    @Override
    public CustomerResponseDto updateCustomer(Long id, CustomerRequestDto customerResquestDto) {
        log.info("Updating customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id:" + id));
        customer.setName(customerResquestDto.getName());
        customer.setEmail(customerResquestDto.getEmail());
        Customer updated = customerRepository.save(customer);
        log.info("Customer update successfully with id: {}", id);
        return mapToResponse(updated);
    }

    /**
     * Deletes a customer by ID.
     *
     * @param id customer ID
     * @throws ResourceNotFoundException if customer not found
     */
    @Override
    public void deleteCustomer(Long id) {
        log.info("Deleting customer with id: {}", id);
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id:" + id);
        }
        customerRepository.deleteById(id);
        log.info("Customer deleted successfully with id:{}", id);
    }

    /**
     * Converts Customer entity to CustomerResponseDto.
     *
     * @param customer customer entity
     * @return mapped response DTO
     */
    private CustomerResponseDto mapToResponse(Customer customer) {
        return CustomerResponseDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .build();

    }
}
