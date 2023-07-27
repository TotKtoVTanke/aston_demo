package com.example.demo.dto;

import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

public class TransferRequest {

    private Long sourceAccount;

    private Long targetAccount;

    @Pattern(regexp = "^\\d{4}$")
    private String pinCode;

    private BigDecimal cash;

    public Long getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Long sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Long getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(Long targetAccount) {
        this.targetAccount = targetAccount;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }
}
