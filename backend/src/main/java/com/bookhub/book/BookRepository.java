package com.bookhub.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data génère automatiquement les implémentations — pas de code SQL à écrire.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    List<Book> findByStatus(BookStatus status);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByTitleContainingIgnoreCase(String title);

    Page<Book> findByStatus(BookStatus status, Pageable pageable);

    /**
     * Solution 1 : @EntityGraph + @Query explicite
     * Sans @Query, Spring Data essaie de parser "findAllWithAuthor" comme
     * une requête dérivée (findAllWith + Author) → erreur.
     */
    @EntityGraph(attributePaths = "authorEntity")
    @Query("SELECT b FROM Book b")
    Page<Book> findAllWithAuthor(Pageable pageable);

    /**
     * Solution 2 : JOIN FETCH en JPQL — charge la relation en une requête.
     * Ne fonctionne PAS avec Pageable (Hibernate avertit "firstResult/maxResults specified with collection fetch").
     */
    @Query("SELECT b FROM Book b JOIN FETCH b.authorEntity WHERE b.authorEntity IS NOT NULL")
    List<Book> findAllWithAuthorFetchJoin();

    /**
     * Sans optimisation — permet de reproduire le N+1.
     */
    @Query("SELECT b FROM Book b WHERE b.authorEntity IS NOT NULL")
    List<Book> findAllWithoutFetch();
}