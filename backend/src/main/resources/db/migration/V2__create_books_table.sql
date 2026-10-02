CREATE TABLE books (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(200) NOT NULL,
                       author VARCHAR(100) NOT NULL,
                       isbn VARCHAR(13) NOT NULL UNIQUE,
                       price_amount NUMERIC(12, 2) NOT NULL,
                       price_currency VARCHAR(3) NOT NULL,
                       status VARCHAR(20) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_author ON books(author);
CREATE INDEX idx_books_status ON books(status);