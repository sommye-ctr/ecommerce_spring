package org.example.ecommerce.service;

import org.example.ecommerce.payload.CartDTO;

import java.util.List;

public interface CartService {
    CartDTO addCart(long productId, int quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCartByUser();

    CartDTO updateProduct(Long productId, int quantity);

    CartDTO deleteProductInCart(Long cartId, Long productId);
}
