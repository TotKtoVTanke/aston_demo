package com.example.demo.controllers;

import com.example.demo.dto.AccountDto;
import com.example.demo.entities.Account;
import com.example.demo.mappers.AccountMapper;
import com.example.demo.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/beneficiaries")
public class BeneficiaryController {

    private final AccountService accountService;

    private final AccountMapper accountMapper;

    @Autowired
    public BeneficiaryController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @GetMapping(path = "/{name}")
    public ResponseEntity<List<AccountDto>> receiveAccountsByBeneficiaryName(
            @PathVariable String name) {
        List<Account> result = accountService.getAccountsOfBeneficiary(name);
        return new ResponseEntity<>(accountMapper.accountGetDtoList(result), HttpStatus.OK);
    }
}
