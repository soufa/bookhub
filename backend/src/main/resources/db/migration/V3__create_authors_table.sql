CREATE TABLE authors (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(150) NOT NULL,
                         nationality VARCHAR(50),
                         birth_year INTEGER,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_authors_name ON authors(name);

ALTER TABLE books ADD COLUMN author_id BIGINT;
ALTER TABLE books ADD CONSTRAINT fk_books_author
    FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE SET NULL;