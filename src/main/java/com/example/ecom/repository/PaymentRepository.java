package com.example.ecom.repository;

import com.example.ecom.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Payment entity.
 * <p>
 * Extends JpaRepository to provide CRUD operations
 * and custom query methods for payment management.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    /**
     * Retrieves a payment by the associated order ID.
     *
     * @param orderId ID of the related order
     * @return Optional containing the payment if found
     */
    Optional<Payment> findByOrderId(Long orderId);
}
