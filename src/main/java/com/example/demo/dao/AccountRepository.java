package com.example.demo.dao;

import com.example.demo.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> getAllByBeneficiaryName(String beneficiaryName);


}
