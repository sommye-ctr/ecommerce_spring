package org.example.ecommerce.repositories;

import org.example.ecommerce.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c from Cart where c.user.id = ?1")
    Cart findByUserId(Long customerId);
}
