## 2026-09-22 (mardi) — J1

### ✅ Fait
- Arborescence projet créée
- Spring Boot 3.2.5 + Java 17 configurés
- PostgreSQL 16 en Docker (port 5433)
- Flyway migration V1 exécutée automatiquement
- Endpoint `/api/hello` fonctionnel
- Projet poussé sur GitHub

### ❌ Bloqué (résolu)
- Erreur d'authentification PostgreSQL → résolue en supprimant le volume obsolète
- Conflit port 5432 avec PostgreSQL natif Windows → résolu avec port 5433
- Conflit Spring Security + Actuator → résolu en excluant les 2 auto-configs

### 💡 Appris
- **Docker volume persistance** : `POSTGRES_PASSWORD` n'est utilisé qu'à la 1ère init du volume
- **`docker-compose down -v`** ne supprime pas toujours les volumes en usage
- **Spring Security auto-config** est activée dès que le starter est dans le pom.xml
- **Actuator + Security** sont couplés (d'où l'erreur `HttpSecurity`)
- **Port mapping Docker** : `5433:5432` = 5433 côté Windows, 5432 dans le container

### 🎯 Demain (J2)
- Cours Buchalka : Records + sealed + pattern matching (2h)
- Créer `Isbn.java` et `Money.java` dans `shared/domain/`
- Écrire 8 Q/R entretien (Java Records + Optional)
- Commit GitHub avec 1ᵉʳ code Java moderne