package com.example.ecom.repository;

import com.example.ecom.entity.Order;
import com.example.ecom.util.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Order entity.
 * <p>
 * Extends JpaRepository to provide CRUD operations and
 * custom query methods for order management.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Retrieves all orders with the given status.
     *
     * @param status order status used for filtering
     * @return list of orders matching the specified status
     */

    List<Order> findByStatus(OrderStatus status);//For filtering purpose
}
