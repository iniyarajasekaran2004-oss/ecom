package com.example.ecom.mapper;

import com.example.ecom.dto.responseDto.PaymentResponseDto;
import com.example.ecom.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(source = "order.id", target = "orderId")
    PaymentResponseDto toResponse(Payment payment);
}
