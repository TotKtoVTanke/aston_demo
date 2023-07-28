package com.example.demo.services;

import com.example.demo.dao.AccountRepository;
import com.example.demo.dto.NewAccountRequest;
import com.example.demo.dto.TransferRequest;
import com.example.demo.entities.Account;
import com.example.demo.entities.TransactionHistory;
import com.example.demo.enums.TransactionType;
import com.example.demo.exceptions.AccountNotFoundException;
import com.example.demo.exceptions.BeneficiaryNotFoundException;
import com.example.demo.exceptions.InsufficientFundsException;
import com.example.demo.exceptions.WrongPinCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountServiceDefault implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    @Autowired
    public AccountServiceDefault(AccountRepository accountRepository,
                                 TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    @Override
    public Account createNewAccount(NewAccountRequest accountPK) {
        Account account = new Account();
        String name = accountPK.getBeneficiaryName();
        account.setBeneficiaryName(name);
        String pinCode = accountPK.getPinCode();
        account.setPinCode(pinCode);
        account = accountRepository.save(account);
        return account;
    }

    @Override
    public Account getAccount(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Override
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void deposit(Long id, BigDecimal cashIn) {
        Account account = getAccount(id);
        BigDecimal currentBalance = account.getBalance()!=null?account.getBalance():new BigDecimal("0");
        currentBalance = currentBalance.add(cashIn);
        account.setBalance(currentBalance);
        transactionService.saveNewTransaction(account, TransactionType.DEPOSIT, cashIn, LocalDateTime.now());
    }

    @Override
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void withdraw(Long id, String incomePin, BigDecimal cashOut) {
        Account account = getAccount(id);
        validatePinCode(incomePin, account.getPinCode());
        BigDecimal currentBalance = account.getBalance()!=null?account.getBalance():new BigDecimal("0");
        if (cashOut.compareTo(currentBalance) > 0) throw new InsufficientFundsException(String.valueOf(id));
        else currentBalance = currentBalance.subtract(cashOut);
        account.setBalance(currentBalance);
        transactionService.saveNewTransaction(account, TransactionType.WITHDRAW, cashOut, LocalDateTime.now());
    }

    @Override
    @Transactional()
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void transfer(TransferRequest transferRequest) {
        BigDecimal cash = transferRequest.getCash();
        withdraw(transferRequest.getSourceAccount(), transferRequest.getPinCode(), cash);
        deposit(transferRequest.getTargetAccount(), cash);
    }

    @Override
    public List<Account> getAccountsOfBeneficiary(String beneficiaryName) {
        List<Account> result = accountRepository.getAllByBeneficiaryName(beneficiaryName);
        if (result.isEmpty()) {
            throw new BeneficiaryNotFoundException("Beneficiary with the same name and with active accounts was not found");
        }
        return result;
    }

    @Override
    public List<TransactionHistory> getTransactionHistory(Long id) {
        return transactionService.getAccountsTransactionHistory(id);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    private void validatePinCode(String incomePin, String currentPin) {
        if (!incomePin.equals(currentPin)) throw new WrongPinCodeException();
    }

}
