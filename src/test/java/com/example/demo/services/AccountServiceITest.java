package com.example.demo.services;

import com.example.demo.dao.AccountRepository;
import com.example.demo.dto.NewAccountRequest;
import com.example.demo.entities.Account;
import com.example.demo.enums.TransactionType;
import com.example.demo.exceptions.AccountNotFoundException;
import com.example.demo.exceptions.InsufficientFundsException;
import com.example.demo.exceptions.WrongPinCodeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        var account = createAccount(name, pinCode, new BigDecimal("0"));
        account.setId(1L);
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
        var account = createAccount(name, pinCode, new BigDecimal("0"));
        account.setId(1L);
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
        var account = createAccount(name, pinCode, new BigDecimal("0"));
        account.setId(1L);
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
        var account = createAccount(name, pinCode, new BigDecimal("0"));
        account.setId(1L);
        when(accountRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(AccountNotFoundException.class,() -> accountService.getAccount(1L));
    }

    @Test
    void depositSuccess() {

        //given
        String name = "Name";
        String pinCode = "1234";
        BigDecimal cashIn = new BigDecimal("100");
        var account = createAccount(name, pinCode, new BigDecimal("0"));
        account.setId(1L);

        //when
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        accountService.deposit(1L, cashIn);

        //then
        assertEquals(account.getBalance(), cashIn);
        Mockito.verify(transactionService,
                Mockito.times(1))
                .saveNewTransaction(eq(account), eq(TransactionType.DEPOSIT), eq(cashIn), any(LocalDateTime.class));
    }

    @Test
    void withdrawCorrect() {

        //given
        String name = "Name";
        String pinCode = "1234";
        BigDecimal sourceBalance =  new BigDecimal("150");
        BigDecimal cashOut = new BigDecimal("100");
        var account = createAccount(name, pinCode, sourceBalance);
        account.setId(1L);

        //when
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        accountService.withdraw(1L, pinCode, cashOut);

        //then
        assertEquals(account.getBalance(), sourceBalance.subtract(cashOut));
        Mockito.verify(transactionService,
                Mockito.times(1))
                .saveNewTransaction(eq(account), eq(TransactionType.WITHDRAW), eq(cashOut), any(LocalDateTime.class));
    }

    @Test
    void withdrawIncorrectPin() {

        //given
        String name = "Name";
        String currentPinCode = "1234";
        String incomePinCode = "1235";
        BigDecimal sourceBalance =  new BigDecimal("150");
        BigDecimal cashOut = new BigDecimal("100");
        var account = createAccount(name, currentPinCode, sourceBalance);
        account.setId(1L);

        //when
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        //then
        Assertions.assertThrows(WrongPinCodeException.class,() -> accountService.withdraw(1L, incomePinCode, cashOut));
        assertEquals(sourceBalance, account.getBalance());
        Mockito.verify(transactionService,
                Mockito.times(0))
                .saveNewTransaction(eq(account), eq(TransactionType.WITHDRAW), eq(cashOut), any(LocalDateTime.class));
    }

    @Test
    void withdrawInsufficientFunds() {

        //given
        String name = "Name";
        String currentPinCode = "1234";
        String incomePinCode = "1234";
        BigDecimal sourceBalance =  new BigDecimal("150");
        BigDecimal cashOut = new BigDecimal("1000");
        var account = createAccount(name, currentPinCode, sourceBalance);
        account.setId(1L);

        //when
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        //then
        Assertions.assertThrows(InsufficientFundsException.class,() -> accountService.withdraw(1L, incomePinCode, cashOut));
        assertEquals(sourceBalance, account.getBalance());
        Mockito.verify(transactionService,
                Mockito.times(0))
                .saveNewTransaction(eq(account), eq(TransactionType.WITHDRAW), eq(cashOut), any(LocalDateTime.class));
    }

    private Account createAccount(String name, String pinCode, BigDecimal balance) {
        Account account = createAccount(name, pinCode);
        account.setBalance(balance);
        return account;
    }

    private Account createAccount(String name, String pinCode) {
        Account account = new Account();
        account.setId(1L);
        account.setPinCode(pinCode);
        account.setBeneficiaryName(name);
        account.setBalance(new BigDecimal("0"));
        return account;
    }
}