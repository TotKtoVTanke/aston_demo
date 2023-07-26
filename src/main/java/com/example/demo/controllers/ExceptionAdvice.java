package com.example.demo.controllers;

import com.example.demo.exceptions.WrongUserInfoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({WrongUserInfoException.class, ConstraintViolationException.class})
    public ResponseEntity<String> handleException(Exception exception) {
        String response = exception.getMessage();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
