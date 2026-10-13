CREATE TABLE IF NOT EXISTS book_statistics (
                                               id                  BIGSERIAL PRIMARY KEY,
                                               computed_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                               total_books         INTEGER NOT NULL,
                                               available_books     INTEGER NOT NULL,
                                               borrowed_books      INTEGER NOT NULL,
                                               average_price_eur   NUMERIC(12, 2),
    top_author          VARCHAR(100),
    top_author_count    INTEGER
    );

CREATE INDEX IF NOT EXISTS idx_book_statistics_computed_at
    ON book_statistics(computed_at DESC);