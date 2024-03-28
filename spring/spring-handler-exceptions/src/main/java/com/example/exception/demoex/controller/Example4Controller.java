package com.example.exception.demoex.controller;

import com.example.exception.demoex.exceptions.InvalidCreditCardException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/example4")
public class Example4Controller {

    private final Logger logger = LoggerFactory.getLogger(Example4Controller.class);

    @RequestMapping(value="/credit-card/{codigo}", method= RequestMethod.GET)
    public void showCreditCard(@PathVariable("codigo") String codigo) throws Exception{
        logger.info("showCreditCard :: by Global");
        throw new InvalidCreditCardException("Incorrect " + codigo);
    }

}
