package com.shop.orderservice.exception;

import feign.FeignException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.NotFound.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,Object> item404(FeignException.NotFound e){
        return Map.of("error","Item not found");
    }

    @ExceptionHandler(InsufficientInventoryException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String,Object> inventory(InsufficientInventoryException e){
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public Map<String,Object> feign(FeignException e){
        return Map.of("error","Item service error","status",e.status());
    }
}
