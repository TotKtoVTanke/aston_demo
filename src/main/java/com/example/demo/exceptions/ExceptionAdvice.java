package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({WrongUserInfoException.class,
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class})
    public ResponseEntity<String> handleException(Exception exception) {
        String response = exception.getMessage();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AccountNotFoundException.class, BeneficiaryNotFoundException.class})
    public ResponseEntity<String> handleException(WrongUserInfoException exception) {
        String response = exception.getMessage();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
