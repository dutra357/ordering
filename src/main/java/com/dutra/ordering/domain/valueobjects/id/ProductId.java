package com.dutra.ordering.domain.valueobjects.id;

import com.dutra.ordering.domain.utility.IdGenerator;

import java.util.Objects;
import java.util.UUID;

public record ProductId(UUID valueId) {

    public ProductId() {
        this(IdGenerator.generateTimeBasedUUID());
    }

    public ProductId(UUID valueId) {
        this.valueId = Objects.requireNonNull(valueId);
    }

    @Override
    public String toString() {
        return valueId.toString();
    }
}
