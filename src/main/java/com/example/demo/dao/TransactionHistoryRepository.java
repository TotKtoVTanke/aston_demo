package com.example.demo.dao;

import com.example.demo.entities.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {

    @Query("from TransactionHistory th where th.account.Id = :accountId")
    List<TransactionHistory> findTransactionHistoriesByAccountId(@Param("accountId") Long accountId);
}
