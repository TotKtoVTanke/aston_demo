package com.example.demo.dto;

import java.math.BigDecimal;

public class WithdrawRequest {

    private String pinCode;

    private BigDecimal cashOut;

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public BigDecimal getCashOut() {
        return cashOut;
    }

    public void setCashOut(BigDecimal cashOut) {
        this.cashOut = cashOut;
    }
}
