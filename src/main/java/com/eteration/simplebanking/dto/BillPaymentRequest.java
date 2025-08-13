package com.eteration.simplebanking.dto;

public class BillPaymentRequest {
    private String payee;
    private double amount;

    public String getPayee() { return payee; }
    public void setPayee(String payee) { this.payee = payee; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
