package org.example.ecommerce.service;

import org.example.ecommerce.payload.CartDTO;

public interface CartService {
    CartDTO addCart(long productId, int quantity);
}
