package com.n23.expense_sharing_app.exception;


import com.n23.expense_sharing_app.dto.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice // <--- Tells Spring: "This class handles global errors"
public class GlobalExceptionHandler {

    // 1. Handle Resource Not Found (e.g., User/Group not found) -> Returns 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex)
    {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }


    // 2. Handle Bad Credentials (Login failed) -> Returns 401
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex)
    {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid email or password",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error,HttpStatus.UNAUTHORIZED);
    }

    // 3. Handle Generic Errors (Catch-all) -> Returns 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse>handleGenericException(Exception ex)
    {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occured:" + ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);

    }






}
