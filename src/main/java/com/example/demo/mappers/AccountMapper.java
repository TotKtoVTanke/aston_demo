package com.example.demo.mappers;

import com.example.demo.dto.AccountDto;
import com.example.demo.entities.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")

public interface AccountMapper {
    @Mapping(target = "id", source = "account.id")
    @Mapping(target = "balance", expression = "java(account.getBalance().toString())")
    AccountDto accountGetDto(Account account);

    List<AccountDto> accountGetDtoList(List<Account> accounts);

    Account accountDtoToAccount(AccountDto accountDto);

}
