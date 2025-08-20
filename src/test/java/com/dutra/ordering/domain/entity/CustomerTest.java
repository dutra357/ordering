package com.dutra.ordering.domain.entity;

import com.dutra.ordering.domain.exceptions.CustomerArchivedException;
import com.dutra.ordering.domain.valueobjects.CustomerId;
import com.dutra.ordering.domain.valueobjects.FullName;
import com.dutra.ordering.domain.valueobjects.LoyaltyPoints;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CustomerTest {

    @Test
    void shouldCreateCustomerWithValidData() {
        CustomerId id = new CustomerId();
        FullName name = new FullName("João", "da Silva");
        LocalDate birthDate = LocalDate.of(1990, 5, 20);
        String email = "joao@email.com";
        String phone = "48999999999";
        String document = "12345678900";
        Boolean promoAllowed = true;
        OffsetDateTime registeredAt = OffsetDateTime.now();

        Customer customer = new Customer(id, name, birthDate, email, phone, document, promoAllowed, registeredAt);

        assertThat(customer.id()).isEqualTo(id);
        assertThat(customer.fullName()).isEqualTo(name);
        assertThat(customer.birthDate()).isEqualTo(birthDate);
        assertThat(customer.email()).isEqualTo(email);
        assertThat(customer.phone()).isEqualTo(phone);
        assertThat(customer.document()).isEqualTo(document);
        assertThat(customer.isPromotionNotificationsAllowed()).isTrue();
        assertThat(customer.isArchived()).isFalse();
        assertThat(customer.registeredAt()).isEqualTo(registeredAt);
    }

    @Test
    void shouldThrowExceptionWhenFullNameIsBlank() {
        assertThatThrownBy(() -> new Customer(
                new CustomerId(), new FullName(" ", " "), LocalDate.of(1990, 1, 1),
                "email@email.com", "48999999999", "12345678900",
                true, OffsetDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("First name is blank.");
    }

    @Test
    void shouldThrowExceptionWhenBirthDateIsInFuture() {
        assertThatThrownBy(() -> new Customer(
                new CustomerId(), new FullName("Maria", "da Silva"), LocalDate.now().plusDays(1),
                "email@email.com", "48999999999", "12345678900",
                true, OffsetDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Data de nascimento não está no passado.");
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        assertThatThrownBy(() -> new Customer(
                new CustomerId(), new FullName("Carlos", "da Silva"), LocalDate.of(1990, 1, 1),
                "invalid-email", "48999999999", "12345678900",
                true, OffsetDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email is invalid.");
    }

    @Test
    void shouldCreateCustomerWithAllFields() {
        CustomerId id = new CustomerId();
        FullName name = new FullName("João", "da Silva");
        LocalDate birthDate = LocalDate.of(1990, 5, 20);
        String email = "joao@email.com";
        String phone = "48999999999";
        String document = "12345678900";
        Boolean promoAllowed = false;
        OffsetDateTime registeredAt = OffsetDateTime.now();
        Boolean archived = false;
        OffsetDateTime archivedAt = OffsetDateTime.now();
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(150);

        Customer customer = new Customer(id, name, birthDate, email, phone, document,
                promoAllowed, archived, registeredAt, archivedAt, loyaltyPoints);

        assertThat(customer.id()).isEqualTo(id);
        assertThat(customer.fullName()).isEqualTo(name);
        assertThat(customer.birthDate()).isEqualTo(birthDate);
        assertThat(customer.email()).isEqualTo(email);
        assertThat(customer.phone()).isEqualTo(phone);
        assertThat(customer.document()).isEqualTo(document);
        assertThat(customer.isPromotionNotificationsAllowed()).isFalse();
        assertThat(customer.isArchived()).isFalse();
        assertThat(customer.registeredAt()).isEqualTo(registeredAt);
        assertThat(customer.archivedAt()).isEqualTo(archivedAt);
        assertThat(customer.loyaltyPoints()).isEqualTo(loyaltyPoints);
    }

    @Test
    void given_unarchivedCustomer_whenArchive_ShouldAnonymize() {
        CustomerId id = new CustomerId();
        FullName name = new FullName("João", "da Silva");
        LocalDate birthDate = LocalDate.of(1990, 5, 20);
        String email = "joao@email.com";
        String phone = "48999999999";
        String document = "12345678900";
        Boolean promoAllowed = true;
        OffsetDateTime registeredAt = OffsetDateTime.now();

        Customer customer = new Customer(id, name, birthDate, email, phone, document, promoAllowed, registeredAt);

        customer.archive();

        Assertions.assertWith(customer,
                c -> Assertions.assertThat(c.fullName().equals("Anonymous")));
    }

    @Test
    void given_archivedCustomer_whenTryArchive_ShouldGenerateException() {
        CustomerId id = new CustomerId();
        FullName name = new FullName("João", "da Silva");
        LocalDate birthDate = LocalDate.of(1990, 5, 20);
        String email = "joao@email.com";
        String phone = "48999999999";
        String document = "12345678900";
        Boolean promoAllowed = true;
        OffsetDateTime registeredAt = OffsetDateTime.now();

        Customer customer = new Customer(id, name, birthDate, email, phone, document, promoAllowed, registeredAt);

        customer.archive();

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::archive);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeEmail("novo@email.com"));
    }

    @Test
    void given_brandNewCustomer_whenAddLoialtyPoints_ShouldSumPoints() {
        CustomerId id = new CustomerId();
        FullName name = new FullName("João", "da Silva");
        LocalDate birthDate = LocalDate.of(1990, 5, 20);
        String email = "joao@email.com";
        String phone = "48999999999";
        String document = "12345678900";
        Boolean promoAllowed = true;
        OffsetDateTime registeredAt = OffsetDateTime.now();

        Customer customer = new Customer(id, name, birthDate, email, phone, document, promoAllowed, registeredAt);

        customer.addLoyaltyPoints(new LoyaltyPoints(10));
        customer.addLoyaltyPoints(new LoyaltyPoints(21));

        Assertions.assertThat(customer.loyaltyPoints().equals(31));
    }

    @Test
    void given_brandNewCustomer_whenAddInvalidPoints_ShouldThrowException() {
        CustomerId id = new CustomerId();
        FullName name = new FullName("João", "da Silva");
        LocalDate birthDate = LocalDate.of(1990, 5, 20);
        String email = "joao@email.com";
        String phone = "48999999999";
        String document = "12345678900";
        Boolean promoAllowed = true;
        OffsetDateTime registeredAt = OffsetDateTime.now();

        Customer customer = new Customer(id, name, birthDate, email, phone, document, promoAllowed, registeredAt);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(-1)));
    }
}
