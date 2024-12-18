package org.example.ecommerce.controller;

import org.example.ecommerce.payload.CartDTO;
import org.example.ecommerce.service.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class CartController {

    CartServiceImpl cartService;

    @Autowired
    public CartController(CartServiceImpl cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProduct(@PathVariable Long productId, @PathVariable int quantity) {
        CartDTO cartDTO = cartService.addCart(productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<CartDTO> list = cartService.getAllCarts();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartByUser() {
        CartDTO cartDTO = cartService.getCartByUser();
        return ResponseEntity.ok(cartDTO);
    }

    @PutMapping("/cart/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> updateProductQuantity(@PathVariable Long productId, @PathVariable int quantity) {
        CartDTO cartDTO = cartService.updateProduct(productId, quantity);
        return ResponseEntity.ok(cartDTO);
    }

    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductInCart(@PathVariable Long cartId, @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.deleteProductInCart(cartId, productId));
    }
}
