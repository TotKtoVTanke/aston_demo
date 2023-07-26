package com.example.demo.dto;

public class WithdrawRequest {

    private String pinCode;

    private String cashOut;

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getCashOut() {
        return cashOut;
    }

    public void setCashOut(String cashOut) {
        this.cashOut = cashOut;
    }
}
