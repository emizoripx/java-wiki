package com.example.exception.demoex.controller;

import com.example.exception.demoex.exceptions.OrderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/example1")
public class Example1Controller {

    private final Logger logger = LoggerFactory.getLogger(Example1Controller.class);

    @RequestMapping(value="/orders/{id}", method= RequestMethod.GET)
    public String showOrder(@PathVariable("id") long id, Model model) {
        logger.info("showOrder: byException");
        if (id == 0) throw new OrderNotFoundException(String.valueOf(id));

        model.addAttribute(id);
        return "orderDetail";
    }

}
