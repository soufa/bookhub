package com.bookhub.book;

public enum BookStatus {
    AVAILABLE("Available for borrowing"),
    BORROWED("Currently borrowed"),
    RESERVED("Reserved"),
    LOST("Reported lost"),
    MAINTENANCE("Under maintenance");

    private final String description;

    BookStatus(String description) {
        this.description = description;
    }

    public String description() { return description; }

    public boolean isBorrowable() {
        return this == AVAILABLE;
    }
}
