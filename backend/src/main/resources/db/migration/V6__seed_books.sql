INSERT INTO books (title, author, isbn, price_amount, price_currency, status, created_at)
VALUES
    ('Effective Java', 'Joshua Bloch', '9780134685991', 45.00, 'EUR', 'AVAILABLE', NOW()),
    ('Clean Code', 'Robert Martin', '9780132350884', 38.00, 'EUR', 'AVAILABLE', NOW()),
    ('Java Concurrency in Practice', 'Brian Goetz', '9780321349606', 52.00, 'EUR', 'AVAILABLE', NOW()),
    ('Refactoring', 'Martin Fowler', '9780134757599', 48.00, 'EUR', 'AVAILABLE', NOW()),
    ('Domain-Driven Design', 'Eric Evans', '9780321125217', 55.00, 'EUR', 'AVAILABLE', NOW()),
    ('The Pragmatic Programmer', 'Andrew Hunt', '9780135957059', 42.00, 'EUR', 'AVAILABLE', NOW())
    ON CONFLICT (isbn) DO NOTHING;