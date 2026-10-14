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

---

## 2026-10-06 (mardi) — J16

### ✅ Fait
- `RestAuthenticationEntryPoint` : réponse JSON 401
- `RestAccessDeniedHandler` : réponse JSON 403
- `SecurityConfig` : handlers branchés
- `JwtEndToEndIT` : 4 tests E2E (login → token → endpoint)
- `JwtServiceTest` : 7 tests unitaires JWT
- `BookControllerWithUserDetailsTest` : 2 tests avec `@WithUserDetails`
- **Tests : 110 verts** (102 Surefire + 8 Failsafe)

### 💡 Appris (4 pièges vécus)
1. **Réflexion sur champ privé** : `secret` → `secretKey` (noms réels)
2. **`ExpiredJwtException` non résolu dans l'IDE** : utiliser `Exception` ou `JwtException` (classe parente)
3. **jjwt choisit l'algo selon la taille de clé** : 64 octets → HS512, pas HS256
4. **`@WithMockUser` vs `@WithUserDetails`** : le second teste le vrai `UserDetailsService`

### 📊 Métriques
- Tests Surefire : 102
- Tests Failsafe : 8
- Total : 110
- Q/R : 125

---

## 2026-10-07 (mercredi) — J17

### ✅ Fait
- Migration Flyway V4 : table `refresh_tokens`
- Entité `RefreshToken` + `RefreshTokenRepository`
- `RefreshTokenService` : create, rotate, revoke
- Endpoints : POST `/api/auth/refresh`, POST `/api/auth/logout`
- Rotation + révocation globale en cas de réutilisation
- 4 tests E2E refresh (rotation, inconnu, réutilisation, logout)
- **Tests : 114 verts** (102 Surefire + 12 Failsafe)

### 💡 Appris (4 pièges vécus)
1. `@AutoConfigureMockMvc` **doit être sur la classe parente** — sur la sous-classe ne suffit pas
2. Contexte Spring Test partagé entre IT → bug login (401)
3. `@DirtiesContext(BEFORE_EACH_TEST_METHOD)` : workaround efficace
4. `LoginResponse` doit avoir `type: "Bearer"` (RFC 6750)
5. `@Bean static` sur `PasswordEncoder` = anti-pattern

### ⚠️ Dette technique (à traiter en J18)
- `RefreshTokenEndToEndIT` : `@DirtiesContext(BEFORE_EACH)` à investiguer
- Probable : cache de contexte Spring Test + `AppUserDetailsService`
- PasswordConfig séparé : à refactorer proprement

### 📊 Métriques
- Tests Surefire : 102
- Tests Failsafe : 12
- Q/R : 132

### 🎯 Demain (J18)
- docker-compose complet (backend + PostgreSQL + Adminer)
- Déploiement local en 1 commande
- Nettoyage du workaround `@DirtiesContext`
- Dockerfile test + première image

---

## 2026-10-12 (lundi) — J18 + J19

### ✅ Fait — J18 (Docker Compose)
- `docker-compose.yml` : postgres + backend + adminer (avec healthchecks)
- `application-docker.yml` : profil Spring pour Docker
- `Dockerfile` multi-stage reconstruit (Maven build → JRE alpine)
- `.dockerignore`, `.env.example`
- Backend en Docker sur 8080, PostgreSQL sur 5432, Adminer sur 8888
- Test : `docker-compose up -d` → 3 conteneurs healthy
- Login JWT fonctionnel via Docker

### ✅ Fait — J19 (Angular 19)
- Angular CLI 19.2.27 installé (désinstallation Node 24 → Node 22.11.0)
- Projet `bookhub-frontend` créé (standalone components, SCSS)
- `BookService` Angular avec pagination (`Page<T>`)
- `BookListComponent` affiche les livres depuis l'API
- `app.config.ts` : `provideHttpClient()`
- `app.routes.ts` : route `/books`
- `CorsConfig.java` côté Spring (autorise localhost:4200)
- `SecurityConfig` : `permitAll` + `/error` + matcher Swagger propre

### 💡 Appris (5 problèmes d'infra résolus)
1. **`winget` absent** → téléchargement direct des MSI Node
2. **Node 24 incompatible Angular** → désinstaller + installer Node 22 LTS
3. **`ng` pas dans le PATH** → ajouter `%AppData%\npm` au PATH
4. **`curl.exe` sur Windows casse le JSON** → utiliser `Invoke-RestMethod`
5. **`/error` bloqué par Security** → erreurs 401 au lieu de 400/500
6. **DTO ≠ Entity ≠ Request** → `CreateBookRequest` (String) vs `BookDto` (objets Isbn/Money)
7. **Spring Data retourne `Page<T>`** → Angular doit extraire `.content`

### 📊 Métriques
- Tests backend : 102 (inchangés)
- Services Docker : 3 (postgres, backend, adminer)
- Composants Angular : 1 (`BookListComponent`)
- Services Angular : 1 (`BookService`)
- Q/R : à mettre à jour dans ENTRETIEN.md

### ⚠️ Dette technique
- `/api/books/**` doit repasser en `authenticated()` (fait en fix CI)
- Angular sans login (J20 va l'ajouter)
- Doublons ISBN en BDD → migration V5 à créer
- Pas de seeds automatiques → migration V6 à créer

### 🎯 Demain (J20)
- Angular Auth JWT : login + interceptor + guards
- Désactiver `@DirtiesContext` (dette J17)
- Re-build backend avec `authenticated()` sur books

---

## 2026-10-12 (lundi, après-midi) — J20

### ✅ Fait — Angular Auth JWT
- `AuthService` : login, logout, stockage token (localStorage), signal `currentUser`
- `AuthInterceptor` : ajoute automatiquement `Authorization: Bearer <token>`
- `LoginComponent` : formulaire réactif + gestion erreurs + comptes de test affichés
- `AuthGuard` : protège `/books`, redirige vers `/login`
- `AppComponent` : header avec username + bouton déconnexion
- `app.routes.ts` : `/login` public, `/books` protégé par `authGuard`
- `app.config.ts` : `provideHttpClient(withInterceptors([authInterceptor]))`
- Refactor avec **Angular 17+ control flow** (`@if` au lieu de `*ngIf`) → plus de `CommonModule`

### 💡 Appris
- **Interceptor fonctionnel** (`HttpInterceptorFn`) : nouvelle API Angular 15+
- **Signals Angular** (`signal()`) : état réactif moderne
- **Guard fonctionnel** (`CanActivateFn`) : nouvelle syntaxe Angular 15+
- **Control flow `@if`** : plus besoin d'importer `CommonModule`
- **`provideHttpClient(withInterceptors(...))`** : config standalone
- **localStorage** pour le JWT : simple, persiste au refresh
- **Piège `title`** : Angular 19 n'expose plus `title` par défaut dans `AppComponent`

### 📊 Métriques
- Composants Angular : 2 (`LoginComponent`, `BookListComponent`)
- Services Angular : 2 (`AuthService`, `BookService`)
- Interceptors : 1 (`AuthInterceptor`)
- Guards : 1 (`AuthGuard`)
- Tests backend : 102 (inchangés)
- Q/R : à mettre à jour (Q151-Q160)

### 🎯 Flow JWT validé bout-en-bout
1. Login → POST `/api/auth/login` → JWT + refresh token stockés
2. Navigation → guard vérifie `isLoggedIn()`
3. Appel API → interceptor ajoute `Bearer <token>`
4. Backend → `JwtAuthenticationFilter` valide → `SecurityContext` peuplé
5. Réponse → 200 avec données

### ⚠️ Dette technique (à traiter en J21)
- Refresh token non utilisé côté Angular (access token expire après 1h)
- Pas de gestion du 401 (redirect auto vers /login si token expiré)
- Pas de "remember me" (localStorage = session persistante)
- Désactiver `@DirtiesContext(BEFORE_EACH)` dans les IT (dette J17)

### 🎯 Demain (J21)
- Workflow CD GitHub Actions : build + push Docker sur GHCR
- Déploiement sur Fly.io (backend) + Netlify/Vercel (frontend)
- Env vars de prod (JWT secret, DB URL)
- Badge CD dans le README


---

## 2026-10-12 (lundi, fin d'après-midi) — J21

### ✅ Fait — CD GitHub Actions
- Workflow `.github/workflows/cd.yml`
- Job `build-and-push-backend` : build JAR + push image Docker sur GHCR
- Job `build-frontend` : build Angular production + upload artifact
- Metadata Docker (tags : `main`, `latest`, `sha-xxxxx`, `v0.1.0`)
- Cache Maven (`setup-java@v4 cache: maven`), Docker (`type=gha`), npm (`setup-node@v4 cache: npm`)
- Enrichissement de `ci.yml` : ajout job `frontend` (Node 22 + `npm ci` + build prod)

### 💡 Appris
- `permissions: packages: write` obligatoire pour pousser sur GHCR
- `docker/metadata-action@v5` génère les tags automatiquement
- `docker/build-push-action@v5` avec `cache-from/cache-to: type=gha`
- 4 niveaux de tags utiles : branche, latest, sha, version sémantique
- `workflow_dispatch` = déclenchement manuel
- `actions/upload-artifact@v4` pour partager un build entre jobs
- Piège : `cd.yml` copié/collé du `ci.yml` → workflow sans Docker

### 📊 Métriques
- Workflows GitHub Actions : 2 (CI + CD)
- Jobs CI : 2 (backend, frontend)
- Jobs CD : 2 (build-and-push-backend, build-frontend)
- Images GHCR : 1 (`ghcr.io/soufa/bookhub/backend`)
- Durée CI : ~1m25s
- Durée CD : ~6-8 min
- Tests backend : 102 (inchangés)

### 🎯 Demain (J22)
- Déploiement backend sur Fly.io
- Déploiement frontend sur Netlify / Vercel
- Env vars de prod (JWT secret, DB URL)
- README final + screenshots + vidéo démo
- Post LinkedIn

---

## 2026-10-12 (lundi) — J23

### ✅ Fait — Spring Batch : import CSV
- Dépendance `spring-boot-starter-batch` ajoutée
- Config `spring.batch.job.enabled: false` (pas d'auto-run au boot)
- CSV `books-import.csv` avec 8 livres
- `BookCsvRecord` DTO + `BookItemProcessor` (validation + skip doublon via `existsByIsbn`)
- `ImportBooksJobConfig` : job `importBooksJob`
  - Reader : `FlatFileItemReader` (CSV, `linesToSkip=1`)
  - Processor : `BookItemProcessor` (validation ISBN + devise + doublon)
  - Writer : `JpaItemWriter`
  - `chunk(10)` + `faultTolerant().skip().skipLimit(20)`
- Endpoint `POST /api/batch/import` (ADMIN)
- **Fix critique** : `BookController` utilise `BookService` (JPA) au lieu de `OldBookService` (mémoire)
- **Test manuel réussi** : job COMPLETED → 8 livres en DB (6 du seed V6 + 2 nouveaux)
- Q171 → Q180 dans `ENTRETIEN.md`

### 💡 Appris (12 concepts clés)
1. Structure `Job → Step → Chunk(Reader, Processor, Writer)`
2. `FlatFileItemReaderBuilder` pour CSV
3. `JpaItemWriter` pour insertion JPA
4. `faultTolerant().skip().skipLimit()` pour résister aux erreurs
5. `JobParameters` avec timestamp pour unicité d'exécution
6. `JobLauncher.run()` depuis un endpoint REST
7. `spring.batch.job.enabled: false` pour empêcher l'auto-run
8. `@Qualifier` sur un bean `Job` quand plusieurs jobs
9. **Tolérance aux doublons** : `existsByIsbn` dans le Processor (skip propre)
10. **Debug Spring Batch** : tables `BATCH_*` + `exit_message`
11. **Idempotence** : le job peut être relancé sans erreur (ON CONFLICT ou skip)
12. **Sécurité** : `@PreAuthorize("hasRole('ADMIN')")` sur les endpoints batch

### ⚠️ Pièges vécus et résolus
| Piège | Symptôme | Fix |
|-------|----------|-----|
| **Doublon ISBN** | `DuplicateKeyException` → job FAILED | `existsByIsbn()` dans le Processor |
| **Container Docker ancien** | API retourne 0 alors que la DB a 6 | `docker-compose build --no-cache backend` |
| **Flyway checksum mismatch** | `Migration checksum mismatch for V6` | `docker-compose down -v` (reset DB) |
| **Classe dans mauvais fichier** | `BookItemProcessor should be in BookItemProcessor.java` | Restaurer les 2 fichiers |
| **`OldBookService` en mémoire** | SQL Hibernate vide, API retourne 0 | Remplacer par `BookService` (JPA) |

### 📊 Métriques
- Tests : 102 (inchangés)
- Jobs Batch : 1 (`importBooksJob`)
- Executions : 3 (2 COMPLETED, 1 FAILED initial)
- Livres en DB : 8 (6 seed + 2 import)
- Q/R : 180

### 🎯 Demain (J24)
- Job `computeStatisticsJob` (Tasklet) — calculs par statut, prix moyen
- Scheduler `@Scheduled(cron = "0 0 2 * * *")` — stats automatiques
- Endpoint `GET /api/batch/runs` (JobExplorer) — historique
- 2-3 tests d'intégration Spring Batch

## 2026-10-13 (mardi) — J24

### ✅ Fait — Spring Batch : Tasklet + Scheduler
- Migration Flyway V7 : table `book_statistics`
- `StatisticsDto` (computedAt, totalBooks, available, borrowed, avgPrice, topAuthor)
- `ComputeStatisticsTasklet` (calcul + insert snapshot)
- `ComputeStatisticsJobConfig` (tasklet-based)
- `BatchScheduler` avec `@Scheduled(cron = "0 0 2 * * *")`
- `SchedulingConfig` avec `@EnableScheduling`
- Enrichissement `BatchController` :
  - `POST /api/batch/statistics` (ADMIN)
  - `GET /api/batch/runs?limit=N` (ADMIN)
  - `GET /api/batch/statistics/latest` (ADMIN)
- **Test manuel** : statistics job COMPLETED
- Q181 → Q190 dans ENTRETIEN.md

### 💡 Appris
- `Tasklet` (vs chunk) pour actions uniques
- `@Scheduled` avec cron expression
- `@EnableScheduling` (obligatoire)
- `JobExplorer` pour interroger l'historique
- `JdbcTemplate` pour requêtes SQL directes
- `@ConditionalOnProperty` pour feature flags
- **Snapshot pattern** : insérer une ligne par exécution

### 📊 Métriques
- Tests : 102 (inchangés)
- Jobs Batch : 2 (`importBooksJob`, `computeStatisticsJob`)
- Endpoints Batch : 4
- Q/R : 190

### 🎯 Demain (J25)
- Actuator Batch (monitoring)
- Tests d'intégration Spring Batch
- Migration Flyway V7 pour les tables `BATCH_*` en prod

---

## 2026-10-13 (mardi) — J25

### ✅ Fait — Monitoring Batch + tests
- Migration Flyway V7 : `book_statistics` (aligné prod)
- `ComputeStatisticsTasklet` : agrégation + snapshot
- `ComputeStatisticsJobConfig` : Job + Step + `JobLoggingListener`
- `BookSkipPolicy` : skip seulement `DuplicateKeyException`
- `POST /api/batch/statistics` (ADMIN) — lance le job
- Test d'intégration `ComputeStatisticsJobTest` (H2)
- **Tests : 115 verts** (103 Surefire + 12 Failsafe)

### 💡 Appris (5 pièges vécus en cascade)
1. **`AUTO_INCREMENT` (MySQL) ≠ `BIGSERIAL` (PostgreSQL)** — H2 en `MODE=PostgreSQL` rejette le premier
2. **Indentation YAML** : `spring.batch.jdbc.initialize-schema` doit être sous `spring:`, pas à la racine
3. **`TestSecurityConfig` casse les tests de sécurité** — il désactive `anyRequest().permitAll()`, donc `isUnauthorized()` échoue
4. **Spring Batch a besoin de son schéma** : `initialize-schema: always` en test
5. **`schema.sql` de test ≠ Flyway V7** : colonnes désalignées → job FAILED → test FAILED

### 📊 Métriques
- Tests Surefire : 103
- Tests Failsafe : 12
- Q/R : 195
- Migrations Flyway : V1 → V8

### ⚠️ Dette technique
- `schema.sql` maintenu manuellement → risque de désalignement futur
- Envisager : Flyway en test avec H2 mode PostgreSQL (à faire en J26)

### 🎯 Demain (J26)
- Nettoyage du workaround `@DirtiesContext` (J17)
- Option : Flyway en test (H2 mode PostgreSQL)
- Suite Angular : auth JWT (login form + interceptor + guards)



### 4. Section J27 dans `JOURNAL.md`

---

## 2026-10-13 (mardi) — J27

### ✅ Fait — Auto-refresh JWT sur 401
- `AuthService.refresh()` : appelle `/api/auth/refresh` (avec rotation)
- `AuthService.clearSession()` : nettoie + redirect si refresh échoue
- `refreshInterceptor` : capture les 401, rejoue la requête avec nouveau token
- Gestion concurrence : 1 seul refresh via `BehaviorSubject` + `isRefreshing`
- Exclusion `/api/auth/**` pour éviter les boucles infinies
- `app.config.ts` : ordre `[refreshInterceptor, authInterceptor]`
- **Test E2E** : token expiré → refresh auto → 200 transparent ✅

### 💡 Appris (3 pièges vécus)
1. **Boucle infinie** : exclure `/api/auth/**` du refresh interceptor
2. **Concurrence** : `BehaviorSubject` + `isRefreshing` pour ne faire qu'1 refresh
3. **Ordre des interceptors** : `refreshInterceptor` AVANT `authInterceptor`

### 📊 Métriques
- Tests backend : 115
- Interceptors Angular : 2 (auth + refresh)
- Q/R : 205
- Tokens côté client : access (1h) + refresh (7j)

### 🎯 Demain (J28)
- Tests unitaires Angular (auth.service.spec.ts, refresh.interceptor.spec.ts)
- Ou CI/CD complet (Docker frontend + GitHub Actions)
- Ou portfolio / README / démo en ligne


---

## 2026-10-14 (mercredi) — J28 (Bloc A) : Tests Angular

### ✅ Fait — 31 tests frontend
- `auth.service.spec.ts` : 11 tests (login, logout, isLoggedIn, refresh, clearSession)
- `auth.interceptor.spec.ts` : 3 tests (header Bearer, exclusion /api/auth, cas sans token)
- `refresh.interceptor.spec.ts` : 4 tests (pass-through, 401 → refresh → retry, session expired, boucle infinie)
- `login.component.spec.ts` : 5 tests (submit valide, erreurs 401/500, champs vides)
- `book-list.component.spec.ts` : 4 tests (load, error, rendu DOM, empty state)
- `app.component.spec.ts` : 2 tests (création, logout appelé)
- `book.service.spec.ts` : 2 tests (création, GET avec pagination)
- **Total** : 31 tests verts (au lieu de ~3 générés par défaut)

### 💡 Appris (5 pièges vécus)

#### 1. Karma n'a pas de `karma.conf.js` en Angular 17+
- Angular CLI moderne **ne génère plus** `karma.conf.js`
- Il faut **le créer manuellement** ET l'ajouter dans `angular.json` (`karmaConfig`)
- Sans cette config, impossible d'utiliser `ChromeHeadlessNoSandbox`

#### 2. Chrome headless crash en environnement CI
- Erreur `Chrome crashed` + `Some of your tests did a full page reload`
- **Fix** : launcher personnalisé `ChromeHeadlessNoSandbox` avec flags :
  - `--no-sandbox`
  - `--disable-gpu`
  - `--disable-dev-shm-usage`
  - `--disable-software-rasterizer`

#### 3. Test par défaut généré par Angular CLI est incomplet
- `ng generate service BookService` crée un test qui **ne fournit pas** `HttpClient`
- Erreur : `NullInjectorError: No provider for HttpClient!`
- **Fix** : ajouter `HttpClientTestingModule` aux imports + écrire de vrais tests

#### 4. Validation avant appel asynchrone
- `LoginComponent.onSubmit()` appelait `auth.login()` **avant** de vérifier les champs vides
- Résultat : `Cannot read properties of undefined (reading 'subscribe')` dans le test
- **Fix** : ajouter une garde `if (!username || !password) return;` **avant** l'appel

#### 5. Mock `AuthService` dans les specs de composants
- Le composant `AppComponent` injecte `AuthService`
- Le test par défaut ne mocke pas → crash
- **Fix** : `{ provide: AuthService, useValue: { isLoggedIn: () => false, ... } }`

### 📊 Métriques
- Tests backend : 115 (inchangés)
- Tests frontend : **31** (nouveaux)
- **Total** : **146 tests**
- Q/R : 205

### ⚠️ Dette technique
- Pas encore de coverage frontend (Karma peut générer `coverage/`)
- `book-list.component.spec.ts` : ajouter tests de pagination
- Tests E2E Cypress (à prévoir J30+)

### 🎯 À faire dans la suite de J28
- Bloc B : Dockerfile Angular + GitHub Actions CI/CD
- Bloc C : README + screenshots + déploiement démo