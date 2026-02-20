package com.example.ecom.util;

/**
 * Enumeration representing the lifecycle status of an Order.
 * <p>
 * Defines the valid states through which an order progresses
 * in the system.
 */
public enum OrderStatus {
    CREATED,
    PAID,
    SHIPPED,
    DELIVERED
}
