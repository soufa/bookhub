package com.bookhub.book;

import com.bookhub.shared.domain.Isbn;
import com.bookhub.shared.domain.Money;

/***
 * dans un dto je garde seulement les champs dont tu as besoin pour transferer les donnes ,
 * ensuite tu convertie ton entité en dto avant de l'envoyer vers le client
 * utilise que pour les api et les services , pas de logique metier dans le dto ,
 * elle sert a transporter des donnes
 * on evite d'exposer diretcemnt test entites , on evite les couplages inutils
 * @param id
 * @param title
 * @param author
 * @param isbn
 * @param price
 * @param status
 */
public record BookDto(
        Long id,
        String title,
        String author,
        Isbn isbn,
        Money price,
        BookStatus status
) {
    public BookDto {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title required");
        }
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author required");
        }
    }
}