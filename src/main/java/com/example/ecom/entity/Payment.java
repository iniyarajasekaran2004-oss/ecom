package com.example.ecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a payment made for an order.
 * Maps to the "payments" table in the database.
 */
@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    /**
     * Unique ID of the payment.
     * Automatically generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Total amount paid for the order.
     */

    @Column(nullable = false)
    private double amount;
    /**
     * Date and time when the payment was made.
     */
    @Column(nullable = false)
    private LocalDateTime paymentDate;
    /**
     * Order associated with this payment.
     * One-to-one relationship.
     */
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)

    private Order order;
}
