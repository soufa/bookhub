package com.bookhub.book;

public class BookNotFoundException extends RuntimeException {
    private final Long bookId;

    public BookNotFoundException(Long bookId) {
        super("Book not found with id: " + bookId);
        this.bookId = bookId;
    }

    public Long bookId() {
        return bookId;
    }
}
