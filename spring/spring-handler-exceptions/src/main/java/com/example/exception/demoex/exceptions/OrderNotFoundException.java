package com.example.exception.demoex.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such Order")  // 404
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String orderId) {
        super(orderId + " not found");
    }
}