package com.dutra.ordering.domain.valueobjects;

import com.dutra.ordering.domain.utility.FieldsValidation;

public record ProductName(String value) {

    public ProductName {
        FieldsValidation.requiresNotBlankOrNull(value);
    }

    @Override
    public String toString() {
        return value;
    }

}