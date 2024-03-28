package com.example.exception.demoex.exceptions;

import com.example.exception.demoex.controller.Example3Controller;
import com.example.exception.demoex.utils.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

// Target all Controllers annotated with @RestController
//@ControllerAdvice(annotations = RestController.class)
// Target all Controllers within specific packages
//@ControllerAdvice("com.example.exception.demoex.controller")
// Target all Controllers assignable to specific classes
//@ControllerAdvice(assignableTypes = {Example3Controller.class})
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);
    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorMessage> defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // Nothing to do
        logger.error("defaultErrorHandler :: Request: " + req.getRequestURL() + " raised " + e);

        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
}
