package com.example.ecom.service.impl;

import com.example.ecom.dto.requestDto.CustomerRequestDto;
import com.example.ecom.dto.responseDto.CustomerResponseDto;
import com.example.ecom.entity.Customer;
import com.example.ecom.exception.DuplicateResourceException;
import com.example.ecom.exception.ResourceNotFoundException;
import com.example.ecom.mapper.CustomerMapper;
import com.example.ecom.repository.CustomerRepository;
import com.example.ecom.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


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
    private final CustomerMapper customerMapper;

    /**
     * Creates a new customer after validating duplicate email.
     *
     * @param customerRequestDto request DTO containing customer details
     * @return created customer response DTO
     * @throws IllegalArgumentException if email already exists
     */
    @Override
    public CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto) {
        log.info("Creating customer with email:{}", customerRequestDto.getEmail());
        if (customerRepository.existsByEmail(customerRequestDto.getEmail())) {
            log.error("Customer already exists with email: {}", customerRequestDto.getEmail());
            throw new DuplicateResourceException("Customer already exists with email: " + customerRequestDto.getEmail());
        }

        Customer customer = customerMapper.toEntity(customerRequestDto);
        Customer saved = customerRepository.save(customer);
        log.info("Customer created with id: {}", saved.getId());

        return customerMapper.toResponse(saved);
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
                .orElseThrow(() -> {
                    log.error("Customer not found with id: {}", id);
                    return new ResourceNotFoundException("Customer not found with id:" + id);
                });
        return customerMapper.toResponse(customer);
    }

    /**
     * Retrieves all customers from the database.
     *
     * @return list of customer response DTOs
     */
    @Override
    public Page<CustomerResponseDto> getAllCustomers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Customer> customerPage = customerRepository.findAll(pageable);
        log.debug("Fetching customers page: {}, size: {}", page, size);

        return customerPage.map(customerMapper::toResponse);
    }


    /**
     * Updates an existing customer.
     *
     * @param id                 customer ID
     * @param customerRequestDto updated customer details
     * @return updated customer response DTO
     * @throws ResourceNotFoundException if customer not found
     */

    @Override
    public CustomerResponseDto updateCustomer(Long id, CustomerRequestDto customerRequestDto) {
        log.info("Updating customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Customer not found for update with id: {}", id);
                    return new ResourceNotFoundException("Customer not found with id:" + id);
                });
        customerMapper.updateCustomerFromDto(customerRequestDto, customer);
        Customer updated = customerRepository.save(customer);
        log.info("Customer update successfully with id: {}", id);
        return customerMapper.toResponse(updated);
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
            log.error("Customer not found for deletion with id: {}", id);
            throw new ResourceNotFoundException("Customer not found with id:" + id);
        }
        customerRepository.deleteById(id);
        log.info("Customer deleted successfully with id:{}", id);
    }


//    private CustomerResponseDto mapToResponse(Customer customer) {
//        return CustomerResponseDto.builder()
//                .id(customer.getId())
//                .name(customer.getName())
//                .email(customer.getEmail())
//                .build();
//
//    }
}
