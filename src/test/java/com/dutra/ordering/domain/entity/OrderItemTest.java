package com.dutra.ordering.domain.entity;

import com.dutra.ordering.domain.valueobjects.Money;
import com.dutra.ordering.domain.valueobjects.ProductName;
import com.dutra.ordering.domain.valueobjects.Quantity;
import com.dutra.ordering.domain.valueobjects.id.OrderId;
import com.dutra.ordering.domain.valueobjects.id.ProductId;
import org.junit.jupiter.api.Test;


class OrderItemTest {

    @Test
    void shouldGeneretaWhenBrandNew() {
        OrderItem.brandNew()
                .productId(new ProductId())
                .quantity(new Quantity(1))
                .orderId(new OrderId())
                .productName(new ProductName("Mouse pad"))
                .price(new Money("100"))
                .build();
    }
}