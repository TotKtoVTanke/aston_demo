package com.example.demo.dto;

import java.math.BigDecimal;

public class DepositRequest {

    private BigDecimal cashIn;

    public BigDecimal getCashIn() {
        return cashIn;
    }

    public void setCashIn(BigDecimal cashIn) {
        this.cashIn = cashIn;
    }
}
