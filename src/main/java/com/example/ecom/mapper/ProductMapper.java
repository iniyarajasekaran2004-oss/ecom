package com.example.ecom.mapper;

import com.example.ecom.dto.requestDto.ProductRequestDto;
import com.example.ecom.dto.responseDto.ProductResponseDto;
import com.example.ecom.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDto toDto(Product product);


    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductRequestDto dto);
}
