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

## 2026-09-23 (mercredi) — J2

### ✅ Fait
- `Isbn.java` : record avec validation + normalisation (retrait tirets/espaces)
- `IsbnTest.java` : 5 tests JUnit 5 (valide 10, valide 13, tirets, invalide, null)
- `Money.java` : record avec `BigDecimal` + `Currency` + validation scale ≤ 2
- `MoneyTest.java` : 5 tests (from strings, add, devise différente, scale, null)
- 8 nouvelles Q/R entretien (Q14 → Q21) — total **21**
- **10 tests** passent en local et dans le CI

### ❌ Bloqué (résolu)
- JUnit 4 imports dans `IsbnTest` → migré vers JUnit 5
- Typo "requiered" dans `Isbn` → corrigée
- `MoneyTest` pas détecté au début → était dans `src/main/java` au lieu de `src/test/java`

### 💡 Appris
- `spring-boot-starter-test` inclut JUnit 5 — jamais ajouter JUnit manuellement
- Les tests vont dans `src/test/java`, jamais `src/main/java`
- JUnit 5 : `org.junit.jupiter.api.Test`, **jamais** `org.junit.Test`
- `Objects.requireNonNull(value, "message")` en compact constructor
- Compact constructor : on peut réassigner le paramètre pour normaliser
- `BigDecimal.equals()` compare valeur ET scale (piège classique)
- `List.of()` refuse null, `Arrays.asList()` accepte null
- CI Maven avec cache GitHub Actions : 5s build + 14s setup = 19s

### 🎯 Demain (J3)
- `BookStatus` enum avec logique
- `BookDto` record (préparation S2)
- Sealed interface pour `Notification` (Email, SMS)
- Pattern matching `instanceof` appliqué dans le code
- 8 Q/R supplémentaires (Q22 → Q29)