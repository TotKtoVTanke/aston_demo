package com.example.demo.services;

import com.example.demo.dao.AccountRepository;
import com.example.demo.dto.NewAccountRequest;
import com.example.demo.entities.Account;
import com.example.demo.exceptions.AccountNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceITest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    TransactionServiceI transactionService;

    @InjectMocks
    AccountService accountService;

    @Test
    void createNewAccountWithCorrectData() {

        //given
        var request = new NewAccountRequest();
        String name = "Name";
        String pinCode = "1234";
        request.setBeneficiaryName(name);
        request.setPinCode(pinCode);
        var account = new Account();
        account.setId(1L);
        account.setPinCode(pinCode);
        account.setBeneficiaryName(name);
        account.setBalance(new BigDecimal("0"));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        //when
        Account accountTest = accountService.createNewAccount(request);

        //then
        assertEquals(name, accountTest.getBeneficiaryName());
        assertEquals(pinCode, accountTest.getPinCode());
        assertNotNull(accountTest.getId());
        assertEquals(new BigDecimal("0"), accountTest.getBalance());
    }

    void createNewAccountWithIncorrectName() {

        //given
        var request = new NewAccountRequest();
        String name = "";
        String pinCode = "1234";
        request.setBeneficiaryName(name);
        request.setPinCode(pinCode);
        var account = new Account();
        account.setId(1L);
        account.setPinCode(pinCode);
        account.setBeneficiaryName(name);
        account.setBalance(new BigDecimal("0"));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        //when
        Account accountCheck = accountService.createNewAccount(request);

        //then
        Assertions.assertThrows(ConstraintViolationException.class,() -> accountService.createNewAccount(request));

    }

    @Test
    void getExisitingAccount() {

        //given
        String name = "Name";
        String pinCode = "1234";
        var account = new Account();
        account.setId(1L);
        account.setPinCode(pinCode);
        account.setBeneficiaryName(name);
        account.setBalance(new BigDecimal("0"));
        when(accountRepository.findById(any(Long.class))).thenReturn(Optional.of(account));

        //when
        Account accountTest = accountService.getAccount(1L);

        //then
        assertEquals(name, accountTest.getBeneficiaryName());
        assertEquals(pinCode, accountTest.getPinCode());
        assertNotNull(accountTest.getId());
        assertEquals(new BigDecimal("0"), accountTest.getBalance());
    }

    @Test
    void getNotExisitingAccount() {

        //given
        String name = "Name";
        String pinCode = "1234";
        var account = new Account();
        account.setId(1L);
        account.setPinCode(pinCode);
        account.setBeneficiaryName(name);
        account.setBalance(new BigDecimal("0"));
        when(accountRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(AccountNotFoundException.class,() -> accountService.getAccount(1L));
    }

    @Test
    void deposit() {
    }

    @Test
    void withdraw() {
    }

    @Test
    void transfer() {
    }

    @Test
    void getAccounts() {
    }

    @Test
    void getTransactionHistory() {
    }

    @Test
    void saveAccount() {
    }
}