package com.bookhub.batch.dto;

import java.math.BigDecimal;

/**
 * DTO représentant une ligne du CSV books-import.csv.
 * Champs mutables (pas record) car Spring Batch utilise les setters
 * du FlatFileItemReader pour peupler l'objet.
 */
public class BookCsvRecord {

    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String currency;

    public BookCsvRecord() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    @Override
    public String toString() {
        return "BookCsvRecord{title='%s', isbn='%s'}".formatted(title, isbn);
    }
}