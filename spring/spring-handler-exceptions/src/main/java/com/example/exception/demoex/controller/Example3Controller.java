package com.example.exception.demoex.controller;

import com.example.exception.demoex.exceptions.OrderSecondNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/example3")
public class Example3Controller {

    private final Logger logger = LoggerFactory.getLogger(Example3Controller    .class);

    @RequestMapping(value="/orders/{id}", method= RequestMethod.GET)
    public String showOrder(@PathVariable("id") long id) {
        logger.info("showOrder :: by Global");
        if (id == 0) throw new OrderSecondNotFoundException(String.valueOf(id));

        return "orderDetail";
    }
}
