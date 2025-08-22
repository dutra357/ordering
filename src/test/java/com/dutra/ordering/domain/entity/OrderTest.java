package com.dutra.ordering.domain.entity;

import com.dutra.ordering.domain.exceptions.OrderStatusCannotBeChangedException;
import com.dutra.ordering.domain.valueobjects.Money;
import com.dutra.ordering.domain.valueobjects.ProductName;
import com.dutra.ordering.domain.valueobjects.Quantity;
import com.dutra.ordering.domain.valueobjects.id.CustomerId;
import com.dutra.ordering.domain.valueobjects.id.OrderItemId;
import com.dutra.ordering.domain.valueobjects.id.ProductId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void draft() {
        Order order = Order.draft(new CustomerId());
    }

    @Test
    void addItem() {

        Order order = Order.draft(new CustomerId());

        order.addItem(
                new ProductId(),
                new ProductName("Mouse pad"),
                new Money("100"),
                new Quantity(1)
        );

        Assertions.assertThat(order.items()).isNotEmpty();
        Assertions.assertThat(order.items()).hasSize(1);

        OrderItem orderItem = order.items().iterator().next();

        Assertions.assertWith(orderItem,
                item -> Assertions.assertThat(item.id()).isNotNull()
        );
    }

    @Test
    void shouldGenerateExceptionWhenTryChangeItemsSet() {

        Order order = Order.draft(new CustomerId());
        order.addItem(
                new ProductId(),
                new ProductName("Mouse pad"),
                new Money("100"),
                new Quantity(1)
        );

        Set<OrderItem> items = order.items();

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(items::clear);
    }

    @Test
    void shouldCalculateTotals() {

        Order order = Order.draft(new CustomerId());
        order.addItem(
                new ProductId(),
                new ProductName("Mouse pad"),
                new Money("100"),
                new Quantity(1)
        );

        order.addItem(
                new ProductId(),
                new ProductName("RAM Memory"),
                new Money("50"),
                new Quantity(2)
        );

        Set<OrderItem> items = order.items();

        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("200"));
    }

    @Test
    void givenDraftOrderWhenPlaceShouldChangeToPlaced() {
        Order order = Order.draft(new CustomerId());
        order.place();

        Assertions.assertThat(order.isPlaced()).isTrue();
    }

    @Test
    void givenDraftOrderWhenTryToPlaceShouldGenerateException() {
        Order order = Order.draft(new CustomerId());
        order.place();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::place);
    }
}