# 📘 Commandes utilisées — Session du 12/10/2026

> Toutes les commandes Docker, PowerShell, Maven, Git et Fly.io utilisées pendant la session J18 → J22.
> Chaque commande est expliquée pour servir de référence rapide.

---

## 📑 Table des matières

1. [Docker — Images et conteneurs](#1-docker--images-et-conteneurs)
2. [Docker Compose](#2-docker-compose)
3. [Le Dockerfile en détail](#3-le-dockerfile-en-détail) ⭐ **NOUVEAU**
4. [PowerShell — Requêtes HTTP](#4-powershell--requêtes-http)
5. [Angular CLI](#5-angular-cli)
6. [Maven](#6-maven)
7. [Git](#7-git)
8. [GitHub Actions](#8-github-actions)
9. [Fly.io](#9-flyio)
10. [Vercel](#10-vercel)
11. [Diagnostic et débogage](#11-diagnostic-et-débogage)
12. [Récapitulatif des pièges](#-récapitulatif-des-pièges-rencontrés)

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
```

**Utilité** : recompiler le backend après un changement de code.

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

## 3. Le Dockerfile en détail ⭐

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

---

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

**Avec multi-stage** :

**Image finale** : ~**180 Mo** (JRE + JAR uniquement).

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

## 8. GitHub Actions

### 8.1 Voir les workflows

Naviguez vers : **https://github.com/soufa/bookhub/actions**

**Colonnes visibles** :
- Nom du workflow (`CI` ou `CD`)
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

Naviguez vers : **https://github.com/soufa/bookhub/pkgs/container/bookhub%2Fbackend**

**Vous y verrez** :
- Les tags : `main`, `latest`, `sha-xxx`
- La commande de pull : `docker pull ghcr.io/soufa/bookhub/backend:sha-xxx`

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

---

## 📚 Ressources

- [Docker docs](https://docs.docker.com/)
- [Docker Compose reference](https://docs.docker.com/compose/compose-file/)
- [Angular CLI reference](https://angular.dev/cli)
- [Maven lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html)
- [Fly.io docs](https://fly.io/docs/)
- [Vercel CLI](https://vercel.com/docs/cli)
- [Conventional Commits](https://www.conventionalcommits.org/)