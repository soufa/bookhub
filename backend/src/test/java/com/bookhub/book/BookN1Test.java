package com.bookhub.book;

import com.bookhub.author.Author;
import com.bookhub.author.AuthorRepository;
import com.bookhub.web.dto.BookWithAuthorDto;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManagerFactory;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class BookN1Test {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private EntityManagerFactory emf;

    private Statistics stats;

    @BeforeEach
    void setUp() {
        stats = emf.unwrap(SessionFactory.class).getStatistics();
        stats.setStatisticsEnabled(true);
        stats.clear();

        // Créer 3 auteurs + 5 livres
        Author joshua = authorRepository.save(new Author("Joshua Bloch", "US", 1961));
        Author robert = authorRepository.save(new Author("Robert Martin", "US", 1952));
        Author brian = authorRepository.save(new Author("Brian Goetz", "US", 1964));

        Book b1 = new Book("Effective Java", "Joshua Bloch", "9780134685991",
                new BigDecimal("45.00"), "EUR", BookStatus.AVAILABLE);
        b1.setAuthorEntity(joshua);
        bookRepository.save(b1);

        Book b2 = new Book("Clean Code", "Robert Martin", "9780132350884",
                new BigDecimal("40.00"), "EUR", BookStatus.AVAILABLE);
        b2.setAuthorEntity(robert);
        bookRepository.save(b2);

        Book b3 = new Book("Clean Architecture", "Robert Martin", "9780134494166",
                new BigDecimal("42.00"), "EUR", BookStatus.AVAILABLE);
        b3.setAuthorEntity(robert);
        bookRepository.save(b3);

        Book b4 = new Book("Java Concurrency", "Brian Goetz", "9780321349606",
                new BigDecimal("55.00"), "EUR", BookStatus.AVAILABLE);
        b4.setAuthorEntity(brian);
        bookRepository.save(b4);

        // Après (ISBN unique)
        Book b5 = new Book("Effective Java 3rd", "Joshua Bloch", "9780134685992",
                new BigDecimal("50.00"), "EUR", BookStatus.AVAILABLE);
        b5.setAuthorEntity(joshua);
        bookRepository.save(b5);

        em.flush();
        em.clear();
        stats.clear();
    }

    @Test
    void without_fetch_should_trigger_n_plus_1() {
        // Charge tous les livres sans optim
        List<Book> books = bookRepository.findAllWithoutFetch();

        // Accède à authorEntity pour chaque livre → N requêtes supplémentaires
        books.forEach(b -> {
            if (b.getAuthorEntity() != null) {
                b.getAuthorEntity().getName();
            }
        });

        long queryCount = stats.getPrepareStatementCount();
        System.out.println(">>> Sans fetch : " + queryCount + " requêtes");
        assertTrue(queryCount >= 2,
                "N+1 partiel : " + queryCount + " requêtes (>1 = N+1 détecté)");
    }

    @Test
    void with_entity_graph_should_use_single_query() {
        // @EntityGraph charge authorEntity en une seule requête
        var page = bookRepository.findAllWithAuthor(PageRequest.of(0, 10));

        page.getContent().forEach(b -> {
            if (b.getAuthorEntity() != null) {
                b.getAuthorEntity().getName();
            }
        });

        long queryCount = stats.getPrepareStatementCount();
        System.out.println(">>> @EntityGraph : " + queryCount + " requêtes");
        // Idéalement 1, mais Hibernate peut faire 2 (books + count pour Page)
        assertTrue(queryCount <= 2,
                "@EntityGraph devrait faire 1-2 requêtes, mais " + queryCount + " observées");
    }

    @Test
    void with_join_fetch_should_use_single_query() {
        List<Book> books = bookRepository.findAllWithAuthorFetchJoin();

        books.forEach(b -> {
            if (b.getAuthorEntity() != null) {
                b.getAuthorEntity().getName();
            }
        });

        long queryCount = stats.getPrepareStatementCount();
        System.out.println(">>> JOIN FETCH : " + queryCount + " requêtes");
        assertEquals(1, queryCount,
                "JOIN FETCH devrait faire 1 seule requête, " + queryCount + " observées");
    }
}