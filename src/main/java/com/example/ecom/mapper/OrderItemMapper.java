package com.example.ecom.mapper;

import com.example.ecom.dto.requestDto.OrderItemRequestDto;
import com.example.ecom.dto.responseDto.OrderItemResponseDto;
import com.example.ecom.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(source = "product.id",target = "productId")
    @Mapping(source = "product.name",target = "productName")
    @Mapping(expression = "java(orderItem.getQuantity() * orderItem.getPrice())", target = "price")
    OrderItemResponseDto toDto(OrderItem orderItem);

    OrderItem toEntity(OrderItemRequestDto dto);

}

