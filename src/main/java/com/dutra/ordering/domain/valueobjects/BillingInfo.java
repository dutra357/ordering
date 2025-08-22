package com.dutra.ordering.domain.valueobjects;

import java.util.Objects;

public record BillingInfo(FullName fullName, Document document, Phone phone, Address address) {
    public BillingInfo {
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(document);
        Objects.requireNonNull(phone);
        Objects.requireNonNull(address);
    }
}
