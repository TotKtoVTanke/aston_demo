package com.example.demo.controllers;



import com.example.demo.dto.NewAccountRequest;
import com.example.demo.entities.Account;
import com.example.demo.services.BeneficiaryServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("api/accountings")
public class AccountController {

    BeneficiaryServiceI userService;

    @Autowired
    public AccountController(BeneficiaryServiceI userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<String> check(@PathVariable Long id) {
        Account account = userService.getAccount(id);
        return ResponseEntity.ok(account.getBeneficiaryName());
    }
    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/add")
    public ResponseEntity<String> deposit(@PathVariable Long id) {
        Account account = userService.getAccount(id);
        BigDecimal temp = account.getBalance();
        if (temp == null) temp = new BigDecimal(0);
        temp = temp.add(new BigDecimal("100"));
        account.setBalance(temp);
        userService.saveAccount(account);
        return ResponseEntity.ok(account.getBeneficiaryName());
    }

    @PostMapping()
    public ResponseEntity<Long> createNewAccount(
            @RequestBody NewAccountRequest newAccountRequest) {
        Long accountId = userService.createNewAccount(newAccountRequest).getId();
        return ResponseEntity.ok(accountId);
    }


}
