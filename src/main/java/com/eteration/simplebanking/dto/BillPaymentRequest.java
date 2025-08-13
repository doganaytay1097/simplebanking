package com.eteration.simplebanking.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class BillPaymentRequest {
    @NotBlank(message = "payee is required")
    private String payee;

    @Positive(message = "amount must be positive")
    private double amount;

    public String getPayee() { return payee; }
    public void setPayee(String payee) { this.payee = payee; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
