package com.example.ecom.mapper;

import com.example.ecom.dto.requestDto.CustomerRequestDto;
import com.example.ecom.dto.responseDto.CustomerResponseDto;
import com.example.ecom.entity.Customer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")

public interface CustomerMapper {

    Customer toEntity(CustomerRequestDto dto);


    CustomerResponseDto toResponse(Customer customer);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomerFromDto(CustomerRequestDto dto, @MappingTarget Customer customer);
}
