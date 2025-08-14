package com.eteration.simplebanking.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import static com.eteration.simplebanking.constants.ErrorMessages.*;

public class BillPaymentRequest {
    @NotBlank(message = PAYEE_REQUIRED)
    private String payee;

    @Positive(message = AMOUNT_MUST_BE_POSITIVE)
    private double amount;

    public String getPayee() { return payee; }
    public void setPayee(String payee) { this.payee = payee; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
