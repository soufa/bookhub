package com.bookhub.book;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entité JPA représentant un livre.
 *
 * <p>Différence avec BookDto : BookDto est immuable (record) pour le transport,
 * Book est mutable (classe) car JPA a besoin de proxies et de setters.</p>
 */

/**
 * @Entity : classe persistée
 *
 * @Table(name = "books") : mappe sur la table books
 *
 * @Id + @GeneratedValue(IDENTITY) : clé auto-générée
 *
 * @Enumerated(EnumType.STRING) : stocke "AVAILABLE" au lieu de 0
 *
 * @PrePersist / @PreUpdate : timestamps automatiques
 *
 * protected Book() : requis par JPA
 *
 * Pas de setters pour id et isbn (immuables)
 */
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false, unique = true, length = 13)
    private String isbn;

    @Column(name = "price_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal priceAmount;

    @Column(name = "price_currency", nullable = false, length = 3)
    private String priceCurrency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /** Constructeur requis par JPA (proxy). */
    protected Book() {}

    public Book(String title, String author, String isbn,
                BigDecimal priceAmount, String priceCurrency, BookStatus status) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.priceAmount = priceAmount;
        this.priceCurrency = priceCurrency;
        this.status = status;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public BigDecimal getPriceAmount() { return priceAmount; }
    public String getPriceCurrency() { return priceCurrency; }
    public BookStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    // Setters métier (pas de setter pour id et isbn)
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setStatus(BookStatus status) { this.status = status; }
    public void setPrice(BigDecimal amount, String currency) {
        this.priceAmount = amount;
        this.priceCurrency = currency;
    }
}