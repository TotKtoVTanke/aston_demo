package com.example.demo.dto;

import org.springframework.stereotype.Component;

import javax.validation.constraints.Pattern;


public class NewAccountRequest {

    private String beneficiaryName;

    @Pattern(regexp = "^\\d{4}$")
    private String pinCode;

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}
