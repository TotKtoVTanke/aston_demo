package com.example.demo.controllers;


import com.example.demo.dto.*;
import com.example.demo.mappers.TransactionHistoryMapper;
import com.example.demo.services.AccountService;
import com.example.demo.services.LocaleMessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("api/accounts")
@Validated
public class AccountController {

    private final AccountService accountService;

    private final TransactionHistoryMapper transactionHistoryMapper;

    private final LocaleMessageProvider localeMessageProvider;

    @Autowired
    public AccountController(AccountService accountService,
                             TransactionHistoryMapper transactionHistoryMapper,
                             LocaleMessageProvider localeMessageProvider) {
        this.accountService = accountService;
        this.transactionHistoryMapper = transactionHistoryMapper;
        this.localeMessageProvider = localeMessageProvider;
    }


    @PutMapping(path = "/{id}/deposit")
    public ResponseEntity<String> deposit(
            @PathVariable Long id,
            @RequestBody DepositRequest depositRequest,
            Locale locale) {
        BigDecimal cashIn = depositRequest.getCashIn();
        accountService.deposit(id, cashIn);
        return new ResponseEntity<>(localeMessageProvider.getMessage("deposit.success", locale), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}/withdraw")
    public ResponseEntity<String> withdraw(
            @PathVariable Long id,
            @RequestBody WithdrawRequest withdrawRequest,
            Locale locale) {
        String pinCode = withdrawRequest.getPinCode();
        BigDecimal cashOut = withdrawRequest.getCashOut();
        accountService.withdraw(id, pinCode, cashOut);
        return new ResponseEntity<>(localeMessageProvider.getMessage("withdraw.success", locale), HttpStatus.OK);
    }

    @PutMapping(path = "/transfer")
    public ResponseEntity<String> transfer(
            @RequestBody @Valid TransferRequest transferRequest,
            Locale locale) {
        accountService.transfer(transferRequest);
        return new ResponseEntity<>(localeMessageProvider.getMessage("transfer.success", locale), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Long> createAccount(
            @RequestBody @Valid NewAccountRequest newAccountRequest) {
        Long accountId = accountService.createNewAccount(newAccountRequest).getId();
        return new ResponseEntity<>(accountId, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}/history")
    public ResponseEntity<List<TransactionHistoryDto>> getTransactions(
            @PathVariable Long id) {
        return new ResponseEntity<>(transactionHistoryMapper.transactionHistoryGetDtoList(accountService.getTransactionHistory(id)), HttpStatus.OK);
    }


}
