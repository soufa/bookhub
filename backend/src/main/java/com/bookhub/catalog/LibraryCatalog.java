package com.bookhub.catalog;

import com.bookhub.book.BookDto;
import com.bookhub.book.BookStatus;
import com.bookhub.shared.domain.Isbn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class LibraryCatalog {

    private final Map<Isbn, BookDto> books = new HashMap<>();

    /***
     * O(1) signifie complexité constante meme si tu as plein livre , la recherche ca veut etre tres rapide , un livre sans parcourir toute la collection,
     * O(n) signifie complexité qui varie ,
     * je choisie la structure en ficntion du besoin fonctionnelle ,
     * @param book
     */
    /***
     * dans HashMap si tu ajoute deux livre avec le meme isbn , le deuxieme remplace le prmeier ,
     * validation avec containsKey , c'est a dire si existe la cle je ne l'ajoute pas avnat le put
     * @param book
     */
    public void add(BookDto book) {
        Objects.requireNonNull(book, "Book required");
        books.put(book.isbn(), book);
    }

    /***
     * Pourquoi findByStatus() retourne List et pas Optional ?
     * Optional BookDTO c'est 0 ou plusieurs livre ,
     * Optional quand il ya 0 ou 1 resultat , list c'est quand nous avons  plusiuers resultat
     * @param isbn
     * @return
     */
    /***
     * Map est une inteface cle , value et hashmap c'est une implementation de Map
     * @param isbn
     * @return
     */
    /***
     * Pourquoi EnumMap est-elle plus rapide qu'une HashMap<Enum, V> ?
     *
     * @param isbn
     * @return
     */
    public Optional<BookDto> findByIsbn(Isbn isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    public List<BookDto> findByStatus(BookStatus status) {
        return books.values().stream()
                .filter(b -> b.status() == status)
                .toList();
    }

    public List<BookDto> findAll() {
        return new ArrayList<>(books.values());
    }

    public int size() {
        return books.size();
    }

    public boolean remove(Isbn isbn) {
        return books.remove(isbn) != null;
    }
}