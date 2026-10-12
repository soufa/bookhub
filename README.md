# BookHub

[![CI](https://github.com/soufa/bookhub/actions/workflows/ci.yml/badge.svg)](https://github.com/soufa/bookhub/actions/workflows/ci.yml)
[![CD](https://github.com/soufa/bookhub/actions/workflows/cd.yml/badge.svg)](https://github.com/soufa/bookhub/actions/workflows/cd.yml)
[![Container](https://img.shields.io/badge/ghcr.io-backend-blue)](https://github.com/soufa/bookhub/pkgs/container/bookhub%2Fbackend)
> Application full-stack de gestion de bibliothèque — Java 17 · Spring Boot 3.2 · Angular 17 · PostgreSQL · Docker

## Objectif

Projet pédagogique de reconversion Java/Spring/Angular.

## Stack

| Couche | Technologie |
|--------|-------------|
| Backend | Java 17, Spring Boot 3.2, Spring Web MVC, Spring Data JPA, Spring Security (JWT) |
| Base de données | PostgreSQL 16, Flyway |
| Frontend | Angular 17, TypeScript, RxJS, Angular Material |
| Tests | JUnit 5, Mockito, Testcontainers, MockMvc |
| Infra | Docker, docker-compose, GitHub Actions |

## Démarrage rapide

\\\ash
git clone https://github.com/VOTRE_USER/bookhub.git
cd bookhub
cp .env.example .env
docker-compose up
\\\

- Frontend : http://localhost:4200
- API : http://localhost:8080
- Swagger : http://localhost:8080/swagger-ui.html

## Suivi d'apprentissage

- [Journal quotidien](JOURNAL.md)
- [Apprentissage hebdo](APPRENTISSAGE.md)
- [Q/R entretien](ENTRETIEN.md)

## Licence

MIT
