package com.dutra.ordering.domain.entity;

import com.dutra.ordering.domain.utility.IdGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CustomerTest {

    @Test
    void shouldCreateCustomerWithValidData() {
        UUID id = IdGenerator.generateTimeBasedUUID();
        String name = "João Silva";
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
        assertThat(customer.loyaltyPoints()).isZero();
    }

    @Test
    void shouldThrowExceptionWhenFullNameIsBlank() {
        assertThatThrownBy(() -> new Customer(
                UUID.randomUUID(), "  ", LocalDate.of(1990, 1, 1),
                "email@email.com", "48999999999", "12345678900",
                true, OffsetDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nome não pode ser nulo ou em branco.");
    }

    @Test
    void shouldThrowExceptionWhenBirthDateIsInFuture() {
        assertThatThrownBy(() -> new Customer(
                UUID.randomUUID(), "Maria", LocalDate.now().plusDays(1),
                "email@email.com", "48999999999", "12345678900",
                true, OffsetDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Data de nascimento não está no passado.");
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        assertThatThrownBy(() -> new Customer(
                UUID.randomUUID(), "Carlos", LocalDate.of(1990, 1, 1),
                "invalid-email", "48999999999", "12345678900",
                true, OffsetDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Um e-mail válido deve ser informado.");
    }

    @Test
    void shouldCreateCustomerWithAllFields() {
        UUID id = IdGenerator.generateTimeBasedUUID();
        String name = "Ana Paula";
        LocalDate birthDate = LocalDate.of(1985, 3, 15);
        String email = "ana@email.com";
        String phone = "48988888888";
        String document = "98765432100";
        Boolean promoAllowed = false;
        Boolean archived = true;
        OffsetDateTime registeredAt = OffsetDateTime.now().minusDays(10);
        OffsetDateTime archivedAt = OffsetDateTime.now();
        Integer loyaltyPoints = 150;

        Customer customer = new Customer(id, name, birthDate, email, phone, document,
                promoAllowed, archived, registeredAt, archivedAt, loyaltyPoints);

        assertThat(customer.id()).isEqualTo(id);
        assertThat(customer.fullName()).isEqualTo(name);
        assertThat(customer.birthDate()).isEqualTo(birthDate);
        assertThat(customer.email()).isEqualTo(email);
        assertThat(customer.phone()).isEqualTo(phone);
        assertThat(customer.document()).isEqualTo(document);
        assertThat(customer.isPromotionNotificationsAllowed()).isFalse();
        assertThat(customer.isArchived()).isTrue();
        assertThat(customer.registeredAt()).isEqualTo(registeredAt);
        assertThat(customer.archivedAt()).isEqualTo(archivedAt);
        assertThat(customer.loyaltyPoints()).isEqualTo(loyaltyPoints);
    }
}
