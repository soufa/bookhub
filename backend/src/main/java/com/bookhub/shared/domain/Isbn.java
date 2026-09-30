package com.bookhub.shared.domain;

import java.util.Objects;

/**
 * Value Object représentant un ISBN.
 *
 * <p>Deux objets ISBN de même valeur sont égaux (égalité structurelle).
 * Un Value Object n'a pas d'identité propre — il est défini par sa valeur.
 * À ne pas confondre avec une Entity (qui aurait un identifiant stable).</p>
 *
 * <p>Record = immuable par défaut. Les accesseurs, equals et hashCode
 * sont générés automatiquement par le compilateur.</p>
 */
public record Isbn(String value) {

    /**
     * Compact constructor : appelé avant l'affectation automatique du champ.
     * Permet la validation et la normalisation.
     */
    public Isbn {
        Objects.requireNonNull(value, "ISBN required");

        // Normalisation : retire tirets et espaces
        String cleaned = value.replaceAll("[-\\s]", "");

        // Validation : ISBN-10 ou ISBN-13
        if (!cleaned.matches("\\d{10}|\\d{13}")) {
            throw new IllegalArgumentException("Invalid ISBN: " + value);
        }

        value = cleaned;
    }
}