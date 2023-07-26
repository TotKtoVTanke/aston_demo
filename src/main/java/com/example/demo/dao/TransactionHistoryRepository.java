package com.example.demo.dao;

import com.example.demo.entities.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {

    @Query("from TransactionHistory th where th.account.Id = :accountId")
    List<TransactionHistory> findTransactionHistoriesByAccountId(@Param("accountId") Long accountId);
}
