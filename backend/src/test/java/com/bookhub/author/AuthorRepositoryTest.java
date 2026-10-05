package com.bookhub.author;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository repository;

    @Test
    void should_save_and_find_author() {
        Author saved = repository.save(new Author("Joshua Bloch", "US", 1961));
        assertNotNull(saved.getId());

        Optional<Author> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Joshua Bloch", found.get().getName());
    }

    @Test
    void should_find_by_name() {
        repository.save(new Author("Joshua Bloch", "US", 1961));
        Optional<Author> found = repository.findByName("Joshua Bloch");
        assertTrue(found.isPresent());
    }

    @Test
    void should_find_by_nationality() {
        repository.save(new Author("A", "US", 1961));
        repository.save(new Author("B", "US", 1952));
        repository.save(new Author("C", "FR", 1964));

        List<Author> usAuthors = repository.findByNationalityIgnoreCase("us");
        assertEquals(2, usAuthors.size());
    }

    @Test
    void should_find_by_name_containing() {
        repository.save(new Author("Joshua Bloch", "US", 1961));
        repository.save(new Author("Robert Martin", "US", 1952));

        List<Author> found = repository.findByNameContainingIgnoreCase("mart");
        assertEquals(1, found.size());
    }

    @Test
    void should_check_existence() {
        repository.save(new Author("Joshua Bloch", "US", 1961));

        assertTrue(repository.existsByNameAndNationality("Joshua Bloch", "US"));
        assertFalse(repository.existsByNameAndNationality("Joshua Bloch", "FR"));
    }

    @Test
    void should_set_timestamps() {
        Author saved = repository.save(new Author("Test", "FR", 2000));
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }
}