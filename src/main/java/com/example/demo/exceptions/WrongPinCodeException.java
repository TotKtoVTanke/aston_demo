package com.example.demo.exceptions;

public class WrongPinCodeException extends WrongUserInfoException{
    public WrongPinCodeException() {
        super("Wrong pin code - try again");
    }
}
