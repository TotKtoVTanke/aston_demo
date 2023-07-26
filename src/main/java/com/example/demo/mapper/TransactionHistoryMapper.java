package com.example.demo.mapper;

import com.example.demo.dto.TransactionHistoryDto;
import com.example.demo.entities.TransactionHistory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionHistoryMapper {

    TransactionHistoryDto transactionHistoryGetDto(TransactionHistory transactionHistory);

    List<TransactionHistoryDto> transactionHistoryGetDtoList (List<TransactionHistory> transactionHistory);

    TransactionHistory transactionHistoryDtoToTransactionHistory(TransactionHistoryDto transactionHistoryDto);

}
