package com.example.ecom.repository;

import com.example.ecom.entity.Customer;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

/**
 * Repository interface for Customer entity.
 * <p>
 * Extends JpaRepository to provide CRUD operations and
 * custom query methods for customer-related database access.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    //for checking duplicate email

    /**
     * Retrieves a customer by email.
     *
     * @param email email address of the customer
     * @return Optional containing the customer if found
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Checks whether a customer exists with the given email.
     *
     * @param email email address to check
     * @return true if a customer exists, otherwise false
     */
    boolean existsByEmail(@NotBlank(message = "Email should not be blank") String email);


}
