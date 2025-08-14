package com.eteration.simplebanking.constants;

/**
 * Uygulama genelinde kullanılan sabit hata mesajları.
 * Tüm mesajlar burada tutulur ki magic string kullanımı olmasın.
 */
public final class ErrorMessages {

    private ErrorMessages() {

    }

    public static final String INSUFFICIENT_BALANCE = "Insufficient balance";
    public static final String ACCOUNT_NOT_FOUND = "Account not found";
    public static final String INVALID_TRANSACTION_AMOUNT = "Invalid transaction amount";
    public static final String TRANSACTION_DECLINED = "Transaction declined";
    public static final String GENERAL_ERROR = "An unexpected error occurred";
    public static final String AMOUNT_MUST_BE_POSITIVE = "Amount must be positive";
    public static final String PAYEE_REQUIRED = "Payee required";
    public static final String VALIDATION_ERROR = "validation error";
}
