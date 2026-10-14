# 🎓 Apprentissage hebdomadaire — BookHub

> Synthèse chaque dimanche des concepts maîtrisés, fragiles, et objectifs pour la semaine suivante.
> Sert de tableau de bord d'apprentissage et de support pour les entretiens techniques.

---

## 📑 Table des matières

- [Semaine 1 (23-29/09/2026) — Java moderne](#semaine-1-23-29092026--java-moderne)
- [Semaine 2 (30/09-06/10/2026) — Spring Core + REST + JPA](#semaine-2-3009-06102026--spring-core--rest--jpa)
- [Semaine 3 (07-13/10/2026) — Relations JPA + Tests + Security](#semaine-3-07-13102026--relations-jpa--tests--security)
- [Semaine 4 (07-12/10/2026) — Docker + Frontend + Déploiement](#semaine-4-07-12102026--docker--frontend--déploiement)
- [Semaine 5 (13-19/10/2026) — Spring Batch + Angular Auth + CI/CD](#semaine-5-13-19102026--spring-batch--angular-auth--cicd)

---

## Semaine 1 (23-29/09/2026) — Java moderne

### Concepts maîtrisés

| Concept | Preuve dans le code | Exercice cahier |
|---------|---------------------|-----------------|
| Records | Isbn.java, Money.java | 1.8 |
| Sealed interfaces | (à venir) | 1.30 |
| Pattern matching | (à venir) | 1.28 |
| Streams | (à venir) | 3.7-3.12 |
| Optional | (à venir) | 3.16 |
| Génériques | PageResult<T> | 7.1-7.6 |

### Concepts fragiles

- (à remplir)

### Statistiques

- Q/R entretien écrites : 0 / 40
- Commits : 0
- Exercices cahier faits : 0 / 20

### Objectif semaine suivante

- Spring Core + REST basique

---

## Semaine 2 (30/09-06/10/2026) — Spring Core + REST + JPA

### Concepts maîtrisés

| Concept | Preuve dans le code | Exercice cahier |
|---------|---------------------|-----------------|
| Spring IoC / DI | BookService, BookController | 5.1-5.5 |
| `@Configuration` + `@Bean` | AppConfig, SecurityConfig | 5.6 |
| Injection par constructeur | Tous les services | 5.7 |
| REST Controller | BookController | 2.1-2.10 |
| Validation Bean Validation | CreateBookRequest | 2.15 |
| RFC 7807 (ProblemDetail) | GlobalExceptionHandler | 2.20 |
| Spring Data JPA | BookRepository | 6.1-6.5 |
| Pagination | `Page<T>` + `Pageable` | 6.10 |
| Records comme DTO | BookDto, BookResponse | 1.8 |
| Value Objects | Isbn, Money (auto-validants) | 1.15 |

### Concepts fragiles

- (à remplir)

### Statistiques

- Q/R entretien écrites : 40
- Commits : ~15
- Exercices cahier faits : 30 / 60

### Objectif semaine suivante

- Relations JPA (OneToMany, ManyToMany)
- Tests unitaires + intégration
- Spring Security JWT

---

## Semaine 3 (07-13/10/2026) — Relations JPA + Tests + Security

### Concepts maîtrisés

| Concept | Preuve dans le code | Exercice cahier |
|---------|---------------------|-----------------|
| `@OneToMany` / `@ManyToOne` | Book ↔ Author | 6.20-6.25 |
| `@ManyToMany` | Book ↔ Category | 6.26 |
| Problème N+1 | `@EntityGraph`, fetch join | 6.30 |
| `@Transactional` propagation | BookService, AuditService | 6.35 |
| Tests unitaires (JUnit 5 + Mockito) | BookControllerTest | 4.1-4.10 |
| Tests d'intégration | BookRepositoryIT | 4.15 |
| Testcontainers | BookRepositoryIT (PostgreSQL réel) | 4.20 |
| `@AutoConfigureMockMvc` | Tous les IT | 4.22 |
| Spring Security (config) | SecurityConfig | 8.1-8.10 |
| JWT (génération + validation) | JwtService | 8.15 |
| JWT filter | JwtAuthenticationFilter | 8.20 |
| Refresh token avec rotation | RefreshTokenService | 8.25 |
| Handlers 401/403 JSON | RestAuthenticationEntryPoint | 8.30 |
| Tests E2E sécurité | JwtEndToEndIT, RefreshTokenEndToEndIT | 8.35 |

### Concepts fragiles

| Concept | Pourquoi fragile | Prochaine étape |
|---------|------------------|-----------------|
| Propagation `REQUIRES_NEW` | Comportement contre-intuitif | Faire un test avec 2 transactions |
| Testcontainers + `withReuse(true)` | État persistant entre runs | `docker ps -a --filter testcontainers` |
| `@DirtiesContext` | Workaround, pas solution | Nettoyer via Flyway en test |

### Statistiques

- Tests : 115 (100 unit + 15 IT)
- Q/R : 175
- Commits : ~30
- Exercices cahier faits : 45 / 60

### Objectif semaine suivante

- Frontend Angular (login + liste)
- Docker Compose complet
- Déploiement Fly.io + Vercel

---

## Semaine 4 (07-12/10/2026) — Docker + Frontend + Déploiement

### Concepts maîtrisés

| Concept | Preuve dans le code | Exercice cahier |
|---------|---------------------|-----------------|
| Dockerfile multi-stage (backend) | `backend/Dockerfile` | 9.1-9.5 |
| Docker Compose (3 services) | `docker-compose.yml` | 9.10 |
| Healthcheck + dépendances | `depends_on: condition: service_healthy` | 9.15 |
| Angular 19 (standalone) | `app.config.ts` | 10.1-10.5 |
| Angular Router | `app.routes.ts` | 10.6 |
| HttpClient + services | `BookService` | 10.10 |
| Signals Angular | `signal<User>` (à venir) | 10.15 |
| CORS Spring Boot | `CorsConfig` | 9.25 |
| Fly.io (déploiement backend) | `fly.toml` | 11.1-11.5 |
| Fly.io Postgres managé | `fly postgres attach` | 11.10 |
| Vercel (déploiement frontend) | `vercel --prod` | 11.15 |
| Variables d'environnement prod | `fly secrets set` | 11.20 |
| Tunnel SSH vers DB Fly | `fly proxy 5433:5432` | 11.25 |

### Concepts fragiles

| Concept | Pourquoi fragile | Prochaine étape |
|---------|------------------|-----------------|
| Fly.io free tier | Machine stoppée après 5 min | Carte bancaire ou upgrade |
| SSL PostgreSQL Fly | `sslmode=require` obligatoire | Tester la connexion en prod |
| CORS multi-origines | `allowed-origins` à maintenir | Centraliser dans une config |
| Cache Docker multi-stage | Ordre des `COPY` critique | Benchmark avant/après |

### Statistiques

- Tests : 115 (inchangés)
- Q/R : 190
- Commits : ~40
- Exercices cahier faits : 55 / 60
- URLs prod : 1 backend (Fly) + 1 frontend (Vercel)

### Objectif semaine suivante

- Spring Batch (import CSV + stats)
- Tests Angular
- Docker frontend + CI/CD complet

---

## Semaine 5 (13-19/10/2026) — Spring Batch + Angular Auth + CI/CD

### Concepts maîtrisés

| Concept | Preuve dans le code | Exercice cahier |
|---------|---------------------|-----------------|
| **Spring Batch — Job** | `ImportBooksJobConfig` | 12.1-12.5 |
| **Spring Batch — Chunk** | `FlatFileItemReader` + `JpaItemWriter` | 12.10 |
| **Spring Batch — Tasklet** | `ComputeStatisticsTasklet` | 12.15 |
| **Skip policy** | `BookSkipPolicy` | 12.20 |
| **Job logging** | `JobLoggingListener` | 12.22 |
| **Tables BATCH_*** | Migration Flyway V8 | 12.25 |
| **Angular — AuthService** | `auth.service.ts` (login/logout/isLoggedIn/refresh) | 13.1-13.5 |
| **Angular — Signals** | `signal<string \| null>` pour `currentUser` | 13.10 |
| **Angular — HttpInterceptorFn** | `auth.interceptor.ts` (Bearer token) | 13.15 |
| **Angular — Refresh interceptor** | `refresh.interceptor.ts` (401 → refresh → retry) | 13.20 |
| **Angular — Concurrence** | `BehaviorSubject` + `isRefreshing` | 13.22 |
| **Angular — CanActivateFn** | `auth.guard.ts` (protège routes) | 13.25 |
| **Angular — Control flow** | `@if` (Angular 19) | 13.28 |
| **Angular — JWT expiration** | Décoder `payload.exp` | 13.30 |
| **Tests Angular — Service** | `auth.service.spec.ts` (11 tests) | 14.1-14.5 |
| **Tests Angular — Interceptor** | `auth.interceptor.spec.ts` (3 tests) | 14.10 |
| **Tests Angular — Refresh** | `refresh.interceptor.spec.ts` (4 tests) | 14.12 |
| **Tests Angular — Composant** | `login.component.spec.ts` (5 tests) | 14.15 |
| **Karma custom launcher** | `ChromeHeadlessNoSandbox` | 14.20 |
| **Dockerfile frontend multi-stage** | `bookhub-frontend/Dockerfile` | 15.1-15.5 |
| **nginx SPA routing** | `try_files $uri $uri/ /index.html` | 15.10 |
| **nginx cache control** | Assets immutables, index.html non-caché | 15.15 |
| **Docker healthcheck** | `wget http://127.0.0.1/` (piège IPv6) | 15.20 |
| **docker-compose multi-services** | 4 services (postgres, backend, frontend, adminer) | 15.25 |
| **GitHub Actions CI** | `.github/workflows/ci-cd.yml` (backend-tests + frontend-tests) | 16.1-16.5 |
| **GitHub Actions CD** | Push Docker sur ghcr.io | 16.10 |
| **GitHub Actions cache** | `cache: 'maven'`, `cache: 'npm'`, `type=gha` | 16.15 |
| **ghcr.io registry** | `permissions: packages: write` | 16.20 |
| **Docker BuildKit cache** | `cache-from: type=gha` | 16.25 |
| **Pattern if: conditionnel** | Skip jobs docker sur PR | 16.30 |

### Concepts fragiles

| Concept | Pourquoi fragile | Prochaine étape |
|---------|------------------|-----------------|
| `BehaviorSubject` concurrence | Complexe à déboguer | Tester manuellement 5 requêtes parallèles |
| `karma.conf.js` customLaunchers | Piège Windows vs Linux CI | Tester avec `--browsers=ChromeHeadless` |
| `type=gha,mode=max` | Options BuildKit peu documentées | Lire doc BuildKit `--cache-to` |
| Tests Angular avec mocks | `TestBed` + `provideRouter` + `provideHttpClient` | Écrire plus de tests `refresh.interceptor` |
| Multi-stage Dockerfile | Ordre `COPY` critique | Benchmark avant/après inversion |
| Healthcheck IPv6 | Bug subtil busybox wget | Documenté dans COMMANDES.md section 13.4 |
| Skip policy Spring Batch | Peut masquer des erreurs | Log détaillé des skips |

### Statistiques

- **Tests backend** : 115 (100 unit + 15 IT)
- **Tests frontend** : **31** (nouveaux)
- **Total** : **146 tests** ✅
- **Q/R entretien** : 215
- **Commits** : ~50
- **Exercices cahier faits** : 60 / 60 ✅
- **Images Docker publiées** : 2 (ghcr.io)
- **CI duration** : 2 min (4 jobs)

### Objectifs semaine 6 (20-26/10/2026)

- **Bloc C (J29)** : README + portfolio + screenshots
- **Déploiement démo** : Fly.io (backend) + Vercel (frontend) + CI/CD auto
- **Tests E2E Cypress** : login → books → logout
- **Révision** : Q1 → Q215 à voix haute (30 min/jour)
- **Angular avancé** : Lazy loading des routes, RxJS avancé (`switchMap`, `catchError`)
- **Kubernetes basics** : Pod, Service, Deployment (théorie + minikube)
- **Observabilité** : Micrometer + Prometheus (optionnel)

---

## 📊 Vue d'ensemble — Progression globale

| Semaine | Thème principal | Tests | Q/R | Commits |
|---------|-----------------|-------|-----|---------|
| S1 | Java moderne | 0 | 0 | 0 |
| S2 | Spring Core + REST + JPA | ~50 | 40 | ~15 |
| S3 | Relations + Tests + Security | 115 | 175 | ~30 |
| S4 | Docker + Frontend + Déploiement | 115 | 190 | ~40 |
| S5 | Spring Batch + Angular Auth + CI/CD | **146** | **215** | ~50 |

### Répartition des tests

| Catégorie | Nombre |
|-----------|--------|
| Tests backend (JUnit + Mockito) | 100 |
| Tests backend intégration (Failsafe + Testcontainers) | 15 |
| Tests frontend (Karma + Jasmine) | 31 |
| **Total** | **146** |

### Technologies maîtrisées (entretien)

| Domaine | Technologies |
|---------|--------------|
| **Backend** | Java 17 (records, sealed, streams), Spring Boot 3, Spring Security (JWT), Spring Batch, Spring Data JPA |
| **Base de données** | PostgreSQL 16, Flyway (8 migrations), H2 (test) |
| **Frontend** | Angular 19 (standalone, signals, control flow), TypeScript 5, SCSS |
| **Tests** | JUnit 5, Mockito, Testcontainers, Karma, Jasmine, AssertJ |
| **DevOps** | Docker (multi-stage), docker-compose, GitHub Actions (CI/CD), ghcr.io, Fly.io, Vercel |
| **Outils** | Maven, npm, PowerShell, Git |

### Concepts solides (à mentionner en entretien)

- Value Objects auto-validants (`Isbn`, `Money`)
- Records comme DTO
- RFC 7807 (ProblemDetail) pour les erreurs API
- JWT + refresh token avec rotation
- Auto-refresh côté Angular (interceptor + concurrence)
- Dockerfile multi-stage (4× à 8× plus léger)
- nginx SPA routing (`try_files`)
- CI/CD avec cache (Maven, npm, BuildKit GHA)
- Tests Testcontainers (PostgreSQL réel en CI)

### Concepts à revoir (S6)

- Kubernetes (Pod, Deployment, Service)
- Observabilité (Micrometer, Prometheus)
- Cypress E2E
- Angular avancé (lazy loading, RxJS avancé)
- Performance JPA (fetch strategies)

---

## 🎯 Objectifs carrière

### Court terme (fin de formation S6-S8)

- [ ] README + portfolio GitHub complet
- [ ] Démo live (URL partageable)
- [ ] Vidéo démo (Loom, 2 min)
- [ ] Article LinkedIn (retour d'expérience)
- [ ] 3 CV ciblés (backend senior, full-stack, DevOps junior)

### Moyen terme (3-6 mois)

- [ ] Certification Spring Professional (optionnel)
- [ ] Contribuer à 1 projet open source
- [ ] Publier 2 articles techniques
- [ ] Maîtriser Kubernetes en pratique

### Long terme (12 mois)

- [ ] Mission full-stack en ESN ou startup
- [ ] Montée en compétences cloud (AWS/GCP)
- [ ] Mentorat ou enseignement