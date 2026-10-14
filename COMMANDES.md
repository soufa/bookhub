# 📘 Commandes utilisées — BookHub

> Référence complète de toutes les commandes Docker, PowerShell, Maven, Git, GitHub Actions, Fly.io et Vercel utilisées dans le projet.
> Chaque commande est expliquée pour servir de mémo rapide et de support d'entretien.

---

## 📑 Table des matières

1. [Docker — Images et conteneurs](#1-docker--images-et-conteneurs)
2. [Docker Compose](#2-docker-compose)
3. [Le Dockerfile backend en détail](#3-le-dockerfile-backend-en-détail-)
4. [PowerShell — Requêtes HTTP](#4-powershell--requêtes-http)
5. [Angular CLI](#5-angular-cli)
6. [Maven](#6-maven)
7. [Git](#7-git)
8. [GitHub Actions — Vue d'ensemble](#8-github-actions--vue-densemble)
9. [Fly.io](#9-flyio)
10. [Vercel](#10-vercel)
11. [Diagnostic et débogage](#11-diagnostic-et-débogage)
12. [Docker — Multi-stage frontend ⭐ J28](#12-docker--multi-stage-frontend--j28)
13. [Nginx — SPA routing et sécurité ⭐ J28](#13-nginx--spa-routing-et-sécurité--j28)
14. [CI/CD GitHub Actions complet ⭐ J28](#14-cicd-github-actions-complet--j28)
15. [Tests Angular (Karma/Jasmine) ⭐ J28](#15-tests-angular-karmajasmine--j28)
16. [Récapitulatif des pièges rencontrés](#-récapitulatif-des-pièges-rencontrés)

---

## 1. Docker — Images et conteneurs

### 1.1 Lister les conteneurs en cours

```powershell
docker ps
```

**Explication** : affiche tous les conteneurs en cours d'exécution.
Colonnes : `CONTAINER ID`, `IMAGE`, `COMMAND`, `CREATED`, `STATUS`, `PORTS`, `NAMES`.

**Utilité** : vérifier que Docker Desktop tourne et quels conteneurs sont actifs.

---

### 1.2 Lister TOUS les conteneurs (y compris arrêtés)

```powershell
docker ps -a
```

**Explication** : `-a` = `--all` → inclut les conteneurs arrêtés.

**Utilité** : déboguer un conteneur qui a crashé (voir son état `Exited`).

---

### 1.3 Construire une image Docker

```powershell
cd C:\Formation\bookhub\backend
docker build -t bookhub-backend:0.1.0 .
```

**Explication** :
- `-t bookhub-backend:0.1.0` → nom et tag de l'image
- `.` → contexte de build (dossier courant)

**Attendu** : build réussi, image visible dans `docker images`.

**Utilité** : construire l'image du backend Spring Boot à partir du `Dockerfile` multi-stage.

---

### 1.4 Lister les images Docker

```powershell
docker images bookhub-backend
```

**Explication** : filtre les images contenant `bookhub-backend`.

**Attendu** :
```
REPOSITORY        TAG       IMAGE ID       CREATED         SIZE
bookhub-backend   0.1.0     abc123def456   2 minutes ago   180MB
```

**Utilité** : vérifier la taille de l'image (multi-stage = ~180 Mo vs ~600 Mo pour un build naïf).

---

### 1.5 Lancer un conteneur manuellement

```powershell
docker run --rm -p 8080:8080 `
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/bookhub `
  -e SPRING_DATASOURCE_USERNAME=bookhub `
  -e SPRING_DATASOURCE_PASSWORD=bookhub `
  bookhub-backend:0.1.0
```

**Explication** :
- `--rm` → supprime le conteneur à l'arrêt
- `-p 8080:8080` → expose le port 8080 du conteneur sur le port 8080 de l'hôte
- `-e` → variable d'environnement
- `host.docker.internal` → adresse de la machine hôte depuis un conteneur (Windows/Mac)

**Utilité** : tester l'image en isolation sans Compose.

---

### 1.6 Se connecter en shell à un conteneur

```powershell
docker exec -it bookhub-backend sh
```

**Explication** :
- `exec` → exécute une commande dans un conteneur **en cours d'exécution**
- `-it` → mode interactif + TTY
- `sh` → shell (alpine utilise `sh`, pas `bash`)

**Utilité** : explorer les fichiers du conteneur, tester `wget`/`curl` internes, voir les variables d'environnement.

---

### 1.7 Voir les logs d'un conteneur

```powershell
docker logs bookhub-backend --tail 50
```

**Explication** : affiche les 50 dernières lignes de logs du conteneur.

**Utilité** : diagnostiquer un crash, voir les erreurs Spring Boot.

---

### 1.8 Supprimer un conteneur

```powershell
docker rm -f <container_id>
```

**Explication** :
- `rm` → supprime le conteneur
- `-f` → force l'arrêt s'il tourne

**Utilité** : nettoyer les conteneurs Testcontainers réutilisés (`withReuse(true)`).

---

### 1.9 Lancer un conteneur Postgres temporaire pour requêtes

```powershell
docker run --rm -it postgres:16-alpine psql "postgres://user:pass@host:port/db" -c "SELECT * FROM table;"
```

**Explication** :
- Lance un conteneur `postgres:16-alpine` avec `psql`
- `--rm` → supprime après usage
- `-c` → exécute la commande SQL puis quitte

**Utilité** : requêter une DB PostgreSQL distante depuis Windows (sans installer psql).

---

## 2. Docker Compose

### 2.1 Démarrer tous les services

```powershell
cd C:\Formation\bookhub
docker-compose up -d --build
```

**Explication** :
- `up` → démarre tous les services
- `-d` → mode détaché (arrière-plan)
- `--build` → rebuild les images avant de démarrer

**Attendu** :
```
[+] Running 4/4
 ✔ Network bookhub_bookhub-net    Created
 ✔ Volume "bookhub_pgdata"        Created
 ✔ Container bookhub-postgres     Healthy
 ✔ Container bookhub-backend      Started
 ✔ Container bookhub-frontend     Started
 ✔ Container bookhub-adminer      Started
```

---

### 2.2 Voir l'état des services Compose

```powershell
docker-compose ps
```

**Attendu** :
```
NAME               SERVICE    STATUS
bookhub-adminer    adminer    Up
bookhub-backend    backend    Up
bookhub-frontend   frontend   Up (healthy)
bookhub-postgres   postgres   Up (healthy)
```

---

### 2.3 Voir la configuration résolue

```powershell
docker-compose config
```

**Explication** : affiche le YAML final après fusion des `.env`, variables, etc.

**Utilité** : vérifier que les variables d'environnement sont bien substituées.

---

### 2.4 Voir les logs d'un service

```powershell
docker-compose logs -f backend
```

**Explication** :
- `-f` → follow (temps réel)
- `backend` → nom du service

**Utilité** : observer le démarrage Spring Boot en direct.

---

### 2.5 Arrêter tous les services

```powershell
docker-compose down
```

**Explication** : arrête et supprime les conteneurs, **conserve les volumes**.

---

### 2.6 Arrêter ET supprimer les volumes

```powershell
docker-compose down -v
```

**Explication** :
- `-v` → supprime aussi les volumes
- ⚠️ **Perte de données** : la DB PostgreSQL sera vide

**Utilité** : repartir de zéro (utile pour tester les migrations Flyway).

---

### 2.7 Rebuilder un seul service

```powershell
docker-compose up -d --build backend
docker-compose up -d --build frontend
```

**Utilité** : recompiler un service après un changement de code.

---

### 2.8 Forcer un rebuild sans cache

```powershell
docker-compose build --no-cache backend
docker-compose up -d
```

**Explication** :
- `--no-cache` → ignore le cache Docker → rebuild complet
- Plus lent (~3-5 min) mais garantit que les modifs sont prises

**Utilité** : quand Docker garde une vieille couche (souvent après un changement de code Java).

---

## 3. Le Dockerfile backend en détail ⭐

### 3.1 Le fichier complet

Le `Dockerfile` du backend est un **build multi-stage**. Il utilise deux images :
- **Étape 1 (build)** : Maven + JDK pour compiler le JAR
- **Étape 2 (runtime)** : JRE alpine pour exécuter le JAR

```dockerfile
# ========================================
# Étape 1 : BUILD
# ========================================
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copier le pom.xml en premier pour cacher les dépendances Maven
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copier le source et builder
COPY src ./src
RUN mvn -B clean package -DskipTests

# ========================================
# Étape 2 : RUNTIME
# ========================================
FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app

# Créer un utilisateur non-root (sécurité)
RUN addgroup -S app && adduser -S app -G app
USER app

# Copier le JAR depuis l'étape 1
COPY --from=build /app/target/*.jar app.jar

# Exposer le port 8080
EXPOSE 8080

# Lancer l'application avec ZGC (Garbage Collector basse latence)
ENTRYPOINT ["java", "-XX:+UseZGC", "-jar", "app.jar"]
```

---

### 3.2 Explication ligne par ligne

#### Étape 1 — Build

| Ligne | Explication |
|-------|-------------|
| `FROM maven:3.9-eclipse-temurin-17 AS build` | Part de l'image Maven + JDK 17. Le nom `build` permet d'y référer plus tard. |
| `WORKDIR /app` | Définit le dossier de travail dans le conteneur. |
| `COPY pom.xml .` | Copie **uniquement** le `pom.xml` d'abord. |
| `RUN mvn -B dependency:go-offline` | Télécharge toutes les dépendances Maven **avant** le code. |
| `COPY src ./src` | Copie le code source **après** les dépendances. |
| `RUN mvn -B clean package -DskipTests` | Compile le JAR sans exécuter les tests. |

**Pourquoi cet ordre ?** Docker met en cache chaque `RUN`. Si vous modifiez **un seul fichier** Java :
- Sans cet ordre → **toutes** les dépendances Maven sont retéléchargées (~2 min)
- Avec cet ordre → seulement le code est recompilé (~20 s)

**C'est la clé d'un Dockerfile efficace.**

#### Étape 2 — Runtime

| Ligne | Explication |
|-------|-------------|
| `FROM eclipse-temurin:17-jre-alpine AS runtime` | Part d'une image **JRE uniquement** (pas de JDK), sur base Alpine (5 Mo). |
| `WORKDIR /app` | Dossier de travail. |
| `RUN addgroup -S app && adduser -S app -G app` | Crée un utilisateur non-root `app`. |
| `USER app` | **Toutes les commandes suivantes s'exécutent en tant qu'`app`** (sécurité). |
| `COPY --from=build /app/target/*.jar app.jar` | Récupère le JAR de l'étape `build`. |
| `EXPOSE 8080` | Documente que l'app écoute sur 8080 (non effectif, mais convention). |
| `ENTRYPOINT ["java", "-XX:+UseZGC", "-jar", "app.jar"]` | Lance l'app. `-XX:+UseZGC` = GC à très faible pause (< 10 ms). |

---

### 3.3 Pourquoi le multi-stage ?

**Sans multi-stage** (image naïve) :

```dockerfile
FROM maven:3.9-eclipse-temurin-17
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
ENTRYPOINT ["java", "-jar", "target/app.jar"]
```

**Image finale** : ~**700 Mo** (JDK + Maven + dépendances + sources).

**Avec multi-stage** : image finale ~**180 Mo** (JRE + JAR uniquement).

**Gain** : **4× plus léger**.

**Bénéfices** :
1. **Déploiement plus rapide** (moins de Mo à transférer)
2. **Surface d'attaque réduite** (pas de Maven, pas de compilateur en prod)
3. **Coût de stockage réduit** (GHCR, Fly.io, etc.)
4. **Meilleure sécurité** (utilisateur non-root, pas d'outils de build)

---

### 3.4 Le fichier `.dockerignore`

À côté du `Dockerfile`, il y a un fichier `.dockerignore` qui **exclut** des fichiers du contexte de build :

```
target/
.git/
.idea/
*.iml
*.log
README.md
```

**Sans ce fichier**, Docker enverrait :
- Le dossier `target/` (~50 Mo de compilation précédente)
- Le dossier `.git/` (~100 Mo d'historique Git)
- Les fichiers de l'IDE (~10 Mo)

**Résultat** : contexte de 350+ Mo au lieu de 5 Mo.

**Règle** : toujours mettre `target/`, `.git/` et `.idea/` dans `.dockerignore`.

---

### 3.5 Le fichier `fly.toml` (pour Fly.io)

Fly.io utilise le `Dockerfile` mais lit sa config depuis `fly.toml` :

```toml
app = 'bookhub-backend-soufa'
primary_region = 'cdg'

[build]
  dockerfile = 'Dockerfile'

[env]
  SERVER_PORT = '8080'
  SPRING_PROFILES_ACTIVE = 'prod'

[http_service]
  internal_port = 8080
  force_https = true
  auto_stop_machines = 'off'
  auto_start_machines = true
  min_machines_running = 0

[[vm]]
  memory = '512mb'
  cpus = 1
```

**Explication des sections** :

| Section | Rôle |
|---------|------|
| `app` | Nom de l'app Fly.io |
| `primary_region` | Région principale (cdg = Paris) |
| `[build]` | Chemin vers le Dockerfile |
| `[env]` | Variables d'environnement non-sensibles (les secrets vont dans `fly secrets set`) |
| `[http_service]` | Configuration HTTP (port, HTTPS, autostop) |
| `[[vm]]` | Taille de la VM (CPU, RAM) |

---

### 3.6 Commandes de build utiles

#### Construire sans cache

```powershell
docker build --no-cache -t bookhub-backend:0.1.0 .
```

**Utilité** : forcer la reconstruction complète (utile après un changement de `pom.xml`).

#### Voir les couches de l'image

```powershell
docker history bookhub-backend:0.1.0
```

**Explication** : montre chaque couche Docker et sa taille.

**Utilité** : identifier les couches volumineuses.

#### Inspecter l'image

```powershell
docker inspect bookhub-backend:0.1.0
```

**Explication** : affiche les métadonnées JSON (entrypoint, variables, layers).

---

## 4. PowerShell — Requêtes HTTP

### 4.1 Login

```powershell
$login = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
  -Method Post `
  -ContentType "application/json" `
  -Body '{"username":"admin","password":"admin123"}'
$token = $login.token
```

**Explication** :
- `` ` `` (backtick) = continuation de ligne PowerShell
- `Invoke-RestMethod` = client HTTP natif PowerShell
- `$login.token` = accès au champ JSON `token`

---

### 4.2 Créer un livre avec authentification

```powershell
$body = @{
    title    = "Effective Java"
    author   = "Joshua Bloch"
    isbn     = "9780134685991"
    price    = 45.00
    currency = "EUR"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/books" `
  -Method Post `
  -ContentType "application/json; charset=utf-8" `
  -Headers @{ Authorization = "Bearer $token" } `
  -Body $body
```

**Explication** :
- `@{ ... }` = hashtable PowerShell
- `| ConvertTo-Json` = convertit en JSON
- `-Headers @{ Authorization = "Bearer $token" }` = header JWT

---

### 4.3 Lister les livres

```powershell
$list = Invoke-RestMethod -Uri "http://localhost:8080/api/books?size=100" `
  -Headers @{ Authorization = "Bearer $token" }

Write-Host "Total : $($list.totalElements)"
$list.content | ForEach-Object { Write-Host "  - $($_.title)" }
```

**Explication** :
- `?size=100` → pagination Spring Data (100 items par page)
- `$($list.totalElements)` → interpolation de propriété
- `ForEach-Object` → boucle sur les résultats

---

### 4.4 Test avec try/catch (voir le body d'erreur)

```powershell
try {
    $r = Invoke-RestMethod -Uri "http://localhost:8080/api/books" `
      -Method Post -ContentType "application/json" `
      -Headers @{ Authorization = "Bearer $token" } `
      -Body $body -TimeoutSec 30
    Write-Host "✅ $($r.id)"
} catch {
    Write-Host "❌ Erreur : $($_.Exception.Message)"
    $stream = $_.Exception.Response.GetResponseStream()
    if ($stream) {
        Write-Host "Body :" (New-Object System.IO.StreamReader($stream)).ReadToEnd()
    }
}
```

**Explication** :
- `$_` = objet courant dans un bloc catch
- `GetResponseStream()` = récupère le body HTTP d'erreur

**Utilité** : voir le message d'erreur complet d'un 400/500.

---

### 4.5 Boucle de retry (tester la stabilité)

```powershell
for ($i = 1; $i -le 5; $i++) {
    try {
        $r = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
          -Method Post -ContentType "application/json" `
          -Body '{"username":"admin","password":"admin123"}'
        Write-Host "  Essai $i : ✅ $($r.username)"
    } catch {
        Write-Host "  Essai $i : ❌ $($_.Exception.Message)"
    }
    Start-Sleep -Seconds 2
}
```

**Utilité** : détecter une instabilité (cold start, machine en trial, etc.).

---

### 4.6 `curl.exe` (⚠️ à éviter sur Windows)

```powershell
curl.exe -X POST http://localhost:8080/api/books `
  -H "Content-Type: application/json" `
  -d '{"title":"Test","author":"Test","isbn":"1234567890","price":10,"currency":"EUR"}'
```

**⚠️ ATTENTION** : `curl.exe` sur Windows **mange les guillemets** dans `-d` → le JSON arrive cassé au backend.

**Règle** : **Utilisez toujours `Invoke-RestMethod` pour du JSON sur Windows.**

---

## 5. Angular CLI

### 5.1 Vérifier la version

```powershell
ng version
```

**Attendu** :
```
Angular CLI: 19.2.27
Node: 22.11.0 (supported)
```

---

### 5.2 Créer un nouveau projet Angular

```powershell
cd C:\Formation\bookhub
ng new bookhub-frontend --routing --style=scss --skip-git --ssr=false
```

**Explication** :
- `--routing` → active le RouterModule
- `--style=scss` → SCSS au lieu de CSS
- `--skip-git` → ne pas créer un repo Git imbriqué
- `--ssr=false` → pas de Server-Side Rendering

---

### 5.3 Démarrer le serveur de dev

```powershell
cd bookhub-frontend
ng serve
```

**Attendu** :
```
✔ Compiled successfully.
➜  Local:   http://localhost:4200/
```

---

### 5.4 Générer un service

```powershell
ng generate service core/services/book
```

**Explication** : crée `src/app/core/services/book.service.ts` + fichier de test.

**Alias** : `ng g s core/services/book`.

---

### 5.5 Générer un composant

```powershell
ng generate component features/books/book-list
```

**Explication** : crée 4 fichiers : `.ts`, `.html`, `.scss`, `.spec.ts`.

**Alias** : `ng g c features/books/book-list`.

---

### 5.6 Générer un interceptor

```powershell
ng generate interceptor core/interceptors/auth
```

**Explication** : crée un `HttpInterceptorFn` (syntaxe Angular 15+).

---

### 5.7 Générer un guard

```powershell
ng generate guard core/guards/auth
```

**Explication** : Angular demande le type. Choisir **CanActivate**.

---

### 5.8 Build de production

```powershell
ng build --configuration=production
```

**Attendu** :
```
✔ Application bundle generation complete.
Initial total: 250 kB
Output location: dist/bookhub-frontend/browser
```

---

## 6. Maven

### 6.1 Compiler sans tester

```powershell
cd backend
mvn clean compile
```

---

### 6.2 Lancer les tests unitaires

```powershell
mvn test
```

**Explication** : exécute uniquement les tests **Surefire** (`*Test.java`) avec H2 en mémoire.

---

### 6.3 Lancer tous les tests (unitaires + intégration)

```powershell
mvn verify
```

**Explication** :
- Exécute `*Test.java` (Surefire, H2)
- Puis `*IT.java` (Failsafe, Testcontainers PostgreSQL réel)

---

### 6.4 Lancer un seul test

```powershell
# Test unitaire
mvn test "-Dtest=BookControllerTest"

# Test d'intégration
mvn verify "-Dit.test=BookRepositoryIT"
```

**⚠️ Important** : les **guillemets** sont obligatoires sur PowerShell pour éviter que le `.` soit mal interprété.

---

### 6.5 Sauter les tests

```powershell
mvn clean package -DskipTests
```

---

## 7. Git

### 7.1 État du repo

```powershell
git status
```

---

### 7.2 Ajouter tous les changements

```powershell
git add .
```

---

### 7.3 Commit

```powershell
git commit -m "feat(scope): description courte

Corps du commit avec plus de détails.

- Point 1
- Point 2"
```

**Convention Conventional Commits** :
- `feat:` nouvelle fonctionnalité
- `fix:` correction de bug
- `docs:` documentation
- `ci:` CI/CD
- `refactor:` réorganisation de code

---

### 7.4 Push

```powershell
git push
```

**⚠️ Erreur fréquente** :
```
! [rejected] main -> main (fetch first)
```

**Fix** :
```powershell
git pull --rebase origin main
git push
```

---

### 7.5 Voir l'historique récent

```powershell
git log --oneline -10
```

---

### 7.6 Créer un tag (version)

```powershell
git tag v0.1.0
git push origin v0.1.0
```

---

## 8. GitHub Actions — Vue d'ensemble

### 8.1 Voir les workflows

Naviguez vers : **https://github.com/soufa/bookhub/actions**

**Colonnes visibles** :
- Nom du workflow (`CI/CD`)
- Statut (vert = succès, rouge = échec)
- Durée
- Commit associé

---

### 8.2 Re-lancer un workflow

Via l'UI GitHub :
1. Ouvrez le workflow en échec
2. Cliquez sur **Re-run all jobs**

---

### 8.3 Consulter l'image Docker publiée

Naviguez vers : **https://github.com/soufa?tab=packages**

**Vous y verrez** :
- Les packages : `bookhub-backend`, `bookhub-frontend`
- Les tags : `latest`, `sha-xxx`
- La commande de pull

---

## 9. Fly.io

### 9.1 Installer `flyctl`

```powershell
iwr https://fly.io/install.ps1 -useb | iex
```

**Fermez et rouvrez PowerShell** après installation.

---

### 9.2 Se connecter

```powershell
fly auth login
```

---

### 9.3 Lister les apps

```powershell
fly apps list
```

---

### 9.4 Créer un cluster PostgreSQL

```powershell
fly postgres create --name bookhub-db --region cdg
```

**Explication** :
- `cdg` = région Paris (Charles de Gaulle)
- Crée un cluster PostgreSQL 16 managé par Fly
- ⚠️ **Sauvegardez les credentials** affichés à l'écran (jamais réaffichés)

---

### 9.5 Attacher une DB à une app

```powershell
fly postgres attach bookhub-db --app bookhub-backend-soufa
```

**Explication** :
- Crée une base dédiée à l'app
- Crée un user dédié
- **Injecte automatiquement `DATABASE_URL`** dans les secrets de l'app

---

### 9.6 Ajouter des secrets

```powershell
fly secrets set `
  APP_JWT_SECRET="votre-secret-64-chars" `
  APP_CORS_ALLOWED_ORIGINS="https://bookhub-frontend.vercel.app" `
  --app bookhub-backend-soufa
```

---

### 9.7 Lister les secrets

```powershell
fly secrets list --app bookhub-backend-soufa
```

**Attendu** :
```
NAME                       │ DIGEST           │ STATUS
APP_JWT_SECRET             │ 903509eecc7cf708 │ Staged
APP_CORS_ALLOWED_ORIGINS   │ 8e466f8457b9bbc6 │ Staged
DATABASE_URL               │ 4c9a2e89af4be207 │ Staged
```

**`Staged`** = sera appliqué au prochain `fly deploy`.

---

### 9.8 Déployer

```powershell
cd backend
fly deploy --app bookhub-backend-soufa
```

**Attendu** :
```
==> Building image
==> Pushing image
==> Creating release
Visit your newly deployed app at https://bookhub-backend-soufa.fly.dev/
```

---

### 9.9 Voir l'état de l'app

```powershell
fly status --app bookhub-backend-soufa
```

---

### 9.10 Lister les machines

```powershell
fly machines list --app bookhub-backend-soufa
```

---

### 9.11 Démarrer une machine arrêtée

```powershell
fly machines start <MACHINE_ID> --app bookhub-backend-soufa
```

---

### 9.12 Détruire une machine

```powershell
fly machine destroy <MACHINE_ID> --app bookhub-backend-soufa
```

---

### 9.13 Voir les logs

```powershell
fly logs --app bookhub-backend-soufa
fly logs --app bookhub-backend-soufa --no-tail
```

**Filtrer** :
```powershell
fly logs --app bookhub-backend-soufa --no-tail | Select-String "Exception|ERROR" | Select-Object -Last 30
```

---

### 9.14 SSH dans un conteneur

```powershell
fly ssh console --app bookhub-backend-soufa
```

**Attendu** :
```
Connecting to fdaa:... complete
850352c4e91368:/app#
```

---

### 9.15 Voir les volumes

```powershell
fly volumes list --app bookhub-db
```

---

### 9.16 Proxy local vers une DB Fly

```powershell
fly proxy 5433:5432 --app bookhub-db
```

**Explication** : ouvre un tunnel `localhost:5433` → `bookhub-db.internal:5432`.

**Utilité** : se connecter à la DB avec DBeaver, pgAdmin, ou psql docker.

---

### 9.17 Consulter les IPs publiques

```powershell
fly ips list --app bookhub-backend-soufa
```

---

## 10. Vercel

### 10.1 Installer

```powershell
npm install -g vercel
vercel --version
```

---

### 10.2 Déployer en preview

```powershell
cd bookhub-frontend
vercel
```

**Réponses aux questions** :

| Question | Réponse |
|----------|---------|
| Set up and deploy? | **Y** |
| Which scope? | Votre compte |
| Link to existing project? | **N** |
| Project name? | Défaut ou `bookhub-frontend-soufa` |
| Directory? | **./** |
| Override settings? | **N** |

---

### 10.3 Déployer en production

```powershell
vercel --prod
```

---

### 10.4 Lister les déploiements

```powershell
vercel list
```

---

### 10.5 Voir les logs

```powershell
vercel logs https://bookhub-frontend.vercel.app
```

---

## 11. Diagnostic et débogage

### 11.1 Vérifier que Docker tourne

```powershell
docker version
```

**Attendu** : affiche Client **et** Server.

**Si `Server: ERROR`** → Docker Desktop n'est pas démarré.

---

### 11.2 Tester un health check

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/actuator/health"
```

**Attendu** : `{"status":"UP"}`.

---

### 11.3 Vérifier qu'un port est libre

```powershell
netstat -ano | findstr :8080
```

**Fix si occupé** : changez le port dans `docker-compose.yml` (`"8081:8080"`).

---

### 11.4 Vérifier qu'un container Testcontainers existe

```powershell
docker ps -a --filter "label=org.testcontainers=true"
```

---

### 11.5 Détruire TOUS les containers Testcontainers

```powershell
docker ps -a --filter "label=org.testcontainers=true" --format "{{.ID}}" | ForEach-Object { docker rm -f $_ }
```

---

### 11.6 Vérifier le contenu d'une DB Fly

**Terminal 1** :
```powershell
fly proxy 5433:5432 --app bookhub-db
```

**Terminal 2** :
```powershell
docker run --rm -it postgres:16-alpine psql "postgres://postgres:PASSWORD@host.docker.internal:5433/bookhub_backend_soufa" -c "SELECT id, title FROM books ORDER BY id;"
```

---

### 11.7 Vérifier la structure d'une table

```powershell
docker run --rm -it postgres:16-alpine psql "postgres://postgres:PASSWORD@host.docker.internal:5433/bookhub_backend_soufa" -c "\d books"
```

---

### 11.8 Tester une connexion SSH Fly + commandes internes

```powershell
fly ssh console --app bookhub-backend-soufa
```

Une fois dedans :

```bash
# Variables d'environnement
env | grep -E "DATABASE_URL|APP_JWT|SPRING_PROFILES"

# Tester un endpoint interne
wget -qO- http://localhost:8080/actuator/health

# Quitter
exit
```

---

## 12. Docker — Multi-stage frontend ⭐ J28

### 12.1 Le Dockerfile frontend complet

Le Dockerfile frontend utilise **2 étapes** : build Angular + runtime nginx.

```dockerfile
# ============================================================
# Étape 1 : Build Angular
# ============================================================
FROM node:22-alpine AS build
WORKDIR /app

# Copier les package.json en premier → cache npm ci
COPY package*.json ./
RUN npm ci --no-audit --no-fund

# Copier le code source
COPY . .

# Build de production
RUN npm run build -- --configuration=production

# ============================================================
# Étape 2 : Runtime nginx alpine
# ============================================================
FROM nginx:1.27-alpine AS runtime

# Config nginx personnalisée
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copier les assets Angular servis par nginx
COPY --from=build /app/dist/bookhub-frontend/browser /usr/share/nginx/html

# Sécurité : utilisateur non-root
RUN chown -R nginx:nginx /usr/share/nginx/html && \
    chmod -R 755 /usr/share/nginx/html

EXPOSE 80

# Healthcheck (voir 13.4 pour le piège IPv6)
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD wget -q -O /dev/null http://127.0.0.1/ || exit 1

CMD ["nginx", "-g", "daemon off;"]
```

### 12.2 Comparaison backend vs frontend

| Aspect | Backend (`backend/Dockerfile`) | Frontend (`bookhub-frontend/Dockerfile`) |
|--------|-------------------------------|------------------------------------------|
| Build | `maven:3.9-eclipse-temurin-17` | `node:22-alpine` |
| Commande build | `mvn -B clean package -DskipTests` | `npm run build -- --configuration=production` |
| Runtime | `eclipse-temurin:17-jre-alpine` | `nginx:1.27-alpine` |
| Port | 8080 | 80 |
| Taille finale | ~180 Mo | **~74 Mo** |
| Healthcheck | Actuator `/actuator/health` | `wget http://127.0.0.1/` |

**Gains du multi-stage** :

| Métrique | Single-stage | Multi-stage | Ratio |
|----------|--------------|-------------|-------|
| Frontend | ~600 Mo | **74 Mo** | 8× |
| Backend | ~700 Mo | **180 Mo** | 4× |
| Surface d'attaque | JDK + Maven + npm en prod | JRE/nginx uniquement | Réduite |

### 12.3 Explication ligne par ligne — Dockerfile frontend

#### Étape 1 — Build Angular

| Ligne | Explication | Pourquoi c'est important |
|-------|-------------|--------------------------|
| `FROM node:22-alpine AS build` | Image Node 22 sur Alpine (~50 Mo). `AS build` = nom pour référence ultérieure | Alpine réduit la taille. Node 22 = LTS alignée avec `package.json` |
| `WORKDIR /app` | Définit `/app` comme dossier de travail | Toutes les commandes suivantes s'exécutent depuis `/app` |
| `COPY package*.json ./` | Copie **uniquement** `package.json` + `package-lock.json` | Clé du cache : si le code change, mais pas les deps → `npm ci` est skippé |
| `RUN npm ci --no-audit --no-fund` | Installation **reproductible** des dépendances | `npm ci` > `npm install` (utilise le lockfile, plus rapide, plus fiable) |
| `COPY . .` | Copie tout le code source | **APRÈS** `npm ci` → modif de code ne réinstalle pas les deps |
| `RUN npm run build -- --configuration=production` | Build de prod (AOT, minification, tree-shaking) | Génère `dist/bookhub-frontend/browser/` |

**Règle d'or Dockerfile** : ordonner du **moins changeant** au **plus changeant**.

```
COPY package*.json → npm ci      (change rarement → caché)
COPY . .          → npm run build (change souvent → rejoué)
```

**Sans cette règle** : chaque modification de code relance `npm ci` (~90 s). **Avec** : ~20 s.

#### Étape 2 — Runtime nginx

| Ligne | Explication |
|-------|-------------|
| `FROM nginx:1.27-alpine AS runtime` | Image nginx alpine (~40 Mo) |
| `COPY nginx.conf ...` | Config SPA + sécurité (voir 13) |
| `COPY --from=build /app/dist/.../browser /usr/share/nginx/html` | **Récupère les assets** de l'étape build |
| `RUN chown -R nginx:nginx ...` | Change le propriétaire → nginx peut servir les fichiers en tant que user non-root |
| `EXPOSE 80` | Documente le port (convention, non effectif) |
| `HEALTHCHECK ...` | Voir 13.4 |
| `CMD ["nginx", "-g", "daemon off;"]` | Démarre nginx au **premier plan** (pas `-d` → Docker peut tracker le process) |

**Pourquoi `--from=build` ?** : Docker a accès aux artefacts des étapes précédentes. On ne copie **que** le résultat final (assets), pas Node + npm + node_modules.

### 12.4 `.dockerignore` frontend

```
node_modules
npm-debug.log*
dist
.angular
coverage
.idea
.vscode
*.iml
*.swp
.git
.gitignore
test-results
README.md
```

**Règles à retenir** :

| À exclure | Pourquoi |
|-----------|----------|
| `node_modules/` | 500 Mo → recréé par `npm ci` dans l'image |
| `dist/` | Build local → recréé par `npm run build` |
| `.git/` | 100+ Mo d'historique |
| `.idea/`, `.vscode/` | Config IDE inutile |
| `coverage/` | Rapports de test |

| À NE PAS exclure | Pourquoi |
|------------------|----------|
| `Dockerfile` | Nécessaire au build |
| `nginx.conf` | **Copié** dans l'image (`COPY nginx.conf ...`) |
| `package.json` + `package-lock.json` | Nécessaires pour `npm ci` |

**⚠️ Piège vécu (J28)** : j'ai mis `nginx.conf` dans `.dockerignore` → erreur `CopyIgnoredFile: Attempting to Copy file "nginx.conf" that is excluded by .dockerignore`.

**Règle** : **ne jamais exclure un fichier utilisé par `COPY` dans le Dockerfile**.

### 12.5 Commandes Docker Compose J28

#### Démarrer la stack complète (4 services)

```powershell
cd C:\Formation\bookhub
docker-compose up -d --build
```

#### Vérifier l'état

```powershell
docker-compose ps
```

**Attendu** :

```
NAME               SERVICE    STATUS
bookhub-adminer    adminer    Up
bookhub-backend    backend    Up
bookhub-frontend   frontend   Up (healthy)   ← grâce au fix IPv6
bookhub-postgres   postgres   Up (healthy)
```

#### Rebuild un seul service (frontend)

```powershell
docker-compose up -d --build frontend
```

**Astuce** : le cache Docker détecte automatiquement que seule l'étape concernée change. Le `npm ci` est réutilisé si `package.json` ne change pas.

#### Voir les logs nginx

```powershell
docker logs bookhub-frontend --tail 10
```

**Attendu** :

```
GET / HTTP/1.1" 200 501
GET /books HTTP/1.1" 200 501      ← SPA routing fonctionne !
```

**Le `200` sur `/books`** prouve que `try_files` renvoie bien `index.html`.

---

## 13. Nginx — SPA routing et sécurité ⭐ J28

### 13.1 Le fichier `nginx.conf` complet

```nginx
server {
    listen 80;
    server_name _;
    root /usr/share/nginx/html;
    index index.html;

    # Compression gzip
    gzip on;
    gzip_vary on;
    gzip_min_length 1024;
    gzip_types text/css application/javascript application/json image/svg+xml;

    # ⭐ SPA routing : toute route inconnue renvoie index.html
    location / {
        try_files $uri $uri/ /index.html;
    }

    # Cache long pour les assets (hash dans le nom)
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
        access_log off;
    }

    # index.html jamais mis en cache
    location = /index.html {
        add_header Cache-Control "no-cache, no-store, must-revalidate";
        expires 0;
    }

    # Headers de sécurité
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-XSS-Protection "1; mode=block" always;
    add_header Referrer-Policy "strict-origin-when-cross-origin" always;

    server_tokens off;
}
```

### 13.2 ⭐ `try_files` — le cœur du SPA routing

**Sans cette ligne** :

| Requête | Comportement nginx | Résultat |
|---------|-------------------|----------|
| `GET /` | Renvoie `index.html` | ✅ Angular charge |
| `GET /login` (F5) | Cherche un fichier `login` → 404 | ❌ **Page blanche** |
| `GET /books` (F5) | Cherche un fichier `books` → 404 | ❌ **Page blanche** |

**Avec cette ligne** :

| Requête | Comportement nginx | Résultat |
|---------|-------------------|----------|
| `GET /` | Renvoie `index.html` | ✅ Angular charge |
| `GET /login` (F5) | Renvoie `index.html` → Angular Router → LoginComponent | ✅ |
| `GET /books` (F5) | Renvoie `index.html` → Angular Router → BookListComponent | ✅ |

**Traduction** : `try_files` essaie dans l'ordre :
1. `$uri` : le fichier existe-t-il exactement ? (ex: `/main.js` existe)
2. `$uri/` : le dossier existe-t-il ? (ex: `/assets/`)
3. `/index.html` : sinon, renvoie toujours `index.html`

**C'est nginx qui fait le "routing" pour les URLs inconnues**, puis Angular Router prend le relais côté client.

### 13.3 Cache control — assets vs index.html

**Assets** (`main.HASH.js`, `styles.HASH.css`) :

```nginx
expires 1y;
add_header Cache-Control "public, immutable";
```

**Pourquoi 1 an** : Angular ajoute un **hash** dans le nom (`main-LYOV7UBA.js`). Si le contenu change, le hash change → nouveau nom de fichier → pas de conflit de cache.

**index.html** :

```nginx
add_header Cache-Control "no-cache, no-store, must-revalidate";
expires 0;
```

**Pourquoi pas de cache** : `index.html` référence les fichiers JS par leur hash. Si l'utilisateur garde un vieux `index.html` en cache, il chargera les vieux JS (qui n'existent plus). Donc `index.html` doit **toujours** être frais.

### 13.4 ⚠️ Healthcheck busybox wget — piège IPv6

```dockerfile
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD wget -q -O /dev/null http://127.0.0.1/ || exit 1
```

**⚠️ Utilisez `127.0.0.1`, PAS `localhost`** — sinon :

```
wget: can't connect to remote host: Connection refused
```

**Pourquoi** :
1. `localhost` résout en `::1` (IPv6) **d'abord** sur Alpine
2. nginx dans `nginx:alpine` écoute **IPv4 only** (`0.0.0.0:80`)
3. → `Connection refused`
4. Container marqué `unhealthy` alors que le service répond parfaitement **de l'extérieur** (port forwarding Docker sur IPv4)

**Diagnostic** :

```powershell
# Voir l'état du healthcheck
docker inspect bookhub-frontend --format "{{.State.Health.Status}}"
# → unhealthy

# Voir les logs d'échec
docker inspect bookhub-frontend --format "{{json .State.Health.Log}}"
# → "Output":"wget: can't connect to remote host: Connection refused"
```

**Fix vérifié** :

```powershell
docker inspect bookhub-frontend --format "{{.State.Health.Status}}"
# → healthy (après fix)
```

---

## 14. CI/CD GitHub Actions complet ⭐ J28

### 14.1 Le workflow `.github/workflows/ci-cd.yml`

```yaml
name: CI/CD

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

env:
  REGISTRY: ghcr.io
  IMAGE_PREFIX: ${{ github.repository }}

jobs:
  backend-tests:
    name: Backend Tests
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: 'maven'
      - name: Run backend tests
        working-directory: backend
        run: mvn -B clean verify

  frontend-tests:
    name: Frontend Tests
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with:
          node-version: '22'
          cache: 'npm'
          cache-dependency-path: bookhub-frontend/package-lock.json
      - name: Install dependencies
        working-directory: bookhub-frontend
        run: npm ci --no-audit --no-fund
      - name: Run tests
        working-directory: bookhub-frontend
        run: npm test -- --watch=false --browsers=ChromeHeadlessNoSandbox
      - name: Build production
        working-directory: bookhub-frontend
        run: npm run build -- --configuration=production

  docker-backend:
    name: Push Backend Image
    needs: [backend-tests]
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main' && github.event_name == 'push'
    permissions:
      contents: read
      packages: write
    steps:
      - uses: actions/checkout@v4
      - uses: docker/setup-buildx-action@v3
      - uses: docker/login-action@v3
        with:
          registry: ${{ env.REGISTRY }}
          username: ${{ github.actor }}
          password: ${{ secrets.GITHUB_TOKEN }}
      - uses: docker/build-push-action@v6
        with:
          context: ./backend
          push: true
          tags: |
            ${{ env.REGISTRY }}/${{ env.IMAGE_PREFIX }}-backend:latest
            ${{ env.REGISTRY }}/${{ env.IMAGE_PREFIX }}-backend:${{ github.sha }}
          cache-from: type=gha
          cache-to: type=gha,mode=max

  docker-frontend:
    name: Push Frontend Image
    needs: [frontend-tests]
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main' && github.event_name == 'push'
    permissions:
      contents: read
      packages: write
    steps:
      - uses: actions/checkout@v4
      - uses: docker/setup-buildx-action@v3
      - uses: docker/login-action@v3
        with:
          registry: ${{ env.REGISTRY }}
          username: ${{ github.actor }}
          password: ${{ secrets.GITHUB_TOKEN }}
      - uses: docker/build-push-action@v6
        with:
          context: ./bookhub-frontend
          push: true
          tags: |
            ${{ env.REGISTRY }}/${{ env.IMAGE_PREFIX }}-frontend:latest
            ${{ env.REGISTRY }}/${{ env.IMAGE_PREFIX }}-frontend:${{ github.sha }}
          cache-from: type=gha
          cache-to: type=gha,mode=max
```

### 14.2 Explication des 4 jobs

#### Job 1 : `backend-tests`

| Ligne | Explication |
|-------|-------------|
| `runs-on: ubuntu-latest` | Runner GitHub (VM Ubuntu) |
| `actions/checkout@v4` | Clone le repo |
| `actions/setup-java@v4` | Installe JDK 17 Temurin |
| `cache: 'maven'` | Cache `~/.m2/repository` entre runs → tests 3× plus rapides |
| `working-directory: backend` | Exécute dans `backend/` |
| `mvn -B clean verify` | Tests unitaires (Surefire) + intégration (Failsafe + Testcontainers) |

**Durée typique** : ~1m 30s (cached) / ~4 min (premier run).

#### Job 2 : `frontend-tests`

| Ligne | Explication |
|-------|-------------|
| `actions/setup-node@v4` | Installe Node 22 |
| `cache-dependency-path: bookhub-frontend/package-lock.json` | Cache `node_modules` |
| `npm ci --no-audit --no-fund` | Installation **reproductible** (lockfile) |
| `npm test -- --watch=false --browsers=ChromeHeadlessNoSandbox` | Lance Karma une fois, en headless |
| `npm run build -- --configuration=production` | Vérifie que le build prod passe |

**Durée typique** : ~45s (cached).

**⚠️ Chrome sur Ubuntu CI** : Chrome est déjà installé sur les runners GitHub. Le `ChromeHeadlessNoSandbox` est nécessaire car les containers CI n'ont pas les droits pour le sandbox Chrome.

#### Jobs 3-4 : `docker-backend` et `docker-frontend`

| Ligne | Explication |
|-------|-------------|
| `needs: [backend-tests]` | Le job démarre **seulement si** les tests passent |
| `if: github.ref == 'refs/heads/main' && github.event_name == 'push'` | **Skip** sur PR → économise des minutes + pas d'image de branche |
| `permissions: packages: write` | **Obligatoire** pour pousser sur ghcr.io (sinon `denied`) |
| `docker/setup-buildx-action@v3` | BuildKit pour builds parallèles + cache |
| `docker/login-action@v3` | Login ghcr.io avec `GITHUB_TOKEN` (auto-fourni) |
| `docker/build-push-action@v6` | Build + push en une action |
| `tags: latest + sha` | 2 tags : `latest` (mouvant) + commit sha (immuable) |
| `cache-from/to: type=gha` | Cache Docker dans GitHub Actions → builds 3× plus rapides |

**Durée typique** :
- Backend : 30 s (cached)
- Frontend : 1m 8s (premier run) / 25s (cached)

### 14.3 Le pattern `if:` — quand un job ne doit pas tourner

```yaml
if: github.ref == 'refs/heads/main' && github.event_name == 'push'
```

**Cas d'usage** : les jobs `docker-*` poussent des images. On ne veut **pas** :
- Pousser une image sur chaque PR (pollution de ghcr.io)
- Consommer des minutes de CI inutilement

**Variantes utiles** :

| Condition | Effet |
|-----------|-------|
| `github.ref == 'refs/heads/main'` | Seulement sur main |
| `github.event_name == 'push'` | Seulement sur push (pas PR) |
| `github.event_name == 'pull_request'` | Seulement sur PR |
| `startsWith(github.ref, 'refs/tags/')` | Seulement sur tag (release) |
| `!contains(github.event.head_commit.message, '[skip ci]')` | Skip si commit contient `[skip ci]` |

### 14.4 Cache GitHub Actions (`cache-from: type=gha`)

**Sans cache** :

```
docker-backend: 4 min (rebuild complet)
```

**Avec cache (`type=gha`)** :

```
docker-backend: 29 s (layers réutilisés)
```

**Comment ça marche** :
1. Le cache est stocké dans GitHub Actions (10 GB gratuit)
2. `cache-from: type=gha` : lit le cache
3. `cache-to: type=gha,mode=max` : écrit le cache (max = toutes les layers)

**Règle d'or** : toujours activer `type=gha` pour les builds Docker en CI.

### 14.5 Vérifier le CI et les images

**Via l'UI GitHub** :

```
https://github.com/soufa/bookhub/actions
```

Vous voyez :
- Workflow `CI/CD`
- Statut : ✅ vert
- 4 jobs listés avec leur durée

**Vérifier les images publiées** :

```
https://github.com/soufa?tab=packages
```

**Tester le pull** :

```powershell
docker pull ghcr.io/soufa/bookhub-backend:latest
docker pull ghcr.io/soufa/bookhub-frontend:latest
```

**Attendu** : images téléchargées.

---

## 15. Tests Angular (Karma/Jasmine) ⭐ J28

### 15.1 Le fichier `karma.conf.js`

Angular 17+ ne génère plus `karma.conf.js` par défaut. Il faut le créer **ET** l'ajouter dans `angular.json`.

```javascript
// karma.conf.js
module.exports = function (config) {
  config.set({
    basePath: '',
    frameworks: ['jasmine', '@angular-devkit/build-angular'],
    plugins: [
      require('karma-jasmine'),
      require('karma-chrome-launcher'),
      require('karma-jasmine-html-reporter'),
      require('karma-coverage'),
      require('@angular-devkit/build-angular/plugins/karma')
    ],
    client: {
      jasmine: {},
      clearContext: false
    },
    jasmineHtmlReporter: {
      suppressAll: true
    },
    coverageReporter: {
      dir: require('path').join(__dirname, './coverage/bookhub-frontend'),
      subdir: '.',
      reporters: [{ type: 'html' }, { type: 'text-summary' }]
    },
    reporters: ['progress', 'kjhtml'],
    browsers: ['ChromeHeadlessNoSandbox'],
    customLaunchers: {
      ChromeHeadlessNoSandbox: {
        base: 'ChromeHeadless',
        flags: [
          '--no-sandbox',
          '--disable-gpu',
          '--disable-dev-shm-usage',
          '--disable-software-rasterizer',
          '--disable-extensions'
        ]
      }
    },
    restartOnFileChange: true,
    singleRun: false,
    browserNoActivityTimeout: 60000,
    captureTimeout: 60000
  });
};
```

**Puis dans `angular.json`** :

```json
"test": {
  "builder": "@angular-devkit/build-angular:karma",
  "options": {
    "polyfills": ["zone.js", "zone.js/testing"],
    "tsConfig": "tsconfig.spec.json",
    "karmaConfig": "karma.conf.js"
  }
}
```

### 15.2 Pourquoi `ChromeHeadlessNoSandbox` ?

Chrome en headless dans un container CI (Docker) **crash** car il ne peut pas accéder au sandbox kernel. Les flags `--no-sandbox --disable-gpu` contournent le problème.

**Symptôme sans flags** :

```
Chrome crashed.
Some of your tests did a full page reload!
```

### 15.3 Lancer les tests

```powershell
cd C:\Formation\bookhub\bookhub-frontend

# Mode watch (dev)
npm test

# Une seule fois (CI)
npm test -- --watch=false --browsers=ChromeHeadlessNoSandbox

# Un seul fichier
npm test -- --watch=false --browsers=ChromeHeadlessNoSandbox --include="**/auth.service.spec.ts"
```

**Attendu** :

```
Chrome Headless (sans sandbox): Executed 31 of 31 SUCCESS
TOTAL: 31 SUCCESS
```

### 15.4 Pattern de test — Service avec HttpClient

```typescript
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();  // vérifie qu'aucune requête n'est restée en attente
  });

  it('should login and store tokens', () => {
    service.login({ username: 'admin', password: 'admin123' }).subscribe();

    const req = httpMock.expectOne('http://localhost:8080/api/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush({ token: 'fake', refreshToken: 'ref', username: 'admin', type: 'Bearer' });

    expect(localStorage.getItem('bookhub.token')).toBe('fake');
  });
});
```

### 15.5 Pattern de test — Interceptor

```typescript
import { TestBed } from '@angular/core/testing';
import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { authInterceptor } from './auth.interceptor';

describe('authInterceptor', () => {
  let http: HttpClient;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting()
      ]
    });
    http = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should add Bearer token', () => {
    localStorage.setItem('bookhub.token', 'my-token');

    http.get('/api/books').subscribe();

    const req = httpMock.expectOne('/api/books');
    expect(req.request.headers.get('Authorization')).toBe('Bearer my-token');
    req.flush({});
  });
});
```

### 15.6 Pièges vécus (J28)

| Piège | Symptôme | Fix |
|-------|----------|-----|
| Test par défaut `ng generate service` incomplet | `NullInjectorError: No provider for HttpClient!` | Ajouter `HttpClientTestingModule` aux imports |
| Validation avant async | `Cannot read properties of undefined (reading 'subscribe')` | Garde `if (!username \|\| !password) return` avant `auth.login()` |
| `AppComponent` non mocké | `NullInjectorError: No provider for AuthService` | `{ provide: AuthService, useValue: { ... } }` |

---

## 🎯 Récapitulatif des pièges rencontrés

| Piège | Symptôme | Fix |
|-------|----------|-----|
| `curl.exe` sur Windows | JSON corrompu | Utiliser `Invoke-RestMethod` |
| `-Dit.test` non reconnu | `Unknown lifecycle phase` | `mvn verify "-Dit.test=X"` |
| Testcontainers trop vieux | `NpipeSocketClientProviderStrategy` | Passer à 1.21.4+ |
| `/error` bloqué Spring Security | 401 au lieu du vrai code | `.requestMatchers("/error").permitAll()` |
| `@AutoConfigureMockMvc` sur sous-classe | MockMvc introuvable | Sur la classe parente |
| DTO ≠ Entity ≠ Request | 400 Validation | Utiliser `CreateBookRequest` |
| `DataSourceConfig` supprime `sslmode` | SSL handshake failed | Ajouter `?sslmode=disable` |
| `AppUserDetailsService` construit au boot | `Empty encoded password` | Construire en lazy |
| Machine Fly trial | Stop après 5 min | Ajouter carte bancaire |
| `git push` rejected | Remote plus récent | `git pull --rebase origin main` |
| Chrome crash headless Windows | `Some of your tests did a full page reload!` | `customLaunchers` avec `--no-sandbox --disable-gpu` |
| Karma sans `karma.conf.js` | `ChromeHeadlessNoSandbox not found` | Créer + ajouter `karmaConfig` dans `angular.json` |
| Test par défaut incomplet | `NullInjectorError: No provider for HttpClient!` | `HttpClientTestingModule` + vrais tests |
| Validation avant async | `Cannot read properties of undefined` | Garde `if (!username \|\| !password) return` |
| `.dockerignore` trop agressif | `CopyIgnoredFile: nginx.conf excluded` | Ne pas exclure les fichiers `COPY`és |
| Healthcheck busybox + IPv6 | `wget: Connection refused` (unhealthy) | Utiliser `127.0.0.1` au lieu de `localhost` |
| ghcr.io permission denied | `denied: permission_denied: write_package` | `permissions: packages: write` |
| CI sur PR (inutile) | Push d'image sur chaque PR | `if: github.ref == 'refs/heads/main'` |

---

## 📚 Ressources

- [Docker docs](https://docs.docker.com/)
- [Docker Compose reference](https://docs.docker.com/compose/compose-file/)
- [Dockerfile best practices](https://docs.docker.com/develop/develop-images/dockerfile_best-practices/)
- [Angular CLI reference](https://angular.dev/cli)
- [Angular Testing Guide](https://angular.dev/guide/testing)
- [Maven lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html)
- [GitHub Actions docs](https://docs.github.com/en/actions)
- [GHCR docs](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry)
- [nginx `try_files`](https://nginx.org/en/docs/http/ngx_http_core_module.html#try_files)
- [Fly.io docs](https://fly.io/docs/)
- [Vercel CLI](https://vercel.com/docs/cli)
- [Conventional Commits](https://www.conventionalcommits.org/)