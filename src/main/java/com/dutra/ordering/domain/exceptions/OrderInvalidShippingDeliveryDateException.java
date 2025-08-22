package com.dutra.ordering.domain.exceptions;

import com.dutra.ordering.domain.valueobjects.id.OrderId;

import static com.dutra.ordering.domain.exceptions.ErrorMessages.ERROR_ORDER_DELIVERY_DATE_CANNOT_BE_IN_PAST;

public class OrderInvalidShippingDeliveryDateException extends RuntimeException {


    public OrderInvalidShippingDeliveryDateException(OrderId id) {
        super(String.format(ERROR_ORDER_DELIVERY_DATE_CANNOT_BE_IN_PAST, id));
    }
}
