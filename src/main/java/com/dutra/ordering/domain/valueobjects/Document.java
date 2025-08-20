package com.dutra.ordering.domain.valueobjects;

import java.util.Objects;

public record Document(String document) {

    public Document {
        Objects.requireNonNull(document);

        if (document.isBlank()) {
            throw new IllegalArgumentException("Document cannot be blank.");
        }
    }

    @Override
    public String toString() {
        return document;
    }
}
