## 2026-09-22 (mardi) — J1

### ✅ Fait
- Arborescence projet créée
- Spring Boot 3.2.5 + Java 17 configurés
- PostgreSQL 16 en Docker (port 5433)
- Flyway migration V1 exécutée automatiquement
- Endpoint `/api/hello` fonctionnel
- Repo GitHub public + CI GitHub Actions vert
- 5 Q/R entretien documentées

### ❌ Bloqué (résolu)
- Erreur d'authentification PostgreSQL → volume obsolète supprimé
- Conflit port 5432 avec PostgreSQL natif Windows → port 5433
- Conflit Spring Security + Actuator → exclusion des auto-configs

### 💡 Appris
- Docker volume persistence : `POSTGRES_PASSWORD` n'est utilisé qu'à la 1ère init
- `docker-compose down -v` ne supprime pas toujours les volumes en usage
- Spring Security auto-config s'active dès que le starter est dans le pom.xml
- Actuator + Security sont couplés (erreur HttpSecurity)
- Port mapping Docker : `5433:5432` = 5433 côté Windows, 5432 dans le container
- GitHub Actions : setup-java avec cache Maven = CI vert
- Badge CI dans le README = signal pro pour les recruteurs

### 🎯 Demain (J2 — 23/09)
- Cours Buchalka : Records + sealed + pattern matching (2h)
- Créer `Isbn.java` et `Money.java` dans `shared/domain/` (2h)
- Écrire 8 nouvelles Q/R entretien (Records avancés + sealed)
- Commit GitHub avec le 1er code Java moderne
### Note sur spring-boot-starter-test
Ce starter inclut automatiquement :
JUnit 5 (Jupiter)
Mockito
AssertJ
Hamcrest
JSONassert
Spring Test
Vous n'avez jamais besoin d'ajouter JUnit manuellement. Si vous voyez une recommandation qui suggère de l'ajouter, elle est fausse.