package com.bookhub.shared.domain;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public record Money(BigDecimal amount, Currency currency) {
    /***
     * constructeur compact c'est le constructeur canonique , tu ecris juste tes verifications
     * et java fait automatiquement les affections des parameters aux chhmaps
     * le constructeur compact est unquement pour les records
     * @param amount
     * @param currency
     */
    /**
     * dans un record java genere getter et pas les stteurs , les equals et le hashcode
     * @param amount
     * @param currency
     */
    /**
     * les regles de validations doivent imperativement dans un contructeur compact ,
     * la validation est executé au moment de new
     * @param amount
     * @param currency
     */
    /***
     * ue record pour les dto et les value objetc et on garde les classes classqiues pour les jpa
     * @param amount
     * @param currency
     */
    public Money {
        Objects.requireNonNull(amount, "Amount required");
        Objects.requireNonNull(currency, "Currency required");
        if (amount.scale() > 2) {
            throw new IllegalArgumentException("Max 2 decimals");
        }
    }

    public static Money of(String amount, String currencyCode) {
        return new Money(new BigDecimal(amount), Currency.getInstance(currencyCode));
    }

    public Money add(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currencies differ");
        }
        return new Money(amount.add(other.amount), currency);
    }
}