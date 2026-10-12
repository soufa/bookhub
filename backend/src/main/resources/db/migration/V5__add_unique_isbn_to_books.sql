-- Supprimer les doublons existants (garde le plus petit id)
DELETE FROM books b1
    USING books b2
WHERE b1.id > b2.id
  AND b1.isbn = b2.isbn;

-- Ajouter la contrainte
ALTER TABLE books
    ADD CONSTRAINT books_isbn_unique UNIQUE (isbn);