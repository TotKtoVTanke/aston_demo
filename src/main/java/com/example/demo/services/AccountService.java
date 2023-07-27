package com.example.demo.services;

import com.example.demo.dao.AccountRepository;
import com.example.demo.dto.NewAccountRequest;
import com.example.demo.dto.TransferRequest;
import com.example.demo.entities.Account;
import com.example.demo.entities.TransactionHistory;
import com.example.demo.enums.TransactionType;
import com.example.demo.exceptions.AccountNotFoundException;
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
public class AccountService implements AccountServiceI {

    private AccountRepository accountRepository;
    private TransactionServiceI transactionService;

    @Autowired
    public AccountService(AccountRepository accountRepository,
                          TransactionServiceI transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    @Override
    public Account createNewAccount(NewAccountRequest accountPK) {
        Account account = new Account();
        String name = accountPK.getBeneficiaryName();
       // validateName(name);
        account.setBeneficiaryName(name);
        String pinCode = accountPK.getPinCode();
       // validatePinFormat(pinCode);
        account.setPinCode(pinCode);
        account = accountRepository.save(account);
        return account;
    }

    @Override
    public Account getAccount(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(String.valueOf(id)));
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
        return accountRepository.getAllByBeneficiaryName(beneficiaryName);
    }

    @Override
    public List<TransactionHistory> getTransactionHistory(Long id) {
        return transactionService.getAccountsTransactionHistory(id);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

/*    private void validatePinFormat(String incomePin) {
        if (!incomePin.matches("^\\d{4}$")) throw new WrongPinCodeException();
    }*/

    private void validatePinCode(String incomePin, String currentPin) {
       // validatePinFormat(incomePin);
        if (!incomePin.equals(currentPin)) throw new WrongPinCodeException();
    }

/*    private void validateName(String name) {
        if (name == null || name.isBlank()) throw new WrongUserInfoException("Wrong name of beneficiary");
    }*/
}
