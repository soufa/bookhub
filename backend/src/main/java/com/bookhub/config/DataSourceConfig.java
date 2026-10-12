package com.bookhub.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;

/**
 * Configuration DataSource pour l'environnement cloud (Fly.io).
 * Convertit DATABASE_URL (format postgres://user:pass@host:port/db)
 * en URL JDBC + credentials que Spring Boot comprend.
 *
 * Note : la connexion réseau est déjà chiffrée via Flycast (6PN).
 * On force sslmode=disable pour éviter un handshake SSL non supporté.
 */
@Configuration
@Profile("prod")
public class DataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null || databaseUrl.isBlank()) {
            throw new IllegalStateException("DATABASE_URL environment variable is required in prod");
        }

        try {
            // Enlever les query params existants (on va les reconstruire)
            String url = databaseUrl;
            int queryIdx = url.indexOf('?');
            if (queryIdx > 0) {
                url = url.substring(0, queryIdx);
            }

            URI uri = URI.create(url);

            String userInfo = uri.getUserInfo();
            String username = null;
            String password = null;
            if (userInfo != null) {
                String[] parts = userInfo.split(":", 2);
                username = parts[0];
                password = parts.length > 1 ? parts[1] : "";
            }

            int port = uri.getPort() > 0 ? uri.getPort() : 5432;

            // ⚠️ AJOUTER ?sslmode=disable pour éviter le handshake SSL
            String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + port
                    + uri.getPath() + "?sslmode=disable";

            HikariDataSource ds = new HikariDataSource();
            ds.setJdbcUrl(jdbcUrl);
            ds.setUsername(username);
            ds.setPassword(password);
            ds.setDriverClassName("org.postgresql.Driver");
            ds.setMaximumPoolSize(5);
            ds.setMinimumIdle(1);
            ds.setConnectionTimeout(30_000);
            ds.setIdleTimeout(60_000);
            ds.setMaxLifetime(1_800_000);
            ds.setKeepaliveTime(30_000);
            ds.setInitializationFailTimeout(-1);
            return ds;
        } catch (Exception e) {
            throw new IllegalStateException("Invalid DATABASE_URL: " + e.getMessage(), e);
        }
    }
}