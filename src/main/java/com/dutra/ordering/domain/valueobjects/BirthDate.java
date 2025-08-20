package com.dutra.ordering.domain.valueobjects;

import java.time.LocalDate;
import java.util.Objects;

public record BirthDate(LocalDate birthDate) {

    public BirthDate {
        Objects.requireNonNull(birthDate);

        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento não está no passado.");
        }
    }

    public Integer age() {
        return LocalDate.now().compareTo(birthDate);
    }

    @Override
    public String toString() {
        return birthDate.toString();
    }
}
