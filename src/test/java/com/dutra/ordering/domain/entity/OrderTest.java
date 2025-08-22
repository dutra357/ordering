package com.dutra.ordering.domain.entity;

import com.dutra.ordering.domain.entity.builder.OrderTestDataBuilder;
import com.dutra.ordering.domain.entity.enums.OrderStatus;
import com.dutra.ordering.domain.entity.enums.PaymentMethods;
import com.dutra.ordering.domain.exceptions.OrderInvalidShippingDeliveryDateException;
import com.dutra.ordering.domain.exceptions.OrderStatusCannotBeChangedException;
import com.dutra.ordering.domain.valueobjects.*;
import com.dutra.ordering.domain.valueobjects.id.CustomerId;
import com.dutra.ordering.domain.valueobjects.id.OrderItemId;
import com.dutra.ordering.domain.valueobjects.id.ProductId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;

import java.time.LocalDate;
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
        Order order = OrderTestDataBuilder.anOrder().build();
        order.place();

        Assertions.assertThat(order.isPlaced()).isTrue();
    }

    @Test
    void givenDraftOrderWhenTryToPlaceShouldGenerateException() {
        Order order = OrderTestDataBuilder.anOrder().setOrderStatus(OrderStatus.PLACED).build();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::place);
    }

    @Test
    void givenDraftOrderWhenChangePaymentShouldAllowChange() {
        Order order = Order.draft(new CustomerId());
        order.changePaymentMethod(PaymentMethods.CREDIT_CARD);

        Assertions.assertWith(order.paymentMethod()).isEqualTo(PaymentMethods.CREDIT_CARD);
    }

    @Test
    void givenDraftOrderWhenChangeBillingInfoShouldAllowChange() {
        Address address = new Address(
                "Rua de Teste",
                "100",
                null,
                "São José",
                "Pantanal",
                "SC",
                new ZipCode("00000000")
        );

        BillingInfo billingInfo = new BillingInfo(
                new FullName("Fulano", "de Tal"),
                new Document("xxxxxx"),
                new Phone("098765432"),
                address
        );

        BillingInfo expectedBillingInfo = new BillingInfo(
                new FullName("Fulano", "de Tal"),
                new Document("xxxxxx"),
                new Phone("098765432"),
                address
        );

        Order order = Order.draft(new CustomerId());
        order.changeBillingInfo(billingInfo);

        Assertions.assertThat(order.billing()).isEqualTo(expectedBillingInfo);
    }

    @Test
    void givenDraftOrderWhenChangeShippingInfoShouldAllowChange() {
        Address address = new Address(
                "Rua de Teste",
                "100",
                null,
                "São José",
                "Pantanal",
                "SC",
                new ZipCode("00000000")
        );

        ShippingInfo shippingInfo = new ShippingInfo(
                new FullName("Fulano", "de Tal"),
                new Document("xxxxxx"),
                new Phone("098765432"),
                address
        );

        ShippingInfo expectedShippingInfo = new ShippingInfo(
                new FullName("Fulano", "de Tal"),
                new Document("xxxxxx"),
                new Phone("098765432"),
                address
        );

        Order order = Order.draft(new CustomerId());
        Money shippingCost = Money.ZERO;
        LocalDate expectedDelivery = LocalDate.now().plusDays(5);

        order.changeShipping(shippingInfo, shippingCost, expectedDelivery);

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.shipping()).isEqualTo(expectedShippingInfo),
                o -> Assertions.assertThat(o.shippingCost()).isEqualTo(shippingCost),
                o -> Assertions.assertThat(o.expectedDeliveryDate()).isEqualTo(expectedDelivery)
                );

        Assertions.assertThat(order.shipping()).isEqualTo(expectedShippingInfo);
    }

    @Test
    void givenDraftOrderWhenChangeShippingInfoWithpastDateShouldNotAllowChange() {
        Address address = new Address(
                "Rua de Teste",
                "100",
                null,
                "São José",
                "Pantanal",
                "SC",
                new ZipCode("00000000")
        );

        ShippingInfo shippingInfo = new ShippingInfo(
                new FullName("Fulano", "de Tal"),
                new Document("xxxxxx"),
                new Phone("098765432"),
                address
        );

        ShippingInfo expectedShippingInfo = new ShippingInfo(
                new FullName("Fulano", "de Tal"),
                new Document("xxxxxx"),
                new Phone("098765432"),
                address
        );

        Order order = Order.draft(new CustomerId());
        Money shippingCost = Money.ZERO;
        LocalDate expectedDelivery = LocalDate.now().minusDays(5);

        Assertions.assertThatExceptionOfType(OrderInvalidShippingDeliveryDateException.class)
                .isThrownBy(() -> order.changeShipping(shippingInfo, shippingCost, expectedDelivery));
    }

    @Test
    void givenPlacedorderWhenMarkedAsPaidShouldChangeToPaid() {
        Order order = OrderTestDataBuilder.anOrder().setOrderStatus(OrderStatus.PLACED).build();
        order.markAsPaid();

        Assertions.assertThat(order.isPaid()).isTrue();
        Assertions.assertThat(order.paidAt()).isNotNull();
    }
}