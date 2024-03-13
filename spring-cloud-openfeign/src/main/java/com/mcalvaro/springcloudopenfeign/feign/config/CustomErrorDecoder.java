package com.mcalvaro.springcloudopenfeign.feign.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcalvaro.springcloudopenfeign.exceptions.BadRequestException;
import com.mcalvaro.springcloudopenfeign.exceptions.NotFoundException;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorDecoder.class);

    @Override
    public Exception decode(String method, Response response) {

        logger.debug("Capture Exception........................." + response.status());

        switch (response.status()) {

            case 400:
                return new BadRequestException("Bad Request");
            case 404:
                return new NotFoundException("Not Found");
            case 500:
                return new Exception("Internal Server Error");
            default:
                return new Exception();

        }

    }

}
