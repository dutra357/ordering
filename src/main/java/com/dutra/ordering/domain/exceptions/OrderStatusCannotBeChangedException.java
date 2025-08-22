package com.dutra.ordering.domain.exceptions;

import com.dutra.ordering.domain.entity.enums.OrderStatus;
import com.dutra.ordering.domain.valueobjects.id.OrderId;

import static com.dutra.ordering.domain.exceptions.ErrorMessages.ERROR_ORDER_STATUS_CANNOT_BE_CHANGED;

public class OrderStatusCannotBeChangedException extends RuntimeException {

    public OrderStatusCannotBeChangedException(OrderId id, OrderStatus orderStatus, OrderStatus newOrderStatus) {
        super(String.format(ERROR_ORDER_STATUS_CANNOT_BE_CHANGED, id, orderStatus, newOrderStatus));
    }
}
