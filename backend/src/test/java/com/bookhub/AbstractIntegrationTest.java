package com.bookhub;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Classe de base pour les tests d'intégration.
 * Démarre UN SEUL container PostgreSQL partagé par tous les tests qui étendent cette classe.
 *
 * <p>Le container est "static" : il est démarré une fois pour toute la suite de tests.
 * Pattern singleton manuel recommandé par Testcontainers pour éviter de redémarrer
 * le container à chaque classe de test.</p>
 */
@SpringBootTest
@ActiveProfiles("it")
public abstract class AbstractIntegrationTest {

    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("bookhub_it")
                    .withUsername("bookhub")
                    .withPassword("bookhub")
                    .withReuse(true);

    static {
        POSTGRES.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }
}