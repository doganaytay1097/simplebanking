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
    public static final String AMOUNT_MUST_BE_POSITIVE = "Amount must be positive";
    public static final String PAYEE_REQUIRED = "Payee required";
    public static final String VALIDATION_ERROR = "Validation error";

    public static final String ACCOUNT_ALREADY_EXISTS = "Account already exists";
    public static final String MALFORMED_JSON = "Malformed JSON request";
    public static final String DATA_INTEGRITY_VIOLATION = "Data integrity violation";
    public static final String UNEXPECTED_ERROR = "Unexpected error occurred";
    public static final String PHONE_NUMBER_REQUIRED = "Phone number required";
}
