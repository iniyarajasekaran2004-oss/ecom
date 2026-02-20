package com.example.ecom.entity;

import com.example.ecom.util.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing an order placed by a customer.
 * Maps to the "Orders" table in the database.
 */
@Entity
@Table(name = "Orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    /**
     * Unique ID of the order.
     * Automatically generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Customer who placed the order.
     */
    @ManyToOne
    @JoinColumn(name = "customerid")
    private Customer customer;
    /**
     * Current status of the order.
     * Stored as string in the database.
     */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    /**
     * List of items included in the order.
     */
    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items= new ArrayList<>();

    private Double totalAmount;

}
