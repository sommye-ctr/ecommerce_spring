package org.example.ecommerce.service;

import org.example.ecommerce.payload.OrderDTO;
import org.example.ecommerce.payload.OrderRequestDTO;

public interface OrderService {
    OrderDTO orderProducts(OrderRequestDTO orderRequestDTO);
}
