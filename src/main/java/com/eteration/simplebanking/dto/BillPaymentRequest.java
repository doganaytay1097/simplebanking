// src/main/java/com/eteration/simplebanking/dto/BillPaymentRequest.java
package com.eteration.simplebanking.dto;

import javax.validation.constraints.*;


import static com.eteration.simplebanking.constants.ErrorMessages.*;



public class BillPaymentRequest {

    @NotBlank(message = PAYEE_REQUIRED)
    private String payee;

    @NotNull(message = AMOUNT_MUST_BE_POSITIVE)
    @Positive(message = AMOUNT_MUST_BE_POSITIVE)
    private Double amount;

    @NotBlank(message = PHONE_NUMBER_REQUIRED)
    @Size(max = 20, message = PHONE_NUMBER_REQUIRED)
    private String phoneNumber;


    public String getPayee() { return payee; }
    public void setPayee(String payee) { this.payee = payee; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
