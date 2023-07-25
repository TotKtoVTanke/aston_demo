package com.example.demo.entities;


import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "number", unique = true, updatable = false)
    private Long Id;

    @Column(name = "beneficiaryName", updatable = false)
    private String beneficiaryName;

    @Column(name = "pin_code")
    @Pattern(regexp = "^\\d{4}$")
    private String pinCode;

    @Column(name = "balance")
    private BigDecimal balance;

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

    public Long getId() {
        return Id;
    }

    public void setId(Long accountNumber) {
        this.Id = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
