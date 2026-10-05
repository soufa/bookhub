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
---

## 2026-09-30 (mercredi) — J7

### ✅ Fait
- `GreetingService` : `@Service` + `@Value` injection
- `AppConfig` : `@Configuration` + `@Bean` (Clock, String)
- `LifecycleBean` : `@Component` + `@PostConstruct` / `@PreDestroy`
- 5 tests Spring (`@ContextConfiguration`)
- **Total tests : 55**
- 8 Q/R entretien (Q46 → Q53) → **total 53**

### 💡 Appris
- IoC : Spring crée et gère les objets
- DI par constructeur = best practice
- `@Component` / `@Service` / `@Repository` / `@Controller`
- `@Bean` pour types externes (Clock, String)
- `@Value` avec valeur par défaut `${clé:défaut}`
- `@PostConstruct` / `@PreDestroy` (cycle de vie)
- Bean singleton par défaut
- `ApplicationContext` = conteneur IoC

### 🎯 Demain (J8)
- Spring Boot + premier `@RestController`
- `@GetMapping`, `@PostMapping`
- `@RequestParam`, `@PathVariable`
- 8 Q/R supplémentaires (Q54 → Q61)

###Note importante
Les tests Spring (@ContextConfiguration) ne chargent QUE les classes spécifiées. Ils ne chargent pas JPA, la base de données, ou le contexte Spring Boot complet. C'est volontaire : les tests restent rapides et isolés.

Si @ContextConfiguration échoue (ex: @Value ne trouve pas de propriété), utilisez @TestPropertySource pour fournir les valeurs manquantes.


---

## `JOURNAL.md` J8

**Ajoutez** :

---

## 2026-10-02 (vendredi) — J8

### ✅ Fait
- `BookService` : CRUD en mémoire (`ConcurrentHashMap` + `AtomicLong`)
- `BookController` : 5 endpoints REST (`GET all`, `GET by id`, `POST`, `PUT`, `DELETE`)
- 6 tests `BookServiceTest`
- 6 tests `BookControllerTest` (`@WebMvcTest` + MockMvc)
- HelloController supprimé
- **Total tests : 67**
- 8 Q/R entretien (Q54 → Q61) → **total 61**

### 💡 Appris
- `@RestController` = `@Controller` + `@ResponseBody`
- `@RequestMapping` sur la classe = préfixe URL
- `@PathVariable` vs `@RequestParam`
- `ResponseEntity` pour contrôler le code HTTP
- `@WebMvcTest` : tests isolés de la couche web
- `MockMvc` : simuler des requêtes HTTP
- `@MockBean` : remplacer une dépendance par un mock
- Codes HTTP : 200, 201, 204, 400, 404

### 🎯 Demain (J9)
- CRUD REST complet avec pagination
- `@RequestParam` avec `Pageable`
- Recherche multi-critères
- 8 Q/R supplémentaires (Q62 → Q69)


---

## 2026-10-01 (jeudi) — J9

### ✅ Fait
- `BookNotFoundException` (exception métier)
- `GlobalExceptionHandler` + `ProblemDetail` (RFC 7807)
- Pagination : `Pageable` + `Page<T>` + `@PageableDefault`
- Recherche multi-critères (`/api/books/search?title=&author=&status=`)
- Tests MockMvc : pagination + ProblemDetail + search
- **Total tests : 70**
- 8 Q/R entretien (Q62 → Q69) → total 69

### 💡 Appris
- `Pageable` + `Page<T>` : pagination native Spring Data
- `@PageableDefault(size, sort)` : valeurs par défaut
- `@RestControllerAdvice` : gestion d'erreurs centralisée
- `ProblemDetail` : implémentation RFC 7807 (Spring 6)
- `orElseThrow()` : plus lisible que `ResponseEntity.notFound()`
- Multi-tri : `?sort=title,asc&sort=price,desc`

### 🎯 Demain (J10)
- DTO + Mapper (séparation Entity/DTO)
- Validation avancée (`@Valid`, `@NotNull`)
- Tests d'intégration REST

 



---

## Étape 2 — Remplir `JOURNAL.md` J10

##### Théorie express (30 min)
Bean Validation : annotations standard (JSR 380) pour valider les données.
Annotation	Rôle
@NotNull	Refuse null
@NotBlank	Refuse null + vide + espaces
@Size(min, max)	Longueur de chaîne
@Min, @Max	Bornes numériques
@Email	Format email
@Valid	Active la validation en cascade

**Ajoutez** :

```markdown
---

## 2026-10-01 (jeudi) — J10

### ✅ Fait
- `CreateBookRequest` (DTO + validation Bean Validation)
- `BookResponse` (DTO sortie)
- `BookMapper` (conversion Domain ↔ DTO)
- `BookController` utilise les DTO
- `GlobalExceptionHandler` : `MethodArgumentNotValidException` → `ProblemDetail`
- `BookIntegrationTest` : CRUD complet end-to-end
- H2 pour les tests (`application-test.yml`)
- **Total tests : 72**
- 8 Q/R entretien (Q70 → Q77) → total 77

### 💡 Appris
- Séparation DTO entrée/sortie (sécurité)
- Bean Validation : `@NotBlank`, `@Positive`, `@Size`
- `@Valid` sur le paramètre
- `MethodArgumentNotValidException` → errors par champ
- `@MockBean BookMapper` obligatoire dans `@WebMvcTest`
- `@SpringBootTest` + `@AutoConfigureMockMvc` : tests d'intégration
- H2 en mémoire pour les tests (rapide)

### 🎯 Vendredi-Dimanche : REVUE 10 JOURS
- Revoir tous les concepts Java (S1 : J1 → J6)
- Revoir Spring Core + Web (S2 : J7 → J10)
- Relire les 77 Q/R
- Consolider `APPRENTISSAGE.md`


---

## Étape 3 — `JOURNAL.md` J12

**Ajoutez à la fin** :

```markdown
---

## 2026-10-05 (lundi) — J12

### ✅ Fait
- Migration Flyway `V3` : table `authors` + FK `books.author_id`
- Entité `Author` avec `@PrePersist` / `@PreUpdate`
- `AuthorRepository` avec 5 requêtes dérivées
- Relation `@ManyToOne(fetch = LAZY)` sur `Book.authorEntity`
- `AuthorRepositoryTest` : 6 tests
- `BookAuthorRelationTest` : 3 tests
- **Total tests : 81**
- 8 Q/R entretien (Q86 → Q93) → **total 93**

### 💡 Appris
- `@ManyToOne` : côté propriétaire (porte la FK)
- `@OneToMany(mappedBy)` : côté inverse
- `fetch = LAZY` obligatoire sur `@ManyToOne` (sinon N+1)
- `@JoinColumn(name)` : nommer la FK
- `ON DELETE SET NULL` : garder le livre quand l'auteur est supprimé
- Cascade + orphanRemoval : uniquement pour relations fortes
- `@DataJpaTest` + `@ActiveProfiles("test")` : H2 en mémoire

### 🎯 Demain (J13)
- Problème N+1 : reproduction + correction (`@EntityGraph`, `JOIN FETCH`)
- `@Transactional` propagation approfondie


---

## `JOURNAL.md` J13

```markdown
---

## 2026-10-05 (lundi) — J13

### ✅ Fait
- `BookWithAuthorDto` : DTO avec auteur
- `@EntityGraph` sur `findAllWithAuthor(Pageable)`
- `JOIN FETCH` en JPQL
- `BookN1Test` : 3 tests de comparaison N+1 (6 vs 2 vs 1 requêtes)
- `AuditService` : `REQUIRES_NEW` + `MANDATORY`
- `AuditServiceTest` : 2 tests propagation
- **Total tests : 86**
- 8 Q/R entretien (Q94 → Q101) → **total 101**

### 💡 Appris
- N+1 = 1 requête initiale + N requêtes par relation
- `@EntityGraph` : marche avec `Pageable`
- `JOIN FETCH` : 1 requête, mais incompatible `Pageable`
- `Statistics.getPrepareStatementCount()` : compter les requêtes en test
- `REQUIRES_NEW` : suspend l'existante, utile pour audit
- `MANDATORY` : impose une transaction existante
- Problème du proxy : `this.method()` ne déclenche pas `@Transactional`

### 🎯 Demain (J14)
- Testcontainers : tests d'intégration PostgreSQL réel
- Remplacer H2 par un container Docker



---

## Étape 12 — `JOURNAL.md` J14

```markdown
---

## 2026-10-05 (lundi) — J14

### ✅ Fait
- Plugin `maven-failsafe-plugin` configuré
- `AbstractIntegrationTest` : container PostgreSQL singleton
- `application-it.yml` : profil Testcontainers avec Flyway
- `BookRepositoryIT` : 3 tests PostgreSQL réel
- `BookN1IT` : 1 test N+1 sur PostgreSQL
- **Tests surefire : 86** (H2)
- **Tests failsafe : 4** (PostgreSQL réel)
- 8 Q/R entretien (Q102 → Q109) → **total 109**

### 💡 Appris
- Testcontainers = vraie BDD dans les tests
- `*Test` (Surefire) vs `*IT` (Failsafe)
- Pattern singleton : container static + start() au chargement
- `@DynamicPropertySource` : injection de propriétés dynamiques
- `withReuse(true)` : réutilisation entre exécutions
- `MODE=PostgreSQL` H2 reste différent du vrai PostgreSQL
- GitHub Actions fournit Docker par défaut

### 🎯 Demain (J15)
- Spring Security : `SecurityFilterChain`
- `UserDetailsService` + users en mémoire
- Tests de sécurité (`@WithMockUser`)

### 2. `JOURNAL.md` J14

Ajoutez **à la fin** :

```markdown
---

## 2026-10-05 (lundi) — J14

### ✅ Fait
- Plugin `maven-failsafe-plugin` + exclusion `*IT` dans Surefire
- Testcontainers 1.19.7 → **1.21.4** (Docker Engine 29.x incompatible)
- `AbstractIntegrationTest` : container PostgreSQL singleton + `@Sql` ISOLATED
- `application-it.yml` : profil Testcontainers avec Flyway (`ddl-auto=validate`)
- `db/clean.sql` : TRUNCATE books + authors avant chaque test
- `BookRepositoryIT` : 3 tests PostgreSQL réel
- `BookN1IT` : 1 test N+1 → **1 requête confirmée**

### 💡 Appris
- Testcontainers = vraie BDD dans les tests d'intégration
- Docker Engine 29.x nécessite Testcontainers ≥ 1.20 (API 1.54)
- `NpipeSocketClientProviderStrategy: Status 400` = client trop vieux
- `*Test` (Surefire) vs `*IT` (Failsafe)
- Pattern singleton : container `static` + `start()`
- `@DynamicPropertySource` : injection dynamique
- `withReuse(true)` + `@Sql ISOLATED` : nettoyage inter-classes
- N+1 doit être testé **hors transaction** (sinon invisible)
- `TransactionTemplate` pour le setup d'un test non-`@Transactional`

### 📊 Métriques
- Tests surefire : 86
- Tests failsafe : 4
- Q/R totales : 110

### 🎯 Demain (J15)
- Spring Security : `SecurityFilterChain`
- `UserDetailsService` + users en mémoire
- Tests (`@WithMockUser`)
- Q111 → Q116



### 2. Mettre à jour `JOURNAL.md` (J15)

```markdown
---

## 2026-10-05 (lundi, après-midi) — J15

### ✅ Fait
- Dépendances JWT (jjwt 0.12.6)
- `SecurityConfig` : SecurityFilterChain + STATELESS + HttpStatusEntryPoint
- `AppUserDetailsService` : users en mémoire (user/user123, admin/admin123)
- `JwtService` : génération + validation JWT
- `JwtAuthenticationFilter` : extraction Bearer token
- `AuthController` : POST /api/auth/login
- `@PreAuthorize("hasRole('ADMIN')")` sur create/update/delete
- `@WithMockUser` sur BookIntegrationTest
- **Tests : 93 verts**

### 💡 Appris (4 bugs réels)
1. **Cycle de beans** Spring Security → `@Bean public static PasswordEncoder`
2. **Rôle vide** dans `.roles("")` → filtrer `!r.isBlank()`
3. **403 en test** sans auth → `@WithMockUser(username="admin", roles="ADMIN")`
4. **403 vs 401** → `HttpStatusEntryPoint(UNAUTHORIZED)` explicite

### 📊 Métriques
- Tests : 93
- Q/R : 118

### 🎯 Demain (J16)
- Tests JWT end-to-end (vrai login + token)
- `@WithUserDetails` (vrai UserDetailsService au lieu de mock)
- Refresh token
- Handler 401/403 personnalisé