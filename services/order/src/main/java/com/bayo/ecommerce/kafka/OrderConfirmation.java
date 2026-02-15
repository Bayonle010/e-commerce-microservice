package com.bayo.ecommerce.kafka;

import com.bayo.ecommerce.customer.CustomerResponse;
import com.bayo.ecommerce.order.PaymentMethod;
import com.bayo.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation (
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
