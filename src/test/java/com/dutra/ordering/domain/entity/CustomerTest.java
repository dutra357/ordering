package com.dutra.ordering.domain.entity;

import com.dutra.ordering.domain.exceptions.CustomerArchivedException;
import com.dutra.ordering.domain.valueobjects.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CustomerTest {

    @Test
    void shouldCreateCustomerWithValidData() {
        FullName fullName = new FullName("João", "da Silva");
        BirthDate birthDate = new BirthDate(LocalDate.of(1990, 5, 20));
        Email email = new Email("joao@email.com");
        Phone phone = new Phone("48999999999");
        Document document = new Document("12345678900");
        Boolean promoAllowed = true;
        Address address = new Address("Rua Fulano de Tal", "100",
                null, "Pantanal", "Florianópolis", "SC", new ZipCode("00000000"));

        Customer customer = Customer.brandNew(fullName, birthDate, email, phone, document, promoAllowed, address);

        assertThat(customer.fullName()).isEqualTo(fullName);
        assertThat(customer.birthDate()).isEqualTo(birthDate);
        assertThat(customer.email()).isEqualTo(email);
        assertThat(customer.phone()).isEqualTo(phone);
        assertThat(customer.document()).isEqualTo(document);
        assertThat(customer.isPromotionNotificationsAllowed()).isTrue();
        assertThat(customer.isArchived()).isFalse();
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        BirthDate birthDate = new BirthDate(LocalDate.of(1990, 5, 20));
        Email email = new Email("joao@email.com");
        Phone phone = new Phone("48999999999");
        Document document = new Document("12345678900");
        Boolean promoAllowed = true;
        Address address = new Address("Rua Fulano de Tal", "100",
                null, "Pantanal", "Florianópolis", "SC", new ZipCode("00000000"));


        assertThatThrownBy(() -> Customer.brandNew(new FullName(" ", "da Silva"), birthDate, email, phone, document, promoAllowed, address))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("First name is blank.");
    }

    @Test
    void shouldThrowExceptionWhenBirthDateIsInFuture() {
        FullName fullName = new FullName("João", "da Silva");
        Email email = new Email("joao@email.com");
        Phone phone = new Phone("48999999999");
        Document document = new Document("12345678900");
        Boolean promoAllowed = true;
        Address address = new Address("Rua Fulano de Tal", "100",
                null, "Pantanal", "Florianópolis", "SC", new ZipCode("00000000"));


        assertThatThrownBy(() -> Customer.brandNew(fullName, new BirthDate(LocalDate.of(2990, 5, 20)), email, phone, document, promoAllowed, address))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Data de nascimento não está no passado.");
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        FullName fullName = new FullName("João", "da Silva");
        BirthDate birthDate = new BirthDate(LocalDate.of(1990, 5, 20));
        Phone phone = new Phone("48999999999");
        Document document = new Document("12345678900");
        Boolean promoAllowed = true;
        Address address = new Address("Rua Fulano de Tal", "100",
                null, "Pantanal", "Florianópolis", "SC", new ZipCode("00000000"));


        assertThatThrownBy(() -> Customer.brandNew(fullName, birthDate, new Email("joaoemail.com"), phone, document, promoAllowed, address))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email is invalid.");
    }

    @Test
    void shouldCreateCustomerWithAllFields() {
        FullName fullName = new FullName("João", "da Silva");
        BirthDate birthDate = new BirthDate(LocalDate.of(1990, 5, 20));
        Email email = new Email("joao@email.com");
        Phone phone = new Phone("48999999999");
        Document document = new Document("12345678900");
        Boolean promoAllowed = true;
        Address address = new Address("Rua Fulano de Tal", "100",
                null, "Pantanal", "Florianópolis", "SC", new ZipCode("00000000"));

        Customer customer = Customer.brandNew(fullName, birthDate, email, phone, document, promoAllowed, address);
        customer.addLoyaltyPoints(new LoyaltyPoints(150));
        customer.diseablePromotionNotifications();

        assertThat(customer.fullName()).isEqualTo(fullName);
        assertThat(customer.birthDate()).isEqualTo(birthDate);
        assertThat(customer.email()).isEqualTo(email);
        assertThat(customer.phone()).isEqualTo(phone);
        assertThat(customer.document()).isEqualTo(document);
        assertThat(customer.isPromotionNotificationsAllowed()).isFalse();
        assertThat(customer.isArchived()).isFalse();
        assertThat(customer.loyaltyPoints().points()).isEqualTo(150);
    }

    @Test
    void given_unarchivedCustomer_whenArchive_ShouldAnonymize() {
        FullName fullName = new FullName("João", "da Silva");
        BirthDate birthDate = new BirthDate(LocalDate.of(1990, 5, 20));
        Email email = new Email("joao@email.com");
        Phone phone = new Phone("48999999999");
        Document document = new Document("12345678900");
        Boolean promoAllowed = true;
        Address address = new Address("Rua Fulano de Tal", "100",
                null, "Pantanal", "Florianópolis", "SC", new ZipCode("00000000"));

        Customer customer = Customer.brandNew(fullName, birthDate, email, phone, document, promoAllowed, address);

        customer.archive();

        Assertions.assertWith(customer,
                c -> Assertions.assertThat(c.fullName().equals("Anonymous")));
    }

    @Test
    void given_archivedCustomer_whenTryArchive_ShouldGenerateException() {
        FullName fullName = new FullName("João", "da Silva");
        BirthDate birthDate = new BirthDate(LocalDate.of(1990, 5, 20));
        Email email = new Email("joao@email.com");
        Phone phone = new Phone("48999999999");
        Document document = new Document("12345678900");
        Boolean promoAllowed = true;
        Address address = new Address("Rua Fulano de Tal", "100",
                null, "Pantanal", "Florianópolis", "SC", new ZipCode("00000000"));

        Customer customer = Customer.brandNew(fullName, birthDate, email, phone, document, promoAllowed, address);

        customer.archive();

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::archive);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeEmail(new Email("novo@email.com")));
    }

    @Test
    void given_brandNewCustomer_whenAddLoialtyPoints_ShouldSumPoints() {
        FullName fullName = new FullName("João", "da Silva");
        BirthDate birthDate = new BirthDate(LocalDate.of(1990, 5, 20));
        Email email = new Email("joao@email.com");
        Phone phone = new Phone("48999999999");
        Document document = new Document("12345678900");
        Boolean promoAllowed = true;
        Address address = new Address("Rua Fulano de Tal", "100",
                null, "Pantanal", "Florianópolis", "SC", new ZipCode("00000000"));

        Customer customer = Customer.brandNew(fullName, birthDate, email, phone, document, promoAllowed, address);

        customer.addLoyaltyPoints(new LoyaltyPoints(10));
        customer.addLoyaltyPoints(new LoyaltyPoints(21));

        Assertions.assertThat(customer.loyaltyPoints().equals(31));
    }

    @Test
    void given_brandNewCustomer_whenAddInvalidPoints_ShouldThrowException() {
        FullName fullName = new FullName("João", "da Silva");
        BirthDate birthDate = new BirthDate(LocalDate.of(1990, 5, 20));
        Email email = new Email("joao@email.com");
        Phone phone = new Phone("48999999999");
        Document document = new Document("12345678900");
        Boolean promoAllowed = true;
        Address address = new Address("Rua Fulano de Tal", "100",
                null, "Pantanal", "Florianópolis", "SC", new ZipCode("00000000"));

        Customer customer = Customer.brandNew(fullName, birthDate, email, phone, document, promoAllowed, address);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(-1)));
    }
}
