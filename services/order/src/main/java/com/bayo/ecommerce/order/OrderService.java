package com.bayo.ecommerce.order;

import com.bayo.ecommerce.customer.CustomerClient;
import com.bayo.ecommerce.exception.BusinessException;
import com.bayo.ecommerce.kafka.OrderConfirmation;
import com.bayo.ecommerce.kafka.OrderProducer;
import com.bayo.ecommerce.orderline.OrderLineRequest;
import com.bayo.ecommerce.orderline.OrderLineService;
import com.bayo.ecommerce.product.ProductClient;
import com.bayo.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    public Integer createOrder(@Valid OrderRequest request) {
        // find the customer
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(()-> new BusinessException("Cannot create order :: NO customer exists with the provided ID:: " + request.customerId()));

        // purchase the product

        var purchasedProducts = this.productClient.purchaseProducts(request.products());

        // persist the order
        var order = this.orderRepository.save(mapper.toOrder(request));

        for (PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        // to do sart ecommerce process

        // send the order confimaion ---> notification -ms (Kafka)
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );


        return null;




    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(mapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(mapper::fromOrder)
                .orElseThrow(()-> new EntityNotFoundException(String.format("No order found with the provided ID: %d", orderId)));
    }
}
