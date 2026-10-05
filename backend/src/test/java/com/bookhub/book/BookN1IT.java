package com.bookhub.book;

import com.bookhub.AbstractIntegrationTest;
import com.bookhub.author.Author;
import com.bookhub.author.AuthorRepository;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookN1IT extends AbstractIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private EntityManagerFactory emf;

    @Test
    void should_avoid_n_plus_1_with_entity_graph_on_real_postgres() {
        // Setup : 3 auteurs, 5 livres
        Author joshua = authorRepository.save(new Author("Joshua Bloch", "US", 1961));
        Author robert = authorRepository.save(new Author("Robert Martin", "US", 1952));
        Author brian = authorRepository.save(new Author("Brian Goetz", "US", 1964));

        createBook("Effective Java", joshua, "9780134685991");
        createBook("Clean Code", robert, "9780132350884");
        createBook("Clean Architecture", robert, "9780134494166");
        createBook("Java Concurrency", brian, "9780321349606");
        createBook("Effective Java 3rd", joshua, "9780134685992");

        em.flush();
        em.clear();

        Statistics stats = emf.unwrap(SessionFactory.class).getStatistics();
        stats.setStatisticsEnabled(true);
        stats.clear();

        // Utilise @EntityGraph → 1 seule requête
        List<Book> books = bookRepository.findAllWithAuthorFetchJoin();

        books.forEach(b -> {
            if (b.getAuthorEntity() != null) {
                b.getAuthorEntity().getName();
            }
        });

        long queryCount = stats.getPrepareStatementCount();
        System.out.println(">>> PostgreSQL réel : " + queryCount + " requête(s)");

        assertEquals(1, queryCount,
                "Sur PostgreSQL réel, JOIN FETCH doit faire exactement 1 requête");
    }

    private void createBook(String title, Author author, String isbn) {
        Book b = new Book(title, author.getName(), isbn,
                new BigDecimal("45.00"), "EUR", BookStatus.AVAILABLE);
        b.setAuthorEntity(author);
        bookRepository.save(b);
    }
}