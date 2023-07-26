package com.example.demo.exceptions;

public class AccountNotFoundException extends WrongUserInfoException{
    public AccountNotFoundException(String accountId) {
        super(String.format("account with id[%s] not found", accountId));
    }
}
