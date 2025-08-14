package com.eteration.simplebanking.exception;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(String message) { super(message); }
}
