package org.example.ecommerce.repositories;

import org.example.ecommerce.models.CartItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {

    @Query("SELECT ci from CartItem where ci.cart.id = ?2 and ci.product.id = ?1")
    CartItem findByProductIdAndCartId(long productId, Long cartId);
}
