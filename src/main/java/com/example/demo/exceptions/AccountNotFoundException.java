package com.example.demo.exceptions;

public class AccountNotFoundException extends WrongUserInfoException{
    public AccountNotFoundException(Long accountId) {
        super(String.format("account with id[%d] not found", accountId));
    }
}
