package com.example.demo.services;

import com.example.demo.dao.AccountRepository;
import com.example.demo.dto.NewAccountRequest;
import com.example.demo.entities.Account;
import com.example.demo.entities.TransactionHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class BeneficiaryService implements BeneficiaryServiceI{

    private AccountRepository accountRepository;

    @Autowired
    public BeneficiaryService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createNewAccount(@Valid NewAccountRequest accountPK) {
        Account account = new Account();
        account.setBeneficiaryName(accountPK.getBeneficiaryName());
        account.setPinCode(accountPK.getPinCode());
        account = accountRepository.save(account);
        return account;
    }

    @Override
    public Account getAccount(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public List<Account> getAccounts(String beneficiaryName) {
        return accountRepository.getAllByBeneficiaryName(beneficiaryName);
    }


    @Override
    public List<TransactionHistory> getTransactionHistory(String userName) {
        return null;
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }
}
