package com.mcalvaro.springcloudopenfeign.exceptions;
 
public class NotFoundException extends RuntimeException {
 
    public NotFoundException(String message) {
        super(message);
    }
}
