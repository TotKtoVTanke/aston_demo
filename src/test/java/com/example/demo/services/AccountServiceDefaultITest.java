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
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceDefaultITest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    TransactionService transactionService;

    @InjectMocks
    AccountServiceDefault accountServiceDefault;

    @Test
    void testCreateNewAccountWithCorrectData() {

        //given
        var request = new NewAccountRequest();
        String name = "Name";
        String pinCode = "1234";
        request.setBeneficiaryName(name);
        request.setPinCode(pinCode);
        var account = getAccount(name, pinCode, new BigDecimal("0"));
        account.setId(1L);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        //when
        Account accountTest = accountServiceDefault.createNewAccount(request);

        //then
        assertEquals(name, accountTest.getBeneficiaryName());
        assertEquals(pinCode, accountTest.getPinCode());
        assertNotNull(accountTest.getId());
        assertEquals(new BigDecimal("0"), accountTest.getBalance());
    }

    @Test
    void testGetExisitingAccount() {

        //given
        String name = "Name";
        String pinCode = "1234";
        var account = getAccount(name, pinCode, new BigDecimal("0"));
        account.setId(1L);
        when(accountRepository.findById(any(Long.class))).thenReturn(Optional.of(account));

        //when
        Account accountTest = accountServiceDefault.getAccount(1L);

        //then
        assertEquals(name, accountTest.getBeneficiaryName());
        assertEquals(pinCode, accountTest.getPinCode());
        assertNotNull(accountTest.getId());
        assertEquals(new BigDecimal("0"), accountTest.getBalance());
    }

    @Test
    void testGetNotExisitingAccount() {

        //given
        String name = "Name";
        String pinCode = "1234";
        var account = getAccount(name, pinCode, new BigDecimal("0"));
        account.setId(1L);
        when(accountRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(AccountNotFoundException.class,() -> accountServiceDefault.getAccount(1L));
    }

    @Test
    void testDepositSuccess() {

        //given
        String name = "Name";
        String pinCode = "1234";
        BigDecimal cashIn = new BigDecimal("100");
        var account = getAccount(name, pinCode, new BigDecimal("0"));
        account.setId(1L);

        //when
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        accountServiceDefault.deposit(1L, cashIn);

        //then
        assertEquals(account.getBalance(), cashIn);
        verify(transactionService,
                times(1))
                .saveNewTransaction(eq(account), eq(TransactionType.DEPOSIT), eq(cashIn), any(LocalDateTime.class));
    }

    @Test
    void testWithdrawCorrect() {

        //given
        String name = "Name";
        String pinCode = "1234";
        BigDecimal sourceBalance =  new BigDecimal("150");
        BigDecimal cashOut = new BigDecimal("100");
        var account = getAccount(name, pinCode, sourceBalance);
        account.setId(1L);

        //when
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        accountServiceDefault.withdraw(1L, pinCode, cashOut);

        //then
        assertEquals(account.getBalance(), sourceBalance.subtract(cashOut));
        verify(transactionService)
                .saveNewTransaction(eq(account), eq(TransactionType.WITHDRAW), eq(cashOut), any(LocalDateTime.class));
    }

    @Test
    void testWithdrawIncorrectPin() {

        //given
        String name = "Name";
        String currentPinCode = "1234";
        String incomePinCode = "1235";
        BigDecimal sourceBalance =  new BigDecimal("150");
        BigDecimal cashOut = new BigDecimal("100");
        var account = getAccount(name, currentPinCode, sourceBalance);
        account.setId(1L);

        //when
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        //then
        Assertions.assertThrows(WrongPinCodeException.class,() -> accountServiceDefault.withdraw(1L, incomePinCode, cashOut));
        assertEquals(sourceBalance, account.getBalance());
        verify(transactionService,
                never())
                .saveNewTransaction(eq(account), eq(TransactionType.WITHDRAW), eq(cashOut), any(LocalDateTime.class));
    }

    @Test
    void testWithdrawInsufficientFunds() {

        //given
        String name = "Name";
        String currentPinCode = "1234";
        String incomePinCode = "1234";
        BigDecimal sourceBalance =  new BigDecimal("150");
        BigDecimal cashOut = new BigDecimal("1000");
        var account = getAccount(name, currentPinCode, sourceBalance);
        account.setId(1L);

        //when
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        //then
        Assertions.assertThrows(InsufficientFundsException.class,() -> accountServiceDefault.withdraw(1L, incomePinCode, cashOut));
        assertEquals(sourceBalance, account.getBalance());
        verify(transactionService,
                times(0))
                .saveNewTransaction(eq(account), eq(TransactionType.WITHDRAW), eq(cashOut), any(LocalDateTime.class));
    }

    private Account getAccount(String name, String pinCode, BigDecimal balance) {
        Account account = getAccount(name, pinCode);
        account.setBalance(balance);
        return account;
    }

    private Account getAccount(String name, String pinCode) {
        Account account = new Account();
        account.setId(1L);
        account.setPinCode(pinCode);
        account.setBeneficiaryName(name);
        account.setBalance(new BigDecimal("0"));
        return account;
    }
}