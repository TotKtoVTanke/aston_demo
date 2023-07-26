package com.example.demo.services;

import com.example.demo.dto.DepositRequest;
import com.example.demo.dto.NewAccountRequest;
import com.example.demo.dto.TransferRequest;
import com.example.demo.dto.WithdrawRequest;
import com.example.demo.entities.Account;
import com.example.demo.entities.TransactionHistory;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;


public interface AccountServiceI {

    Account createNewAccount(@Valid NewAccountRequest accountPK);

    Account getAccount(Long id);

    void deposit(Long id, BigDecimal cashIn);

    void withdraw(Long id, String pinCode, BigDecimal cashOut);

    void transfer(TransferRequest transferRequest);

    List<Account> getAccounts(String userName);

    List<TransactionHistory> getTransactionHistory(Long id);

    void saveAccount(Account account);

}