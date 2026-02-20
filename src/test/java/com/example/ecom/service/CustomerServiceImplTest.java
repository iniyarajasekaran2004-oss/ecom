package com.example.ecom.service;

import com.example.ecom.dto.requestDto.CustomerRequestDto;
import com.example.ecom.dto.responseDto.CustomerResponseDto;
import com.example.ecom.entity.Customer;
import com.example.ecom.exception.DuplicateResourceException;
import com.example.ecom.exception.ResourceNotFoundException;
import com.example.ecom.mapper.CustomerMapper;
import com.example.ecom.repository.CustomerRepository;
import com.example.ecom.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void createCustomer_Success() {

        CustomerRequestDto requestDto = new CustomerRequestDto();
        requestDto.setName("Ini");
        requestDto.setEmail("ini@gmail.com");

        Customer customer = new Customer();
        customer.setName("Ini");
        customer.setEmail("ini@gmail.com");

        Customer savedCustomer = new Customer();
        savedCustomer.setId(1L);
        savedCustomer.setName("Ini");
        savedCustomer.setEmail("ini@gmail.com");

        CustomerResponseDto responseDto = CustomerResponseDto.builder()
                .id(1L)
                .name("Ini")
                .email("ini@gmail.com")
                .build();


        when(customerRepository.existsByEmail("ini@gmail.com"))
                .thenReturn(false);

        when(customerMapper.toEntity(requestDto))
                .thenReturn(customer);

        when(customerRepository.save(customer))
                .thenReturn(savedCustomer);

        when(customerMapper.toResponse(any(Customer.class)))
                .thenReturn(responseDto);

        CustomerResponseDto result = customerService.createCustomer(requestDto);

        assertEquals("Ini", result.getName());
        assertEquals("ini@gmail.com", result.getEmail());
    }

    @Test
    void createCustomer_DuplicateEmail_Exception() {

        CustomerRequestDto requestDto = new CustomerRequestDto();
        requestDto.setName("Ini");
        requestDto.setEmail("ini@gmail.com");

        when(customerRepository.existsByEmail("ini@gmail.com"))
                .thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> customerService.createCustomer(requestDto)
        );

        assertTrue(exception.getMessage().contains("Customer already exists"));
    }

    @Test
    void getCustomerById_Success() {

        Long id = 1L;

        Customer customer = new Customer();
        customer.setId(id);
        customer.setName("Ini");
        customer.setEmail("ini@gmail.com");

        CustomerResponseDto responseDto = CustomerResponseDto.builder()
                .id(id)
                .name("Ini")
                .email("ini@gmail.com")
                .build();

        when(customerRepository.findById(id))
                .thenReturn(Optional.of(customer));

        when(customerMapper.toResponse(any(Customer.class)))
                .thenReturn(responseDto);

        CustomerResponseDto result = customerService.getCustomerById(id);

        assertEquals("Ini", result.getName());
        assertEquals("ini@gmail.com", result.getEmail());
    }

    @Test
    void getCustomerById_NotFound_Exception() {

        Long id = 1L;

        when(customerRepository.findById(id))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> customerService.getCustomerById(id)
        );

        assertTrue(exception.getMessage().contains("Customer not found"));
    }

    @Test
    void updateCustomer_Success() {

        Long id = 1L;

        CustomerRequestDto requestDto = new CustomerRequestDto();
        requestDto.setName("Updated Ini");
        requestDto.setEmail("updated@gmail.com");

        Customer existingCustomer = new Customer();
        existingCustomer.setId(id);
        existingCustomer.setName("Old Name");
        existingCustomer.setEmail("old@gmail.com");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(id);
        updatedCustomer.setName("Updated Ini");
        updatedCustomer.setEmail("updated@gmail.com");

        CustomerResponseDto responseDto = CustomerResponseDto.builder()
                .id(id)
                .name("Updated Ini")
                .email("updated@gmail.com")
                .build();

        when(customerRepository.findById(id))
                .thenReturn(Optional.of(existingCustomer));

        // mapper updates entity (void method)
        doNothing().when(customerMapper)
                .updateCustomerFromDto(requestDto, existingCustomer);

        when(customerRepository.save(existingCustomer))
                .thenReturn(updatedCustomer);

        when(customerMapper.toResponse(any(Customer.class)))
                .thenReturn(responseDto);

        CustomerResponseDto result =
                customerService.updateCustomer(id, requestDto);

        assertEquals("Updated Ini", result.getName());
        assertEquals("updated@gmail.com", result.getEmail());
    }


}
