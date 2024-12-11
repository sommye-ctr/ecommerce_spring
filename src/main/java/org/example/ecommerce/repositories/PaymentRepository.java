package org.example.ecommerce.repositories;

import org.example.ecommerce.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}