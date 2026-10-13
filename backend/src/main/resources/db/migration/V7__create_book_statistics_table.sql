CREATE TABLE book_statistics (
                                 id                  BIGSERIAL PRIMARY KEY,
                                 computed_at         TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                                 total_books         INTEGER NOT NULL,
                                 available_books     INTEGER NOT NULL,
                                 borrowed_books      INTEGER NOT NULL,
                                 average_price_eur   NUMERIC(12, 2),
                                 top_author          VARCHAR(100),
                                 top_author_count    INTEGER
);

CREATE INDEX idx_book_statistics_computed_at ON book_statistics(computed_at DESC);