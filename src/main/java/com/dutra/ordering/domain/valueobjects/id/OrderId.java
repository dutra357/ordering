package com.dutra.ordering.domain.valueobjects.id;

import com.dutra.ordering.domain.utility.IdGenerator;
import io.hypersistence.tsid.TSID;

import java.util.Objects;

public record OrderId(TSID value) {

    public OrderId() {
        this(IdGenerator.generateTSID());
    }

    public OrderId {
        Objects.requireNonNull(value);
    }

    public OrderId(Long value) {
        this(TSID.from(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
