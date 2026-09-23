package com.bookhub.shared.domain;

import java.util.Objects;

public record Isbn(String value){
    public Isbn{
        Objects.requireNonNull(value,"ISBN requiered");
        String cleaned= value.replaceAll("[-\\s]", "");
        if(!cleaned.matches("\\d{10}|\\d{13}")){
            throw new IllegalArgumentException("Invalid ISBN" + value);
        }
         value = cleaned;
    }
}
