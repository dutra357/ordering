package com.dutra.ordering.domain.valueobjects;

import com.dutra.ordering.domain.utility.FieldsValidation;


public record Email(String email) {

    public Email {
        FieldsValidation.requiresValidEmail(email);
    }

    @Override
    public String toString() {
        return email;
    }
}
