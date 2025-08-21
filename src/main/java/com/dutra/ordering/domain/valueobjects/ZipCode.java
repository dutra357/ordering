package com.dutra.ordering.domain.valueobjects;

import java.util.Objects;

public record ZipCode(String value) {

    public ZipCode {
        Objects.requireNonNull(value);

        if (value.isBlank()) {
            throw new IllegalArgumentException("Zipcode cannot be blank.");
        }

        if (value.length() != 8) {
            throw new IllegalArgumentException("Zipcode requires 8 digits.");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
