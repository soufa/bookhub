-- Seed initial des livres pour le portfolio BookHub.
-- Utilise ON CONFLICT pour ne pas échouer si les livres existent déjà.

INSERT INTO books (title, author, isbn, price_amount, price_currency, status, created_at, updated_at)
VALUES
    ('Effective Java', 'Joshua Bloch', '9780134685991', 45.00, 'EUR', 'AVAILABLE', NOW(), NOW()),
    ('Clean Code', 'Robert Martin', '9780132350884', 38.00, 'EUR', 'AVAILABLE', NOW(), NOW()),
    ('Java Concurrency in Practice', 'Brian Goetz', '9780321349606', 52.00, 'EUR', 'AVAILABLE', NOW(), NOW()),
    ('Refactoring', 'Martin Fowler', '9780134757599', 48.00, 'EUR', 'AVAILABLE', NOW(), NOW()),
    ('Domain-Driven Design', 'Eric Evans', '9780321125217', 55.00, 'EUR', 'AVAILABLE', NOW(), NOW()),
    ('The Pragmatic Programmer', 'Andrew Hunt', '9780135957059', 42.00, 'EUR', 'AVAILABLE', NOW(), NOW())
    ON CONFLICT (isbn) DO NOTHING;