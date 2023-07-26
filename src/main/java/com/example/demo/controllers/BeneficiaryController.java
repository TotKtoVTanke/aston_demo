package com.example.demo.controllers;

import com.example.demo.dto.AccountDto;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.services.AccountServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/beneficiaries")
public class BeneficiaryController {

    AccountServiceI accountService;

    AccountMapper accountMapper;

    private final MessageSource messageSource;

    @Autowired
    public BeneficiaryController(AccountServiceI accountService, AccountMapper accountMapper, MessageSource messageSource) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
        this.messageSource = messageSource;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{name}")
    public List<AccountDto> receiveAccountsByBeneficiaryName(
            @PathVariable String name) {
        return accountMapper.accountGetDtoList(accountService.getAccounts(name));
    }
}
