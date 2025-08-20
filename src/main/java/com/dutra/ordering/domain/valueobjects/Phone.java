package com.dutra.ordering.domain.valueobjects;

import java.util.Objects;

public record Phone(String phone) {

    public Phone {
        Objects.requireNonNull(phone);

        if (phone.isBlank()) {
            throw new IllegalArgumentException("Phone cannot be blank.");
        }
    }

    @Override
    public String toString() {
        return phone;
    }
}
