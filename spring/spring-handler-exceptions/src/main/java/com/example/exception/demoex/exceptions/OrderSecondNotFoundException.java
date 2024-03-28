package com.example.exception.demoex.exceptions;

public class OrderSecondNotFoundException extends RuntimeException {
    public OrderSecondNotFoundException(String orderId) {
        super(orderId + " not found");
    }
}
