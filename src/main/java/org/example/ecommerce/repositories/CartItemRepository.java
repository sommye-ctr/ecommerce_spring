package org.example.ecommerce.repositories;

import org.example.ecommerce.models.CartItem;
import org.springframework.data.repository.CrudRepository;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {
}
