package com.example.ecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing an item within an order.
 * Maps to the "order_items" table in the database.
 */
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    /**
     * Unique ID of the order item.
     * Automatically generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Order to which this item belongs.
     */

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)

    private Order order;
    /**
     * Product included in the order.
     */
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    /**
     * Quantity of the product ordered.
     */
    @Column(nullable = false)
    private int quantity;

}
