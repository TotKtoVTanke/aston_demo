package com.example.demo.services;

import com.example.demo.dao.TransactionHistoryRepository;
import com.example.demo.entities.Account;
import com.example.demo.entities.TransactionHistory;
import com.example.demo.enums.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService implements TransactionServiceI {

    TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    public TransactionService(TransactionHistoryRepository transactionHistoryRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    @Override
    public TransactionHistory saveNewTransaction(Account account, TransactionType transactionType, BigDecimal amount, LocalDateTime dateTime) {
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setAccount(account);
        transactionHistory.setTransactionType(transactionType);
        transactionHistory.setChange(amount);
        transactionHistory.setTransactionDateTime(dateTime);
        return transactionHistoryRepository.save(transactionHistory);
    }

    @Override
    public List<TransactionHistory> saveNewTransferTransaction(Account sourceAccount, Account targetAccount, BigDecimal amount, LocalDateTime dateTime) {
        TransactionHistory sourceAccountTransaction = saveNewTransaction(sourceAccount, TransactionType.WITHDRAW, amount, dateTime);
        TransactionHistory targetAccountTransaction = saveNewTransaction(sourceAccount, TransactionType.DEPOSIT, amount, dateTime);
        List<TransactionHistory> result = new ArrayList<>();
        result.add(sourceAccountTransaction);
        result.add(targetAccountTransaction);
        return result;
    }

    @Override
    public List<TransactionHistory> getAccountsTransactionHistory(Long id) {
        return transactionHistoryRepository.findTransactionHistoriesByAccountId(id);
    }
}
