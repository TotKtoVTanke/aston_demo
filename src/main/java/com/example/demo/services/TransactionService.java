package com.example.demo.services;

import com.example.demo.entities.Account;
import com.example.demo.entities.TransactionHistory;
import com.example.demo.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    TransactionHistory saveNewTransaction(Account account, TransactionType transactionType, BigDecimal amount, LocalDateTime DateTime);

    List<TransactionHistory> saveNewTransferTransaction(Account sourceAccount, Account targetAccount, BigDecimal amount, LocalDateTime DateTime);

    List<TransactionHistory> getAccountsTransactionHistory(Long id);
}
