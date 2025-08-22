package com.dutra.ordering.domain.exceptions;

import com.dutra.ordering.domain.valueobjects.id.OrderId;

public class OrderCannotBePlacedException extends RuntimeException {


    public OrderCannotBePlacedException(OrderId id) {
        super(String.format(ErrorMessages.ERROR_ORDER_CANNOT_BE_PLACED, id));
    }
}
