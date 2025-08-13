package com.shop.authservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneral(Exception ex) {
        return buildErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", status.value());
        error.put("error", message);
        return new ResponseEntity<>(error, status);
    }


    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String,Object> badCreds(InvalidCredentialsException e){
        return Map.of("error","Invalid email or password");
    }

    @ExceptionHandler(AccountDisabledException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String,Object> disabled(AccountDisabledException e){
        return Map.of("error","Account not allowed to login", "statusReason", e.getMessage());
    }
}