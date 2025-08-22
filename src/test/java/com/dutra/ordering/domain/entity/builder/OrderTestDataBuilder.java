package com.dutra.ordering.domain.entity.builder;

import com.dutra.ordering.domain.entity.Order;
import com.dutra.ordering.domain.entity.enums.OrderStatus;
import com.dutra.ordering.domain.entity.enums.PaymentMethods;
import com.dutra.ordering.domain.valueobjects.*;
import com.dutra.ordering.domain.valueobjects.id.CustomerId;
import com.dutra.ordering.domain.valueobjects.id.ProductId;

import java.time.LocalDate;

public class OrderTestDataBuilder {

    private CustomerId customerId = new CustomerId();
    private PaymentMethods paymentMethod = PaymentMethods.GATEWAY_BALANCE;
    private Money shippingCost = new Money("10");
    private LocalDate expectedDeliveryDate = LocalDate.now().plusDays(7);
    private ShippingInfo shippingInfo = aValidShippingInfo();
    private BillingInfo billingInfo = aValidBillingInfo();

    private boolean withItems = true;
    private OrderStatus orderStatus = OrderStatus.DRAFT;

    public static ShippingInfo aValidShippingInfo() {
        return new ShippingInfo(
                new FullName("Fulano", "de Tal"),
                new Document("xxxxxx"),
                new Phone("098765432"),
                anAddress()
        );
    }

    public static BillingInfo aValidBillingInfo() {
        return  new BillingInfo(
                new FullName("Fulano", "de Tal"),
                new Document("xxxxxx"),
                new Phone("098765432"),
                anAddress()
        );
    }

    public static Address anAddress() {
        return new Address(
                "Rua de Teste",
                "100",
                null,
                "São José",
                "Pantanal",
                "SC",
                new ZipCode("00000000")
        );
    }

    private OrderTestDataBuilder() {
    }

    public static OrderTestDataBuilder anOrder() {
        return new OrderTestDataBuilder();
    }

    public Order build() {
        Order order = Order.draft(customerId);
        order.changeShipping(shippingInfo, shippingCost, expectedDeliveryDate);
        order.changeBillingInfo(billingInfo);order.changePaymentMethod(paymentMethod);

        if (withItems) {
            order.addItem(
                    new ProductId(),
                    new ProductName("IPhone X"),
                    new Money("3000"),
                    new Quantity(1)
            );

            order.addItem(
                    new ProductId(),
                    new ProductName("RAM Memory 4GB"),
                    new Money("120"),
                    new Quantity(2)
            );
        }

        switch (this.orderStatus) {
            case DRAFT -> {}
            case PLACED -> {
                order.place();
            }
            case PAID -> {
                order.place();
                order.markAsPaid();
            }
            case READY -> {

            }
            case CANCELED -> {

            }
        }

        return order;
    }

    // Setters
    public OrderTestDataBuilder setCustomerId(CustomerId customerId) {
        this.customerId = customerId;
        return this;
    }

    public OrderTestDataBuilder setPaymentMethods(PaymentMethods paymentMethods) {
        this.paymentMethod = paymentMethods;
        return this;
    }

    public OrderTestDataBuilder setShippingCost(Money shippingCost) {
        this.shippingCost = shippingCost;
        return this;
    }

    public OrderTestDataBuilder setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
        return this;
    }

    public OrderTestDataBuilder setShippingInfo(ShippingInfo shippingInfo) {
        this.shippingInfo = shippingInfo;
        return this;
    }

    public OrderTestDataBuilder setBillingInfo(BillingInfo billingInfo) {
        this.billingInfo = billingInfo;
        return this;
    }

    public OrderTestDataBuilder setWithItems(boolean withItems) {
        this.withItems = withItems;
        return this;
    }

    public OrderTestDataBuilder setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }
}
