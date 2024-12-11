package org.example.ecommerce.controller;

import lombok.AllArgsConstructor;
import org.example.ecommerce.payload.OrderDTO;
import org.example.ecommerce.payload.OrderRequestDTO;
import org.example.ecommerce.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderController {

    private OrderService orderService;

    @PostMapping("/users")
    public ResponseEntity<OrderDTO> orderProducts(@RequestBody OrderRequestDTO orderRequestDTO) {
        OrderDTO orderDTO = orderService.orderProducts(orderRequestDTO);
        return ResponseEntity.ok(orderDTO);
    }
}
