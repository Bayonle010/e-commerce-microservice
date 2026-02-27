package com.bayo.ecommerce.payment;

import com.bayo.ecommerce.customer.CustomerResponse;
import com.bayo.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
