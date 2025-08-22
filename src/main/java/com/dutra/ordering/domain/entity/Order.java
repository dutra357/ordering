package com.dutra.ordering.domain.entity;

import com.dutra.ordering.domain.entity.enums.OrderStatus;
import com.dutra.ordering.domain.entity.enums.PaymentMethods;
import com.dutra.ordering.domain.exceptions.OrderStatusCannotBeChangedException;
import com.dutra.ordering.domain.valueobjects.*;
import com.dutra.ordering.domain.valueobjects.id.CustomerId;
import com.dutra.ordering.domain.valueobjects.id.OrderId;
import com.dutra.ordering.domain.valueobjects.id.ProductId;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Order {

    private OrderId id;
    private CustomerId customerId;

    private Money totalAmount;
    private Quantity totalItems;

    private OffsetDateTime placedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime readyAt;

    private BillingInfo billing;
    private ShippingInfo shipping;

    private OrderStatus orderStatus;
    private PaymentMethods paymentMethod;

    private Money shippingCost;

    private LocalDate expectedDeliveryDate;

    private Set<OrderItem> items = new HashSet<>();

    @Builder(builderClassName = "ExistingOrderBuilder", builderMethodName = "existing")
    public Order(OrderId id, CustomerId customerId, Money totalAmount,
                 Quantity totalItems, OffsetDateTime placedAt, OffsetDateTime paidAt,
                 OffsetDateTime canceledAt, OffsetDateTime readyAt, BillingInfo billing,
                 ShippingInfo shipping, OrderStatus orderStatus, PaymentMethods paymentMethod,
                 Money shippingCost, LocalDate expectedDeliveryDate, Set<OrderItem> items) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setTotalAmount(totalAmount);
        this.setTotalItems(totalItems);
        this.setPlacedAt(placedAt);
        this.setPaidAt(paidAt);
        this.setCanceledAt(canceledAt);
        this.setReadyAt(readyAt);
        this.setBilling(billing);
        this.setShipping(shipping);
        this.setOrderStatus(orderStatus);
        this.setPaymentMethod(paymentMethod);
        this.setShippingCost(shippingCost);
        this.setExpectedDeliveryDate(expectedDeliveryDate);
        this.setItems(items);
    }

    // Factory
    public static Order draft(CustomerId customerId) {
        return new Order(
                new OrderId(),
                customerId,
                Money.ZERO,
                Quantity.ZERO,
                null,
                null,
                null,
                null,
                null,
                null,
                OrderStatus.DRAFT,
                null,
                null,
                null,
                new HashSet<>()
        );
    }

    // Comportamentos
    public void addItem(ProductId productId,
                        ProductName productName, Money price, Quantity quantity) {

        OrderItem orderItem = OrderItem.brandNew()
                .orderId(this.id)
                .price(price)
                .quantity(quantity)
                .productName(productName)
                .productId(productId)
                .build();

        if (this.items.isEmpty()) {
            this.items = new HashSet<>();
        }

        this.items.add(orderItem);

        this.recalculateTotals();
    }

    public void place() {

        //TODO Business rules!

        this.changeStatus(OrderStatus.PLACED);
    }

    public boolean isDraft() {
        return OrderStatus.DRAFT.equals(this.orderStatus);
    }

    public boolean isPlaced() {
        return OrderStatus.PLACED.equals(this.orderStatus);
    }

    // Métodos Auxiliares
    private void changeStatus(OrderStatus newOrderStatus) {
        Objects.requireNonNull(orderStatus);

        if (this.orderStatus().canNotChangeTo(newOrderStatus)) {
            throw new OrderStatusCannotBeChangedException(this.id, this.orderStatus, newOrderStatus);
        }

        this.setOrderStatus(newOrderStatus);
    }

    private void recalculateTotals() {
        BigDecimal totalItemsAmount = this.items.stream().map(item -> item.totalAmount().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalItemsQuantity = this.items().stream().map(item -> item.quantity().value()).reduce(0, Integer::sum);

        BigDecimal shippingCost;

        if (this.shippingCost() == null) {
            shippingCost = BigDecimal.ZERO;
        } else {
            shippingCost = this.shippingCost.value();
        }

        BigDecimal totalAmount = totalItemsAmount.add(shippingCost);

        this.setTotalAmount(new Money(totalAmount));
        this.setTotalItems(new Quantity(totalItemsQuantity));
    }

    // Getters no modo record
    public OrderId id() {
        return id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    public Quantity totalItems() {
        return totalItems;
    }

    public OffsetDateTime placedAt() {
        return placedAt;
    }

    public OffsetDateTime paidAt() {
        return paidAt;
    }

    public OffsetDateTime canceledAt() {
        return canceledAt;
    }

    public OffsetDateTime readyAt() {
        return readyAt;
    }

    public BillingInfo billing() {
        return billing;
    }

    public ShippingInfo shipping() {
        return shipping;
    }

    public OrderStatus orderStatus() {
        return orderStatus;
    }

    public PaymentMethods paymentMethod() {
        return paymentMethod;
    }

    public Money shippingCost() {
        return shippingCost;
    }

    public LocalDate expectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public Set<OrderItem> items() {
        return Collections.unmodifiableSet(this.items);
    }

    // Setters para private
    private void setId(OrderId id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    private void setCustomerId(CustomerId customerId) {
        Objects.requireNonNull(customerId);
        this.customerId = customerId;
    }

    private void setTotalAmount(Money totalAmount) {
        Objects.requireNonNull(totalAmount);
        this.totalAmount = totalAmount;
    }

    private void setTotalItems(Quantity totalItems) {
        Objects.requireNonNull(totalItems);
        this.totalItems = totalItems;
    }

    private void setPlacedAt(OffsetDateTime placedAt) {
        this.placedAt = placedAt;
    }

    private void setPaidAt(OffsetDateTime paidAt) {
        this.paidAt = paidAt;
    }

    private void setCanceledAt(OffsetDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    private void setReadyAt(OffsetDateTime readyAt) {
        this.readyAt = readyAt;
    }

    private void setBilling(BillingInfo billing) {
        this.billing = billing;
    }

    private void setShipping(ShippingInfo shipping) {
        this.shipping = shipping;
    }

    private void setOrderStatus(OrderStatus orderStatus) {
        Objects.requireNonNull(orderStatus);
        this.orderStatus = orderStatus;
    }

    private void setPaymentMethod(PaymentMethods paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    private void setShippingCost(Money shippingCost) {
        this.shippingCost = shippingCost;
    }

    private void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    private void setItems(Set<OrderItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
