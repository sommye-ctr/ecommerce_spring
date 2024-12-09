package org.example.ecommerce.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.ecommerce.payload.CartDTO;
import org.example.ecommerce.service.CartServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private CartServiceImpl cartService;

    @PostMapping("/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProduct(@PathVariable Long productId, @PathVariable int quantity) {
        CartDTO cartDTO = cartService.addCart(productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }
}
