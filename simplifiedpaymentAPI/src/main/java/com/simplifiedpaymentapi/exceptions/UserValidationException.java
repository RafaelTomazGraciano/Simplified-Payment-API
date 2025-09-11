package com.simplifiedpaymentapi.exceptions;

import java.util.Map;

public class UserValidationException extends RuntimeException{
    private final Map<String, String> errors;

    public UserValidationException(Map<String, String> errors) {
        super("Validation failed");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
