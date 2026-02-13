package com.example.ecom.repository;

import com.example.ecom.entity.OrderItem;
import com.example.ecom.util.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for OrderItem entity.
 * <p>
 * Extends JpaRepository to provide CRUD operations
 * for order item persistence and retrieval.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
