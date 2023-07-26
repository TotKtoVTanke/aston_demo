package com.example.demo.exceptions;

public class InsufficientFundsException extends WrongUserInfoException{
    public InsufficientFundsException(String accountId) {
        super(String.format("Insufficient funds in the account [%s]", accountId));
    }
}
