package com.example.demo.services;

import com.example.demo.dto.NewAccountRequest;
import com.example.demo.entities.Account;
import com.example.demo.entities.TransactionHistory;

import javax.validation.Valid;
import java.util.List;


public interface BeneficiaryServiceI {

    Account createNewAccount(@Valid NewAccountRequest accountPK);

    Account getAccount(Long id);

    List<Account> getAccounts(String userName);

    List<TransactionHistory> getTransactionHistory(String userName);

    void saveAccount(Account account);

}
