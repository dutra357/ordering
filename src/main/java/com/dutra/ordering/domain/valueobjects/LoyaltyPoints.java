package com.dutra.ordering.domain.valueobjects;

import java.util.Objects;

public record LoyaltyPoints(Integer points) implements Comparable<LoyaltyPoints> {

    public LoyaltyPoints() {
        this(0);
    }

    public LoyaltyPoints {
        Objects.requireNonNull(points);

        if (points < 0) {
            throw new IllegalArgumentException("Negative number informed for points is invalid.");
        }

    }

    public LoyaltyPoints add(Integer points) {
        return add(new LoyaltyPoints(points));
    }

    public LoyaltyPoints add(LoyaltyPoints loyaltyPoints) {
        Objects.requireNonNull(loyaltyPoints);

        if (loyaltyPoints.points <= 0) {
            throw new IllegalArgumentException("Negative number informed for points is invalid.");
        }

        return new LoyaltyPoints(this.points() + loyaltyPoints.points);
    }

    @Override
    public String toString() {
        return points.toString();
    }

    @Override
    public int compareTo(LoyaltyPoints object) {
        return this.points.compareTo(object.points);
    }
}
