package com.example.demo.exceptions;

public class BeneficiaryNotFoundException extends WrongUserInfoException{
    public BeneficiaryNotFoundException(String message) {
        super(message);
    }
}
