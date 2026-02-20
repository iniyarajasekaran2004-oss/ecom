package com.example.ecom.mapper;

import com.example.ecom.dto.requestDto.OrderItemRequestDto;
import com.example.ecom.dto.requestDto.OrderRequestDto;
import com.example.ecom.dto.responseDto.OrderResponseDto;
import com.example.ecom.entity.Order;
import com.example.ecom.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring",uses= OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source="id",target="orderId")
    @Mapping(source="customer.id",target="customerId")
    OrderResponseDto toDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    Order toEntity(OrderRequestDto dto);
}
