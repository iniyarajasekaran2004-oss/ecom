package com.example.ecom.exception;

import com.example.ecom.util.OrderStatus;

public class InvalidOrderStatusTransitionException extends RuntimeException {

    public InvalidOrderStatusTransitionException(
            OrderStatus currentStatus,
            OrderStatus newStatus) {

        super("Invalid status transition from "
                + currentStatus + " to " + newStatus);
    }
}

