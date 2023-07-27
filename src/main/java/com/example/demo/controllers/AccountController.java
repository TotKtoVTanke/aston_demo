package com.example.demo.controllers;


import com.example.demo.dto.DepositRequest;
import com.example.demo.dto.NewAccountRequest;
import com.example.demo.dto.TransactionHistoryDto;
import com.example.demo.dto.TransferRequest;
import com.example.demo.dto.WithdrawRequest;
import com.example.demo.mapper.TransactionHistoryMapper;
import com.example.demo.services.AccountServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("api/accounts")
@Validated
public class AccountController {

    AccountServiceI accountService;

    TransactionHistoryMapper transactionHistoryMapper;

    private final MessageSource messageSource;

    @Autowired
    public AccountController(AccountServiceI accountServiceI,
                             TransactionHistoryMapper transactionHistoryMapper,
                             MessageSource messageSource) {
        this.accountService = accountServiceI;
        this.transactionHistoryMapper = transactionHistoryMapper;
        this.messageSource = messageSource;
    }


    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/deposit")
    public ResponseEntity<String> deposit(
            @PathVariable Long id,
            @RequestBody DepositRequest depositRequest,
            Locale locale) {
        BigDecimal cashIn = depositRequest.getCashIn();
        accountService.deposit(id, cashIn);
        return ResponseEntity.ok(messageSource.getMessage("deposit.success", new Object[0], Locale.ENGLISH));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/withdraw")
    public ResponseEntity<String> withdraw(
            @PathVariable Long id,
            @RequestBody WithdrawRequest withdrawRequest,
            Locale locale) {
        String pinCode = withdrawRequest.getPinCode();
        BigDecimal cashOut = withdrawRequest.getCashOut();
        accountService.withdraw(id, pinCode, cashOut);
        return ResponseEntity.ok(messageSource.getMessage("withdraw.success", new Object[0], Locale.ENGLISH));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/transfer")
    public ResponseEntity<String> transfer(
            @RequestBody @Valid TransferRequest transferRequest,
            Locale locale) {
        accountService.transfer(transferRequest);
        return ResponseEntity.ok(messageSource.getMessage("transfer.success", new Object[0], Locale.ENGLISH));
    }

    @PostMapping()
    public ResponseEntity<Long> createNewAccount(
            @RequestBody @Valid NewAccountRequest newAccountRequest) {
        Long accountId = accountService.createNewAccount(newAccountRequest).getId();
        return ResponseEntity.ok(accountId);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/history")
    public ResponseEntity<List<TransactionHistoryDto>> getTransactions(
            @PathVariable Long id) {
        return ResponseEntity.ok(transactionHistoryMapper.transactionHistoryGetDtoList(accountService.getTransactionHistory(id)));
    }



}
