package com.example.ecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity representing a customer in the system.
 * Maps to the "Customers" table in the database.
 */
@Entity
@Table(name = "Customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    /**
     * Unique ID of the customer.
     * Automatically generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the customer.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String name;
    /**
     * Email of the customer.
     * Must be unique and cannot be null.
     */

    @Column(nullable = false, unique = true)
    private String email;
    /**
     * List of orders placed by the customer.
     */

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)

    private List<Order> orders;
}
