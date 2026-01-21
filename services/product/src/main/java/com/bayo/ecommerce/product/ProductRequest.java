package com.bayo.ecommerce.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

public record ProductRequest (
        Integer id,

        @NotBlank(message = "product name is required")
        String name,


        @NotBlank(message = "product description is required")
        String description,


        @Positive(message = "available quantity should be positive")
        double availableQuantity,


        @Positive(message = "price should be positive")
        BigDecimal price,

        @NotNull(message = "product category is required")
        Integer categoryId
){
}
