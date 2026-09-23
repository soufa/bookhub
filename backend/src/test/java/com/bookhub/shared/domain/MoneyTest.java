package com.bookhub.shared.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    public void should_create_from_strings() {
        Money m = Money.of("19.99", "EUR");
        assertEquals(new BigDecimal("19.99"), m.amount());
        assertEquals(Currency.getInstance("EUR"), m.currency());
    }

    @Test
    public void should_add_same_currency() {
        Money a = Money.of("10.00", "EUR");
        Money b = Money.of("5.50", "EUR");
        Money sum = a.add(b);
        assertEquals(new BigDecimal("15.50"), sum.amount());
    }

    @Test
    public void should_reject_different_currencies() {
        Money eur = Money.of("10.00", "EUR");
        Money usd = Money.of("10.00", "USD");
        assertThrows(IllegalArgumentException.class, () -> eur.add(usd));
    }

    @Test
    public void should_reject_more_than_2_decimals() {
        assertThrows(IllegalArgumentException.class,
                () -> new Money(new BigDecimal("10.123"), Currency.getInstance("EUR")));
    }

    @Test
    public void should_reject_null() {
        assertThrows(NullPointerException.class,
                () -> new Money(null, Currency.getInstance("EUR")));
    }
}