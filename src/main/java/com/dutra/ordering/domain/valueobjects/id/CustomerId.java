package com.dutra.ordering.domain.valueobjects.id;

import com.dutra.ordering.domain.utility.IdGenerator;

import java.util.Objects;
import java.util.UUID;

public record CustomerId(UUID valueId) {

    public CustomerId() {
        this(IdGenerator.generateTimeBasedUUID());
    }

    public CustomerId(UUID valueId) {
        this.valueId = Objects.requireNonNull(valueId);
    }

    @Override
    public String toString() {
        return valueId.toString();
    }
}
