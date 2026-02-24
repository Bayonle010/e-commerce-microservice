package com.bayo.ecommerce.payment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record Customer(
        String id,

        @NotNull(message = "first name is requiredd")
        String firstname,

        @NotNull(message = "last name is required")
        String lastname,

        @NotNull(message = "email is required")
        @Email(message = "the customer email is not correctly formatted")
        String email
) {
}
