package com.dutra.ordering.domain.valueobjects;

import com.dutra.ordering.domain.utility.FieldsValidation;

import java.util.Objects;

public record Address(String street, String number,
                      String Complement, String neighborhood,
                      String city, String state, ZipCode zipCode) {

    public Address {
        FieldsValidation.requiresNotBlankOrNull(street);
        FieldsValidation.requiresNotBlankOrNull(number);
        FieldsValidation.requiresNotBlankOrNull(neighborhood);
        FieldsValidation.requiresNotBlankOrNull(city);
        FieldsValidation.requiresNotBlankOrNull(state);
        Objects.requireNonNull(zipCode);
    }
}
