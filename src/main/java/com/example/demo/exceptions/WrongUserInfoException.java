package com.example.demo.exceptions;

public class WrongUserInfoException extends RuntimeException{
    public WrongUserInfoException(String message) {
        super(message);
    }
}
