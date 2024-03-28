package com.example.exception.demoex.controller;

import com.example.exception.demoex.exceptions.DataAccessException;
import com.example.exception.demoex.exceptions.DataIntegrityViolationException;
import com.example.exception.demoex.exceptions.OrderSecondNotFoundException;
import com.example.exception.demoex.utils.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
@RequestMapping("/example2")
public class Example2Controller {
    private final Logger logger = LoggerFactory.getLogger(Example2Controller.class);

    @RequestMapping(value="/invoices/{id}", method= RequestMethod.GET)
    public String showInvoice(@PathVariable("id") long id) {
        logger.info("showInvoice :: by Controller");
        if (id == 0) throw new DataIntegrityViolationException(String.valueOf(id));

        return "invoiceDetail";
    }

    @RequestMapping(value="/orders/{id}", method= RequestMethod.GET)
    public String showOrder(@PathVariable("id") long id) {
        logger.info("showOrder");
        if (id == 0) throw new OrderSecondNotFoundException(String.valueOf(id));

        return "orderDetail";
    }

    // Exception handling methods

    // Convert a predefined exception to an HTTP Status code
    @ResponseStatus(value= HttpStatus.CONFLICT,
            reason="Data integrity violation")  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<ErrorMessage> conflict() {
    public void conflict() {
        logger.info("conflict");
        // Nothing to do

//        ErrorMessage errorMessage = new ErrorMessage("Conflict");
//
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    // Specify name of a specific view that will be used to display the error:
    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public String databaseError() {
        logger.info("databaseError");
        // Nothing to do.  Returns the logical view name of an error page, passed
        // to the view-resolver(s) in usual way.
        // Note that the exception is NOT available to this view (it is not added
        // to the model) but see "Extending ExceptionHandlerExceptionResolver"
        // below.
        return "databaseError";
    }

    // Total control - setup a model and return the view name yourself. Or
    // consider subclassing ExceptionHandlerExceptionResolver (see below).
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleError(HttpServletRequest req, Exception ex) {
        logger.error("handleError :: Request: " + req.getRequestURL() + " raised " + ex);

        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

}
