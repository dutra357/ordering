package com.dutra.ordering.domain.utility;

import com.dutra.ordering.domain.exceptions.ErrorMessages;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

public class FieldsValidation {

    private FieldsValidation() {}

    public static void requiresValidEmail(String email) {
        requiresValidEmail(email, null);
    }

    public static void requiresValidEmail(String email, String msg) {
        Objects.requireNonNull(email, msg);

        if (email.isBlank()) {
            throw new IllegalArgumentException(ErrorMessages.VALIDATION_ERROR_EMAIL_IS_BLANK);
        }

        if (!EmailValidator.getInstance().isValid(email)) {
            throw new IllegalArgumentException(ErrorMessages.VALIDATION_ERROR_EMAIL_IS_INVALID);
        }
    }

    public static void requiresNotBlankOrNull(String value) {
        Objects.requireNonNull(value);

        if (value.isBlank()) {
            throw new IllegalArgumentException("Cannot be blank.");
        }
    }
}
