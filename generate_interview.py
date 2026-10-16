#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Generateur des 3 fichiers d'interview Java Full Stack :
- SpringBoot.md  (50 questions)
- Java.md        (60 questions)
- SQL.md         (100+ questions)

Contenu en francais, termes techniques en anglais.
Mise en page Markdown propre.
"""

import os
import re
from datetime import datetime


# ============================================================================
# FICHIER 1 : SpringBoot.md
# ============================================================================

SPRINGBOOT = r'''# Spring Boot - Questions d'Interview

> **Nombre de questions :** 50
> **Généré le :** {date}

---

## Table des matières

1. [Questions de Base](#1-questions-de-base)
2. [Configuration et Dépendances](#2-configuration-et-dépendances)
3. [Dependency Injection et Beans](#3-dependency-injection-et-beans)
4. [REST et Web Development](#4-rest-et-web-development)
5. [Spring Data et JPA](#5-spring-data-et-jpa)
6. [Sécurité](#6-sécurité)
7. [Microservices](#7-microservices)
8. [Testing](#8-testing)
9. [Monitoring et Actuator](#9-monitoring-et-actuator)
10. [Déploiement](#10-déploiement)

---

## 1. Questions de Base

### 1. Qu'est-ce que Spring Boot et en quoi diffère-t-il du Spring Framework ?

**Réponse :**

Spring Boot est une extension du Spring Framework qui simplifie la configuration et le développement d'applications Spring. Il fournit des configurations par défaut et un serveur embarqué pour réduire le temps de configuration.

**Différences principales :**
- Spring Framework nécessite une configuration XML ou Java extensive
- Spring Boot utilise l'auto-configuration et les starters pour minimiser la configuration
- Spring Boot inclut un serveur embarqué (Tomcat, Jetty, Undertow)

---

### 2. Quels sont les avantages de Spring Boot ?

**Réponse :**

- **Développement rapide** : Configuration minimale requise
- **Auto-configuration** : Configuration automatique basée sur les dépendances
- **Serveur embarqué** : Pas besoin de déployer sur un serveur externe
- **Starters** : Gestion simplifiée des dépendances
- **Production-ready** : Actuator pour le monitoring et les métriques
- **Opinionated defaults** : Configuration par défaut sensée

---

### 3. Comment créer une application Spring Boot ?

**Réponse :**

```java
@SpringBootApplication
public class MyApplication {{
    public static void main(String[] args) {{
        SpringApplication.run(MyApplication.class, args);
    }}
}}
```

**Étapes :**
1. Utiliser Spring Initializr (start.spring.io)
2. Ou ajouter les dépendances dans `pom.xml` / `build.gradle`
3. Créer la classe principale avec `@SpringBootApplication`

### 4. Qu'est-ce qu'un Spring Boot Starter ?

**Réponse :**

Un Starter est un ensemble de descripteurs de dépendances qui simplifie la configuration Maven/Gradle. Il regroupe toutes les dépendances nécessaires pour une fonctionnalité spécifique.

Exemples :

spring-boot-starter-web : Pour les applications web (Spring MVC, Tomcat, Jackson)

spring-boot-starter-data-jpa : Pour JPA avec Hibernate

spring-boot-starter-security : Pour Spring Security

spring-boot-starter-test : Pour les tests (JUnit, Mockito, AssertJ)

### 5. Expliquez le concept d'auto-configuration dans Spring Boot

**Réponse :**

L'auto-configuration configure automatiquement l'application Spring en fonction des dépendances présentes dans le classpath.

Fonctionnement :

Spring Boot examine les JARs présents

Il applique des configurations conditionnelles

Les classes @ConditionalOnClass, @ConditionalOnMissingBean contrôlent l'activation

Exemple : Si H2 est dans le classpath, Spring Boot configure automatiquement une base de données en mémoire.

### 6. Comment définir des propriétés dans une application Spring Boot ?

**Réponse :**

Dans application.properties ou application.yml :

```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

### 7. Qu'est-ce qu'un profil dans Spring Boot et comment l'utiliser ?

**Réponse :**

Un profil permet de séparer les configurations selon l'environnement (dev, test, prod).

Utilisation :

Annotation @Profile("dev") sur les composants

Fichiers application-dev.properties, application-prod.properties

Activation : --spring.profiles.active=dev

```java
@Component
@Profile("dev")
public class DevDataSourceConfig {{
    // Configuration spécifique au développement
}}
```

### 8. Comment implémenter la gestion des exceptions dans Spring Boot ?

**Réponse :**

Utiliser @ControllerAdvice et @ExceptionHandler :

```java
@ControllerAdvice
public class GlobalExceptionHandler {{
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {{
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }}
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {{
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }}
}}
```

### 9. Qu'est-ce que l'annotation @SpringBootApplication ?

**Réponse :**

@SpringBootApplication combine trois annotations :

@Configuration : Indique une classe de configuration

@EnableAutoConfiguration : Active l'auto-configuration

@ComponentScan : Scanne les composants dans le package courant

### 10. Quelle est la différence entre application.properties et bootstrap.properties ?

**Réponse :**

bootstrap.properties : Chargé en premier, utilisé pour Spring Cloud Config (connexion au serveur de configuration)

application.properties : Configuration principale de l'application

## 2. Configuration et Dépendances

### 11. Comment configurer un DataSource dans Spring Boot ?

**Réponse :**

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
```

### 12. Comment activer HTTPS dans Spring Boot ?

**Réponse :**

```properties
server.port=8443
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=changeit
server.ssl.keyStoreType=PKCS12
server.ssl.keyAlias=tomcat
```

### 13. Comment configurer le logging dans Spring Boot ?

**Réponse :**

```properties
logging.level.org.springframework=DEBUG
logging.level.com.example=INFO
logging.file.name=app.log
logging.pattern.console=%d{{yyyy-MM-dd HH:mm:ss}} - %msg%n
```

### 14. Qu'est-ce que @EnableAutoConfiguration ?

**Réponse :**

@EnableAutoConfiguration indique à Spring Boot de configurer automatiquement les beans en fonction des dépendances du classpath et des propriétés définies.

### 15. Comment externaliser la configuration dans Spring Boot ?

**Réponse :**

Ordre de priorité (du plus élevé au plus bas) :

Arguments de ligne de commande

Variables d'environnement

application-{{profile}}.properties

application.properties

@PropertySource

### 16. Qu'est-ce que CommandLineRunner ?

**Réponse :**

Interface pour exécuter du code après le démarrage de l'application.

```java
@Component
public class MyRunner implements CommandLineRunner {{
    @Override
    public void run(String... args) throws Exception {{
        System.out.println("Application started!");
    }}
}}
```

### 17. Comment configurer plusieurs DataSources ?

**Réponse :**

```java
@Configuration
public class DataSourceConfig {{
    @Bean(name = "primaryDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {{
        return DataSourceBuilder.create().build();
    }}
    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {{
        return DataSourceBuilder.create().build();
    }}
}}
```

### 18. Qu'est-ce que @ConfigurationProperties ?

**Réponse :**

Permet de binder des propriétés à un objet Java de façon type-safe.

```java
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {{
    private String name;
    private String version;
    // getters et setters
}}
properties
app.name=MyApp
app.version=1.0.0
```

### 19. Quelle est la différence entre @Value et @ConfigurationProperties ?

**Réponse :**

@Value : Injection de valeurs individuelles, pas de validation

@ConfigurationProperties : Binding de groupes de propriétés, support de la validation, type-safe

### 20. Comment utiliser @ConditionalOnProperty ?

**Réponse :**

```java
@Configuration
@ConditionalOnProperty(
    name = "feature.cache.enabled",
    havingValue = "true",
    matchIfMissing = false
)
public class CacheConfig {{
    // Configuration activée uniquement si la propriété est true
}}
```

## 3. Dependency Injection et Beans

### 21. Qu'est-ce que la Dependency Injection dans Spring Boot ?

**Réponse :**

Design pattern où les dépendances d'un objet sont fournies par le conteneur Spring plutôt que créées par l'objet lui-même.

Types :

Constructor Injection (recommandé)

Setter Injection

Field Injection (déconseillé)

```java
@Service
public class UserService {{
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {{
        this.userRepository = userRepository;
    }}
}}
```

### 22. Qu'est-ce qu'un Spring Bean ?

**Réponse :**

Un Bean est un objet géré par le conteneur Spring IoC. Il est instancié, assemblé et géré par Spring.

Définition :

@Component : Composant générique

@Service : Couche service

@Repository : Couche DAO

@Controller : Contrôleur web

@Bean : Dans une classe @Configuration

### 23. Quelle est la différence entre @Component, @Service, @Repository et @Controller ?

**Réponse :**

@Component : Stéréotype générique pour tout composant Spring

@Service : Spécialisation pour la couche métier

@Repository : Spécialisation pour la couche d'accès aux données (traduction des exceptions)

@Controller : Spécialisation pour les contrôleurs MVC

### 24. Expliquez la différence entre @Primary et @Qualifier

**Réponse :**

@Primary : Définit le bean par défaut quand plusieurs candidats existent

@Qualifier : Spécifie explicitement quel bean injecter

```java
@Bean
@Primary
public MessageService emailService() {{
    return new EmailService();
}}
@Bean
public MessageService smsService() {{
    return new SMSService();
}}
@Autowired
@Qualifier("smsService")
private MessageService messageService;
```

### 25. Comment définir un Bean avec @Bean ?

**Réponse :**

```java
@Configuration
public class AppConfig {{
    @Bean
    public RestTemplate restTemplate() {{
        return new RestTemplate();
    }}
    @Bean
    public ObjectMapper objectMapper() {{
        return new ObjectMapper();
    }}
}}
```

### 26. Quels sont les scopes de Bean dans Spring ?

**Réponse :**

singleton : Une instance par conteneur (défaut)

prototype : Une instance à chaque demande

request : Une instance par requête HTTP

session : Une instance par session HTTP

application : Une instance par ServletContext

### 27. Comment créer une annotation personnalisée ?

**Réponse :**

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {{
    String value() default "";
}}
```

### 28. Qu'est-ce que le cycle de vie d'un Bean Spring ?

**Réponse :**

Instanciation

Population des propriétés

BeanNameAware.setBeanName()

ApplicationContextAware.setApplicationContext()

@PostConstruct / afterPropertiesSet()

Utilisation

@PreDestroy / destroy()

### 29. Comment utiliser @Lookup ?

**Réponse :**

```java
@Component
public abstract class CommandManager {{
    @Lookup
    protected abstract Command createCommand();
}}
```

### 30. Quelle est la différence entre @Resource, @Inject et @Autowired ?

**Réponse :**

@Autowired : Spécifique Spring, injection par type puis par nom

@Resource : JSR-250, injection par nom d'abord

@Inject : JSR-330, standard Java, similaire à @Autowired

## 4. REST et Web Development

### 31. Comment créer un service RESTful dans Spring Boot ?

**Réponse :**

```java
@RestController
@RequestMapping("/api/users")
public class UserController {{
    @Autowired
    private UserService userService;
    
    @GetMapping
    public List<User> getAllUsers() {{
        return userService.findAll();
    }}
    
    @GetMapping("/{{id}}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {{
        return userService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }}
    
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {{
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.save(user));
    }}
}}
```

### 32. Quelle est la différence entre @RequestBody et @ResponseBody ?

**Réponse :**

@RequestBody : Lie le corps de la requête HTTP à un paramètre de méthode

@ResponseBody : Lie la valeur de retour de la méthode au corps de la réponse HTTP

Avec @RestController, @ResponseBody est implicite.

### 33. Comment valider une requête dans Spring Boot ?

**Réponse :**

```java
@PostMapping("/users")
public ResponseEntity<User> createUser(@Valid @RequestBody User user) {{
    return ResponseEntity.ok(userService.save(user));
}}

public class User {{
    @NotNull
    @Size(min = 2, max = 50)
    private String name;
    @Email
    private String email;
    @Min(18)
    private int age;
}}
```

### 34. Quelle est la différence entre @PathVariable et @RequestParam ?

**Réponse :**

@PathVariable : Extrait les valeurs du chemin URI

@RequestParam : Extrait les paramètres de requête

```java
@GetMapping("/users/{{id}}")
public User getUser(@PathVariable Long id) {{ }}

@GetMapping("/search")
public List<User> search(@RequestParam String name) {{ }}
```

### 35. Comment implémenter la pagination dans Spring Boot ?

**Réponse :**

```java
@GetMapping("/users")
public Page<User> getUsers(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(defaultValue = "id") String sortBy) {{
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    return userRepository.findAll(pageable);
}}
```

### 36. Comment gérer CORS dans Spring Boot ?

**Réponse :**

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {{
    @Override
    public void addCorsMappings(CorsRegistry registry) {{
        registry.addMapping("/api/**")
            .allowedOrigins("http://example.com")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*")
            .allowCredentials(true);
    }}
}}
```

### 37. Quelle est la différence entre POST et PUT ?

**Réponse :**

POST : Crée une nouvelle ressource, non idempotent

PUT : Met à jour ou crée une ressource, idempotent

### 38. Quels sont les codes de statut HTTP courants ?

**Réponse :**

200 OK : Succès

201 Created : Ressource créée

204 No Content : Succès sans contenu

400 Bad Request : Requête invalide

401 Unauthorized : Non authentifié

403 Forbidden : Non autorisé

404 Not Found : Ressource non trouvée

500 Internal Server Error : Erreur serveur

### 39. Comment implémenter HATEOAS ?

**Réponse :**

```java
@GetMapping("/users/{{id}}")
public EntityModel<User> getUser(@PathVariable Long id) {{
    User user = userService.findById(id);
    EntityModel<User> resource = EntityModel.of(user);
    resource.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
    return resource;
}}
```

### 40. Comment créer un filtre personnalisé ?

**Réponse :**

```java
@Component
public class LoggingFilter implements Filter {{
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {{
        long start = System.currentTimeMillis();
        chain.doFilter(request, response);
        long duration = System.currentTimeMillis() - start;
        System.out.println("Request took: " + duration + "ms");
    }}
}}
```

## 5. Spring Data et JPA

### 41. Qu'est-ce que Spring Data JPA ?

**Réponse :**

Spring Data JPA simplifie l'implémentation de la couche d'accès aux données en fournissant des repositories basés sur JPA.

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {{
    Optional<User> findByEmail(String email);
    List<User> findByAgeGreaterThan(int age);
    @Query("SELECT u FROM User u WHERE u.department = :dept")
    List<User> findByDepartment(@Param("dept") String department);
}}
```

### 42. Quelle est la différence entre CrudRepository, JpaRepository et PagingAndSortingRepository ?

**Réponse :**

CrudRepository : Opérations CRUD de base

PagingAndSortingRepository : Étend CrudRepository avec pagination et tri

JpaRepository : Étend PagingAndSortingRepository avec des méthodes JPA spécifiques

### 43. Comment définir une requête personnalisée ?

**Réponse :**

```java
// JPQL
@Query("SELECT u FROM User u WHERE u.email = ?1")
User findByEmail(String email);

// SQL natif
@Query(value = "SELECT * FROM users WHERE email = ?1", nativeQuery = true)
User findByEmailNative(String email);

// Query By Method Name
List<User> findByLastNameAndFirstName(String lastName, String firstName);
```

### 44. Comment gérer les transactions dans Spring Boot ?

**Réponse :**

```java
@Service
public class UserService {{
    @Transactional
    public void transferMoney(Long fromId, Long toId, BigDecimal amount) {{
        // Opérations dans une transaction
    }}
    @Transactional(readOnly = true)
    public User findUser(Long id) {{
        return userRepository.findById(id).orElse(null);
    }}
}}
```

### 45. Qu'est-ce que le problème N+1 et comment le résoudre ?

**Réponse :**

Le problème N+1 se produit quand une requête charge une entité, puis N requêtes supplémentaires chargent les entités liées.

Solutions :

```java
// JOIN FETCH
@Query("SELECT u FROM User u JOIN FETCH u.orders WHERE u.id = :id")
User findUserWithOrders(@Param("id") Long id);

// EntityGraph
@EntityGraph(attributePaths = {{"orders"}})
User findWithOrdersById(Long id);
```

### 46. Qu'est-ce que @Entity et @Table ?

**Réponse :**

```java
@Entity
@Table(name = "users", schema = "public")
public class User {{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_name", nullable = false, length = 100)
    private String name;
    @Transient
    private String temporaryField;
}}
```

### 47. Quels sont les types de relations JPA ?

**Réponse :**

@OneToOne

@OneToMany

@ManyToOne

@ManyToMany

```java
@Entity
public class Order {{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
}}
```

### 48. Qu'est-ce que FetchType.LAZY vs EAGER ?

**Réponse :**

LAZY : Charge l'entité liée uniquement quand elle est accédée

EAGER : Charge l'entité liée immédiatement

```java
@ManyToOne(fetch = FetchType.LAZY)  // Recommandé
private Customer customer;
```

### 49. Comment utiliser @Modifying et @Query ?

**Réponse :**

```java
@Modifying
@Transactional
@Query("UPDATE User u SET u.active = false WHERE u.lastLogin < :date")
int deactivateInactiveUsers(@Param("date") LocalDate date);
```

### 50. Qu'est-ce qu'un Projection dans Spring Data JPA ?

**Réponse :**

Interface pour récupérer un sous-ensemble de propriétés.

```java
public interface UserSummary {{
    String getName();
    String getEmail();
}}

@Query("SELECT u.name as name, u.email as email FROM User u")
List<UserSummary> findAllSummaries();
```
'''

# ============================================================================
# FICHIER 2 : Java.md
# ============================================================================

JAVA = r'''# Java - Questions d'Interview

> **Nombre de questions :** 60
> **Généré le :** {date}

## Table des matières

1. [Core Java](#1-core-java)
2. [Java 8+ Features](#2-java-8-features)
3. [Collections Framework](#3-collections-framework)
4. [Multithreading et Concurrency](#4-multithreading-et-concurrency)
5. [Gestion des Exceptions](#5-gestion-des-exceptions)
6. [String et Immutabilité](#6-string-et-immutabilité)
7. [Java Memory Model](#7-java-memory-model)
8. [Design Patterns](#8-design-patterns)
9. [Java 17+ Features](#9-java-17-features)

---

## 1. Core Java

### 1. Quelles sont les caractéristiques principales de Java ?

**Réponse :**

Object-Oriented : Tout est objet (sauf primitives)

Platform-Independent : Write Once, Run Anywhere (WORA)

Simple : Syntaxe claire et épurée

Secure : Gestion de la mémoire, pas de pointeurs

Architecture-Neutral : Bytecode indépendant de la plateforme

Portable : Même code sur toutes les plateformes

Robust : Gestion des exceptions, garbage collection

Multithreaded : Support natif des threads

High Performance : Compilation JIT

Distributed : Support réseau intégré

### 2. Quelle est la différence entre JDK, JRE et JVM ?

**Réponse :**

JDK (Java Development Kit) : Kit complet de développement incluant JRE + outils (compilateur javac, debugger, etc.)

JRE (Java Runtime Environment) : Environnement d'exécution incluant JVM + bibliothèques

JVM (Java Virtual Machine) : Machine virtuelle qui exécute le bytecode Java

```
JDK > JRE > JVM
```

### 3. Quelle est la différence entre == et equals() ?

**Réponse :**

== : Compare les références (adresses mémoire)

equals() : Compare les valeurs (contenu des objets)

```java
String s1 = "hello";
String s2 = "hello";
String s3 = new String("hello");

s1 == s2        // true (même référence dans le pool)
s1 == s3        // false (références différentes)
s1.equals(s3)   // true (même contenu)
```

### 4. Quelle est la différence entre int et Integer ?

**Réponse :**

int : Type primitif, stocke la valeur directement

Integer : Wrapper class, encapsule un int dans un objet

```java
int primitive = 42;           // Stocké sur la stack
Integer wrapper = 42;         // Objet sur le heap

// Autoboxing (int -> Integer)
Integer boxed = primitive;

// Unboxing (Integer -> int)
int unboxed = wrapper;
```

### 5. Java est-il un langage purement orienté objet ?

**Réponse :**

Non, car Java supporte les types primitifs (int, char, boolean, etc.) qui ne sont pas des objets. Pour être purement orienté objet, tout devrait être un objet.

### 6. Quel est le rôle du mot-clé final ?

**Réponse :**

Variable : Valeur constante, ne peut pas être réassignée

Méthode : Ne peut pas être overridée

Classe : Ne peut pas être étendue (héritée)

```java
public final class Constants {{
    public static final double PI = 3.14159;
    public final void display() {{
        // Ne peut pas être overridée
    }}
}}
```

### 7. Peut-on surcharger ou overrider les méthodes static ?

**Réponse :**

Surcharge (Overloading) : OUI, possible

Override : NON, impossible (les méthodes static sont liées à la classe, pas à l'instance)

```java
class Parent {{
    static void display() {{ System.out.println("Parent"); }}
}}
class Child extends Parent {{
    static void display() {{ System.out.println("Child"); }} // Hiding, pas overriding
}}
```

### 8. Quelle est la différence entre une classe abstraite et une interface ?

**Réponse :**

Classe Abstraite	Interface
Peut avoir des méthodes concrètes	Méthodes abstraites par défaut (Java 8: default/static)
Peut avoir des constructeurs	Pas de constructeurs
Peut avoir des champs d'instance	Champs public static final uniquement
Héritage simple	Héritage multiple possible
### 9. Qu'est-ce que le polymorphisme ?

**Réponse :**

Le polymorphisme permet à un objet d'être traité comme une instance de sa classe parente ou d'une interface.

Types :

Compile-time (Overloading) : Résolu à la compilation

Runtime (Overriding) : Résolu à l'exécution

```java
Animal animal = new Dog();  // Upcasting
animal.sound();             // Appelle Dog.sound() à runtime
```

### 10. Qu'est-ce que l'encapsulation ?

**Réponse :**

L'encapsulation cache les détails internes d'un objet et expose uniquement ce qui est nécessaire via des méthodes publiques.

```java
public class BankAccount {{
    private double balance;  // Champ privé
    public double getBalance() {{
        return balance;
    }}
    public void deposit(double amount) {{
        if (amount > 0) {{
            balance += amount;
        }}
    }}
}}
```

### 11. Quelle est la différence entre surcharge et override ?

**Réponse :**

Surcharge (Overloading) : Même nom, paramètres différents, même classe

Override : Même signature, sous-classe redéfinit la méthode parente

```java
// Overloading
public void print(int x) {{ }}
public void print(String s) {{ }}

// Overriding
@Override
public String toString() {{ return "custom"; }}
```

### 12. Qu'est-ce qu'un constructeur ?

**Réponse :**

Méthode spéciale pour initialiser un objet.

```java
public class User {{
    private String name;
    private int age;
    
    // Constructeur par défaut
    public User() {{ }}
    
    // Constructeur paramétré
    public User(String name, int age) {{
        this.name = name;
        this.age = age;
    }}
}}
```

### 13. Qu'est-ce que le chaînage de constructeurs ?

**Réponse :**

Appeler un constructeur depuis un autre constructeur avec this().

```java
public class User {{
    private String name;
    private int age;
    
    public User() {{
        this("Unknown", 0);
    }}
    public User(String name, int age) {{
        this.name = name;
        this.age = age;
    }}
}}
```

### 14. Quelle est la différence entre this et super ?

**Réponse :**

this : Référence à l'instance courante

super : Référence à la classe parente

```java
public class Child extends Parent {{
    public Child() {{
        super();  // Appelle le constructeur parent
        this.name = "Child";  // Référence à l'instance courante
    }}
}}
```

### 15. Qu'est-ce que la covariance des types de retour ?

**Réponse :**

Une méthode overridée peut retourner un sous-type du type de retour original.

```java
class Parent {{
    Object getValue() {{ return new Object(); }}
}}
class Child extends Parent {{
    @Override
    String getValue() {{ return "Hello"; }}  // String est un sous-type d'Object
}}
```

## 2. Java 8+ Features

### 16. Quelles sont les fonctionnalités introduites dans Java 8 ?

**Réponse :**

Lambda Expressions : Syntaxe concise pour les interfaces fonctionnelles

Stream API : Traitement des collections de façon fonctionnelle

Optional : Conteneur pour éviter les NullPointerException

Default Methods : Méthodes avec implémentation dans les interfaces

Method References : Raccourci pour les lambdas

New Date/Time API : Package java.time

### 17. Qu'est-ce qu'une lambda expression ?

**Réponse :**

Une lambda est une fonction anonyme qui peut être passée comme argument ou stockée dans une variable.

```java
// Sans paramètres
Runnable r = () -> System.out.println("Hello");

// Avec un paramètre
Consumer<String> print = s -> System.out.println(s);

// Avec plusieurs paramètres
BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;

// Avec bloc de code
Function<Integer, Integer> square = n -> {{
    int result = n * n;
    return result;
}};
```

### 18. Qu'est-ce qu'une interface fonctionnelle ?

**Réponse :**

Une interface avec exactement une méthode abstraite. Elle peut avoir plusieurs méthodes default/static.

```java
@FunctionalInterface
public interface Calculator {{
    int calculate(int a, int b);
    default void display() {{
        System.out.println("Calculator");
    }}
}}

Calculator add = (a, b) -> a + b;
```

### 19. Quelles sont les interfaces fonctionnelles intégrées en Java 8 ?

**Réponse :**

Predicate<T> : boolean test(T t) - Retourne un booléen

Function<T, R> : R apply(T t) - Transforme T en R

Consumer<T> : void accept(T t) - Consomme sans retour

Supplier<T> : T get() - Fournit une valeur

UnaryOperator<T> : T apply(T t) - Opère sur même type

BinaryOperator<T> : T apply(T t1, T t2) - Deux mêmes types

```java
Predicate<String> isEmpty = String::isEmpty;
Function<String, Integer> length = String::length;
Consumer<String> print = System.out::println;
```

### 20. Quelle est la différence entre map et flatMap ?

**Réponse :**

map : Transforme chaque élément en un autre élément (1:1)

flatMap : Transforme chaque élément en un stream et aplatit les résultats (1:N)

```java
List<String> words = Arrays.asList("Hello", "World");

// flatMap : Stream<Character>
List<Character> flatMapped = words.stream()
    .flatMap(word -> word.chars().mapToObj(c -> (char) c))
    .collect(Collectors.toList());
// [H, e, l, l, o, W, o, r, l, d]
```

### 21. Qu'est-ce que l'API Stream ?

**Réponse :**

L'API Stream permet de traiter des collections de façon déclarative et fonctionnelle.

Opérations :

Intermédiaires : filter, map, sorted, distinct, limit

Terminales : collect, forEach, reduce, count, anyMatch

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);

List<Integer> evenSquares = numbers.stream()
    .filter(n -> n % 2 == 0)
    .map(n -> n * n)
    .collect(Collectors.toList());
// [4, 16, 36]
```

### 22. Qu'est-ce que la classe Optional ?

**Réponse :**

Optional est un conteneur qui peut ou non contenir une valeur non-null.

```java
Optional<String> empty = Optional.empty();
Optional<String> present = Optional.of("value");
Optional<String> nullable = Optional.ofNullable(getValue());

optional.ifPresent(System.out::println);
String value = optional.orElse("default");
String value2 = optional.orElseThrow(() -> new RuntimeException("Not found"));
```

### 23. Qu'est-ce qu'une méthode de référence ?

**Réponse :**

Syntaxe raccourcie pour les lambdas qui appellent une méthode existante.

```java
// Lambda
list.forEach(s -> System.out.println(s));

// Method reference
list.forEach(System.out::println);

// Constructeur
Supplier<List<String>> supplier = ArrayList::new;
```

### 24. Qu'est-ce qu'une méthode default dans une interface ?

**Réponse :**

Méthode avec implémentation dans une interface, permettant la rétrocompatibilité.

```java
public interface Vehicle {{
    void start();
    default void honk() {{
        System.out.println("Beep beep!");
    }}
}}
```

### 25. Qu'est-ce que l'API Date/Time de Java 8 ?

**Réponse :**

Nouvelle API immutable et thread-safe pour les dates.

```java
LocalDate date = LocalDate.now();
LocalTime time = LocalTime.now();
LocalDateTime dateTime = LocalDateTime.now();

LocalDate tomorrow = date.plusDays(1);
Period period = Period.between(date, tomorrow);

DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
String formatted = date.format(formatter);
```

### 26. Qu'est-ce que l'interface Collector ?

**Réponse :**

Interface pour les opérations de réduction mutables.

```java
List<String> list = stream.collect(Collectors.toList());
Set<String> set = stream.collect(Collectors.toSet());

Map<Integer, List<String>> byLength = stream.collect(
    Collectors.groupingBy(String::length));

String joined = stream.collect(Collectors.joining(", ", "[", "]"));
```

### 27. Qu'est-ce qu'un Stream parallèle ?

**Réponse :**

Stream qui traite les éléments en parallèle utilisant plusieurs threads.

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
int sum = numbers.parallelStream()
    .mapToInt(Integer::intValue)
    .sum();
```

### 28. Qu'est-ce que reduce() dans les Streams ?

**Réponse :**

Opération terminale pour réduire le stream à une seule valeur.

```java
int sum = numbers.stream().reduce(0, Integer::sum);
Optional<Integer> max = numbers.stream().reduce(Integer::max);
```

### 29. Qu'est-ce que l'inférence de type avec var ?

**Réponse :**

Java 10+ permet d'utiliser var pour l'inférence locale.

```java
var name = "John";           // String
var numbers = List.of(1, 2); // List<Integer>
var map = new HashMap<String, Integer>();
```

### 30. Qu'est-ce que les Text Blocks ?

**Réponse :**

Java 15+ : chaînes multi-lignes avec triple quotes.

```java
String json = """  // (triple quotes Java 15+)
    {{
        "name": "John",
        "age": 30
    }}
    """;
```

## 3. Collections Framework

### 31. Quelle est la différence entre List, Set et Map ?

**Réponse :**

Collection	Ordre	Doublons	Accès
List	Oui (index)	Oui	Par index
Set	Non	Non	Par valeur
Map	Non (clés)	Clés non, valeurs oui	Par clé
### 32. Comment fonctionne HashMap en interne ?

**Réponse :**

HashMap utilise un tableau de buckets :

Hashing : hashCode() calcule l'index du bucket

Stockage : Les entrées sont stockées dans des listes chaînées

Collision : Si deux clés ont le même hash, elles sont dans la même liste

Treeification : Si une liste dépasse 8 éléments, elle devient un arbre rouge-noir

Resize : Quand 75% rempli, la capacité double

### 33. Qu'est-ce qu'une collision de hash ?

**Réponse :**

Une collision se produit quand deux clés différentes produisent le même hash code et sont stockées dans le même bucket.

Résolution :

Chaining : Liste chaînée dans le bucket

Open Addressing : Chercher un autre bucket libre

### 34. Quelle est la différence entre HashMap et Hashtable ?

**Réponse :**

HashMap	Hashtable
Non synchronisé	Synchronisé
Autorise 1 clé null	N'autorise pas null
Plus rapide	Plus lent
Préféré en single-thread	Legacy
### 35. Quelle est la différence entre HashMap et ConcurrentHashMap ?

**Réponse :**

HashMap : Non thread-safe

ConcurrentHashMap : Thread-safe avec un meilleur niveau de concurrence

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
map.put("key", 1);
map.computeIfAbsent("key2", k -> 2);
map.merge("key", 1, Integer::sum);
```

### 36. Quelle est la différence entre ArrayList et LinkedList ?

**Réponse :**

ArrayList	LinkedList
Tableau redimensionnable	Liste doublement chaînée
Accès rapide O(1)	Accès lent O(n)
Insertion/suppression lente au milieu	Insertion/suppression rapide
### 37. Qu'est-ce qu'un itérateur fail-fast vs fail-safe ?

**Réponse :**

Fail-fast : Lance ConcurrentModificationException si la collection est modifiée pendant l'itération (ArrayList, HashMap, HashSet)

Fail-safe : Itère sur une copie, pas d'exception (CopyOnWriteArrayList, ConcurrentHashMap)

```java
// Fail-safe
CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>();
for (String s : cowList) {{
    cowList.add("new");  // Pas d'exception
}}
```

### 38. Quelle est la différence entre Comparable et Comparator ?

**Réponse :**

Comparable : Ordre naturel, implémenté par la classe

Comparator : Ordre personnalisé, classe externe

```java
public class Employee implements Comparable<Employee> {{
    @Override
    public int compareTo(Employee other) {{
        return this.name.compareTo(other.name);
    }}
}}

Comparator<Employee> bySalary = Comparator.comparing(Employee::getSalary);
employees.sort(bySalary);
```

### 39. Qu'est-ce que TreeMap ?

**Réponse :**

Map triée par clés (ordre naturel ou Comparator).

```java
TreeMap<String, Integer> map = new TreeMap<>();
map.put("banana", 2);
map.put("apple", 1);
map.put("cherry", 3);
// Ordre : apple, banana, cherry
```

### 40. Qu'est-ce que LinkedHashMap ?

**Réponse :**

HashMap qui maintient l'ordre d'insertion ou d'accès.

```java
LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
map.put("a", 1);
map.put("b", 2);
// Ordre : a, b

// Ordre d'accès (LRU cache)
LinkedHashMap<String, Integer> lru = new LinkedHashMap<>(16, 0.75f, true);
```

### 41. Qu'est-ce que WeakHashMap ?

**Réponse :**

Map avec des clés faiblement référencées, permettant le garbage collection.

```java
WeakHashMap<Object, String> map = new WeakHashMap<>();
Object key = new Object();
map.put(key, "value");
key = null;  // La clé peut être garbage collected
System.gc();
```

### 42. Qu'est-ce que EnumMap ?

**Réponse :**

Map optimisée pour les clés enum.

```java
enum Day {{ MONDAY, TUESDAY, WEDNESDAY }}
EnumMap<Day, String> map = new EnumMap<>(Day.class);
map.put(Day.MONDAY, "Start of week");
```

### 43. Qu'est-ce que Collections.unmodifiableList() ?

**Réponse :**

Crée une vue non modifiable d'une collection.

```java
List<String> mutable = new ArrayList<>();
mutable.add("a");

List<String> immutable = Collections.unmodifiableList(mutable);
immutable.add("b");  // UnsupportedOperationException

// Java 9+
List<String> immutable2 = List.of("a", "b", "c");
```

### 44. Quelle est la différence entre Collections et Collection ?

**Réponse :**

Collection : Interface racine de la hiérarchie

Collections : Classe utilitaire avec méthodes statiques

```java
Collections.sort(list);
Collections.reverse(list);
Collections.shuffle(list);
Collections.max(list);
Collections.min(list);
```

### 45. Comment synchroniser une collection ?

**Réponse :**

```java
// Wrappers synchronisés
List<String> syncList = Collections.synchronizedList(new ArrayList<>());

// Collections concurrentes (meilleures performances)
List<String> cowList = new CopyOnWriteArrayList<>();
Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
```

## 4. Multithreading et Concurrency

### 46. Qu'est-ce qu'un thread ?

**Réponse :**

Un thread est une unité d'exécution légère qui partage la mémoire du processus parent.

```java
// Méthode 1 : Implémenter Runnable
Thread t1 = new Thread(() -> System.out.println("Running"));
t1.start();

// Méthode 2 : Étendre Thread
class MyThread extends Thread {{
    public void run() {{
        System.out.println("Running");
    }}
}}
new MyThread().start();
```

### 47. Quels sont les états d'un thread ?

**Réponse :**

NEW : Créé mais pas démarré

RUNNABLE : En cours d'exécution ou prêt

BLOCKED : En attente d'un lock

WAITING : Attente indéfinie (wait, join)

TIMED_WAITING : Attente avec timeout (sleep, wait)

TERMINATED : Terminé

### 48. Qu'est-ce qu'un deadlock ?

**Réponse :**

Un deadlock se produit quand deux threads ou plus s'attendent mutuellement.

```java
// Thread 1
synchronized(resourceA) {{
    synchronized(resourceB) {{ }}  // Attend resourceB
}}
// Thread 2
synchronized(resourceB) {{
    synchronized(resourceA) {{ }}  // Attend resourceA
}}
Prévention :

Acquérir les locks dans le même ordre

Utiliser des timeouts

Éviter les locks imbriqués

```

### 49. Quelle est la différence entre wait() et sleep() ?

**Réponse :**

wait()	sleep()
Méthode de Object	Méthode de Thread
Libère le lock	Ne libère pas le lock
Nécessite synchronized	Pas nécessaire
Réveillé par notify()	Se termine après timeout
### 50. Qu'est-ce qu'un ExecutorService ?

**Réponse :**

ExecutorService gère un pool de threads pour exécuter des tâches asynchrones.

```java
ExecutorService executor = Executors.newFixedThreadPool(5);

// Soumettre Runnable
executor.submit(() -> System.out.println("Task"));

// Soumettre Callable (retourne Future)
Future<Integer> future = executor.submit(() -> 42);
Integer result = future.get();

executor.shutdown();
```

### 51. Quelle est la différence entre Runnable et Callable ?

**Réponse :**

Runnable	Callable
void run()	V call()
Pas de retour	Retourne un résultat
Pas d'exception checked	Peut lancer des exceptions
Executor.execute()	Executor.submit()
### 52. Qu'est-ce que CompletableFuture ?

**Réponse :**

API pour la programmation asynchrone non-bloquante.

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {{
    return "Hello";
}});
future.thenApply(s -> s + " World")
      .thenAccept(System.out::println);

// Combiner
CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "A");
CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "B");
f1.thenCombine(f2, (a, b) -> a + b)
  .thenAccept(System.out::println);
```

### 53. Qu'est-ce que le mot-clé volatile ?

**Réponse :**

volatile garantit la visibilité des modifications d'une variable entre threads.

```java
public class SharedResource {{
    private volatile boolean flag = false;
    public void setFlag() {{
        flag = true;  // Visible immédiatement par tous les threads
    }}
}}
Note : volatile ne garantit pas l'atomicité.

```

### 54. Qu'est-ce que synchronized ?

**Réponse :**

synchronized assure qu'un seul thread accède à une section critique à la fois.

```java
// Méthode synchronisée
public synchronized void increment() {{
    count++;
}}

// Bloc synchronisé
public void increment() {{
    synchronized(this) {{
        count++;
    }}
}}
```

### 55. Qu'est-ce qu'un AtomicInteger ?

**Réponse :**

Classe thread-safe pour les opérations atomiques sur les entiers.

```java
AtomicInteger counter = new AtomicInteger(0);
counter.incrementAndGet();      // ++counter
counter.getAndIncrement();      // counter++
counter.addAndGet(5);           // counter += 5
counter.compareAndSet(5, 10);   // Si counter == 5, counter = 10
```

### 56. Qu'est-ce qu'un CountDownLatch ?

**Réponse :**

Synchronise les threads en attendant que N opérations se terminent.

```java
CountDownLatch latch = new CountDownLatch(3);
for (int i = 0; i < 3; i++) {{
    new Thread(() -> {{
        latch.countDown();
    }}).start();
}}
latch.await();  // Attend que le compteur atteigne 0
```

### 57. Qu'est-ce qu'un CyclicBarrier ?

**Réponse :**

Synchronise les threads à un point de rendez-vous.

```java
CyclicBarrier barrier = new CyclicBarrier(3, () -> {{
    System.out.println("All threads arrived");
}});
for (int i = 0; i < 3; i++) {{
    new Thread(() -> {{
        barrier.await();
    }}).start();
}}
```

### 58. Qu'est-ce qu'un Semaphore ?

**Réponse :**

Contrôle l'accès à une ressource partagée avec un nombre de permits.

```java
Semaphore semaphore = new Semaphore(3);  // 3 permits
semaphore.acquire();  // Prend un permit
try {{
    // Accéder à la ressource
}} finally {{
    semaphore.release();  // Libère le permit
}}
```

### 59. Qu'est-ce qu'un BlockingQueue ?

**Réponse :**

Queue thread-safe avec opérations bloquantes.

```java
BlockingQueue<String> queue = new LinkedBlockingQueue<>(10);
queue.put("item");           // Bloque si pleine
String item = queue.take();  // Bloque si vide
queue.offer("item");         // Non bloquant
queue.poll();                // Non bloquant
```

### 60. Qu'est-ce que ForkJoinPool ?

**Réponse :**

Pool de threads pour le traitement parallèle avec divide-and-conquer.

```java
ForkJoinPool pool = new ForkJoinPool();
class SumTask extends RecursiveTask<Long> {{
    protected Long compute() {{
        if (array.length <= 1000) {{
            // Calcul direct
        }}
        // Diviser et conquérir
    }}
}}
long result = pool.invoke(new SumTask(array));
```
'''

# ============================================================================
# FICHIER 3 : SQL.md
# ============================================================================

SQL = r'''# SQL - Questions d'Interview

> **Nombre de questions :** 100
> **Généré le :** {date}

## Table des matières

1. [Questions de Base](#1-questions-de-base)
2. [Requêtes et Jointures](#2-requêtes-et-jointures)
3. [Index et Optimisation](#3-index-et-optimisation)
4. [Transactions et Concurrence](#4-transactions-et-concurrence)
5. [Administration](#5-administration)
6. [Questions Pratiques](#6-questions-pratiques)
7. [Bonnes Pratiques](#7-bonnes-pratiques)

---

## 1. Questions de Base

### 1. Qu'est-ce que SQL ?

**Réponse :**

SQL (Structured Query Language) est un langage standardisé pour gérer et manipuler les bases de données relationnelles.

Types de commandes :

DDL : CREATE, ALTER, DROP

DML : SELECT, INSERT, UPDATE, DELETE

DCL : GRANT, REVOKE

TCL : COMMIT, ROLLBACK, SAVEPOINT

### 2. Qu'est-ce qu'une clé primaire (Primary Key) ?

**Réponse :**

Identifiant unique pour chaque enregistrement d'une table.

```sql
CREATE TABLE Employees (
    EmployeeID INT PRIMARY KEY,
    Name VARCHAR(50),
    Department VARCHAR(50)
);
Caractéristiques : Unique, Non null, Une seule par table

```

### 3. Qu'est-ce qu'une clé étrangère (Foreign Key) ?

**Réponse :**

Champ qui référence la clé primaire d'une autre table, assurant l'intégrité référentielle.

```sql
CREATE TABLE Orders (
    OrderID INT PRIMARY KEY,
    OrderDate DATE,
    CustomerID INT,
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)
);
```

### 4. Qu'est-ce qu'une clé unique (Unique Key) ?

**Réponse :**

Assure que toutes les valeurs d'une colonne sont uniques.

```sql
CREATE TABLE Employees (
    EmployeeID INT PRIMARY KEY,
    Email VARCHAR(100) UNIQUE
);
Différence avec Primary Key :

Unique peut contenir NULL (une seule fois)

Plusieurs Unique Keys par table

```

### 5. Qu'est-ce qu'une clé composite ?

**Réponse :**

Clé primaire composée de plusieurs colonnes.

```sql
CREATE TABLE Enrollment (
    StudentID INT,
    CourseID INT,
    EnrollmentDate DATE,
    PRIMARY KEY (StudentID, CourseID)
);
```

### 6. Qu'est-ce qu'une clé candidate ?

**Réponse :**

Colonne ou ensemble de colonnes pouvant identifier uniquement un enregistrement. Une table peut avoir plusieurs clés candidates, dont une devient la clé primaire.

### 7. Qu'est-ce qu'une contrainte ?

**Réponse :**

Règle appliquée aux colonnes pour assurer l'intégrité des données.

Types : PRIMARY KEY, FOREIGN KEY, UNIQUE, CHECK, DEFAULT, NOT NULL

### 8. Qu'est-ce qu'une contrainte CHECK ?

**Réponse :**

Valide une condition sur les valeurs d'une colonne.

```sql
CREATE TABLE Employees (
    EmployeeID INT PRIMARY KEY,
    Name VARCHAR(50),
    Age INT CHECK (Age >= 18),
    Salary DECIMAL(10,2) CHECK (Salary > 0)
);
```

### 9. Qu'est-ce qu'une contrainte DEFAULT ?

**Réponse :**

Valeur par défaut si aucune valeur n'est fournie.

```sql
CREATE TABLE Employees (
    EmployeeID INT PRIMARY KEY,
    Name VARCHAR(50),
    Status VARCHAR(20) DEFAULT 'Active',
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 10. Qu'est-ce qu'un index ?

**Réponse :**

Structure de données qui accélère les recherches.

Types :

Clustered Index : Tri physique des données (1 par table)

Non-Clustered Index : Pointeur vers les données

```sql
CREATE INDEX idx_employee_name ON Employees(Name);
CREATE INDEX idx_dept_name ON Employees(DepartmentID, Name);
```

### 11. Qu'est-ce qu'une vue (View) ?

**Réponse :**

Table virtuelle basée sur le résultat d'une requête.

```sql
CREATE VIEW EmployeeView AS
SELECT e.Name, d.DepartmentName
FROM Employees e
JOIN Departments d ON e.DepartmentID = d.DepartmentID;

SELECT * FROM EmployeeView;
```

### 12. Qu'est-ce qu'une sous-requête (Subquery) ?

**Réponse :**

Requête imbriquée dans une autre requête.

```sql
-- Sous-requête non corrélée
SELECT Name FROM Employees
WHERE DepartmentID = (
    SELECT DepartmentID FROM Departments WHERE DepartmentName = 'Sales'
);

-- Sous-requête corrélée
SELECT E1.Name FROM Employees E1
WHERE E1.Salary > (
    SELECT AVG(Salary) FROM Employees E2 
    WHERE E1.DepartmentID = E2.DepartmentID
);
```

### 13. Qu'est-ce qu'une procédure stockée (Stored Procedure) ?

**Réponse :**

Code SQL précompilé et réutilisable.

```sql
CREATE PROCEDURE GetEmployeeDetails
    @DepartmentID INT
AS
BEGIN
    SELECT * FROM Employees WHERE DepartmentID = @DepartmentID;
END;

EXEC GetEmployeeDetails @DepartmentID = 1;
```

### 14. Qu'est-ce qu'un trigger ?

**Réponse :**

Code SQL qui s'exécute automatiquement sur un événement.

```sql
CREATE TRIGGER trgAfterInsert ON Employees
AFTER INSERT
AS
BEGIN
    INSERT INTO AuditLog (Action, TableName, Timestamp)
    VALUES ('INSERT', 'Employees', GETDATE());
END;
```

### 15. Qu'est-ce que la normalisation ?

**Réponse :**

Organisation des données pour réduire la redondance.

Formes normales :

1NF : Pas de groupes répétitifs, valeurs atomiques

2NF : 1NF + dépendance fonctionnelle complète

3NF : 2NF + pas de dépendances transitives

BCNF : 3NF + toutes les dépendances sont sur des super-clés

### 16. Qu'est-ce que la dénormalisation ?

**Réponse :**

Combiner des tables normalisées pour améliorer les performances de lecture.

Quand l'utiliser : Requêtes de lecture fréquentes, Jointures coûteuses, Rapport et analyses

### 17. Qu'est-ce qu'une transaction ?

**Réponse :**

Séquence d'opérations traitées comme une unité atomique.

Propriétés ACID :

Atomicity : Tout ou rien

Consistency : État cohérent

Isolation : Indépendance des transactions

Durability : Persistance des données

```sql
BEGIN TRANSACTION;
    UPDATE Accounts SET Balance = Balance - 100 WHERE AccountID = 1;
    UPDATE Accounts SET Balance = Balance + 100 WHERE AccountID = 2;
COMMIT;
```

### 18. Quelles sont les propriétés ACID ?

**Réponse :**

Atomicity : La transaction est indivisible

Consistency : La base reste cohérente

Isolation : Les transactions sont isolées

Durability : Les changements sont permanents

### 19. Qu'est-ce qu'un deadlock ?

**Réponse :**

Situation où deux transactions s'attendent mutuellement.

Exemple : Transaction 1 lock A et attend B ; Transaction 2 lock B et attend A

Prévention : Accéder aux ressources dans le même ordre, Timeouts, Détection automatique

### 20. Quelle est la différence entre DELETE et TRUNCATE ?

**Réponse :**

DELETE	TRUNCATE
Supprime ligne par ligne	Supprime tout
Peut avoir WHERE	Pas de WHERE
Loggé	Non loggé (plus rapide)
Rollback possible	Rollback difficile
Trigger exécuté	Trigger non exécuté
### 21. Quelle est la différence entre DROP et TRUNCATE ?

**Réponse :**

DROP : Supprime la table et sa structure

TRUNCATE : Supprime les données, garde la structure

### 22. Quelle est la différence entre WHERE et HAVING ?

**Réponse :**

WHERE : Filtre avant le GROUP BY

HAVING : Filtre après le GROUP BY

```sql
SELECT Department, AVG(Salary) as AvgSalary
FROM Employees
WHERE Age > 25
GROUP BY Department
HAVING AVG(Salary) > 50000;
```

### 23. Qu'est-ce qu'un wildcard en SQL ?

**Réponse :**

Caractères spéciaux pour la recherche de motifs.

% : Zéro ou plusieurs caractères

_ : Un seul caractère

```sql
SELECT * FROM Employees WHERE Name LIKE 'A%';    -- Commence par A
SELECT * FROM Employees WHERE Name LIKE '%son';  -- Finit par son
SELECT * FROM Employees WHERE Name LIKE '_ohn';  -- Un caractère + ohn
```

### 24. Qu'est-ce que l'opérateur IN ?

**Réponse :**

Vérifie si une valeur est dans une liste.

```sql
SELECT * FROM Employees WHERE DepartmentID IN (1, 2, 3);

SELECT * FROM Employees 
WHERE DepartmentID IN (SELECT ID FROM Departments WHERE Active = 1);
```

### 25. Qu'est-ce que l'opérateur BETWEEN ?

**Réponse :**

Sélectionne une plage de valeurs.

```sql
SELECT * FROM Employees WHERE Age BETWEEN 30 AND 40;
SELECT * FROM Orders WHERE OrderDate BETWEEN '2024-01-01' AND '2024-12-31';
```

### 26. Qu'est-ce qu'un CASE statement ?

**Réponse :**

Logique conditionnelle dans SQL.

```sql
SELECT Name,
    CASE
        WHEN Age < 18 THEN 'Minor'
        WHEN Age BETWEEN 18 AND 65 THEN 'Adult'
        ELSE 'Senior'
    END AS AgeGroup
FROM Employees;
```

### 27. Qu'est-ce que la fonction COALESCE ?

**Réponse :**

Retourne la première valeur non-null.

```sql
SELECT COALESCE(Phone, Mobile, 'No Phone') AS Contact
FROM Employees;
```

### 28. Qu'est-ce qu'une valeur NULL ?

**Réponse :**

Représente une valeur manquante ou inconnue.

Points importants :

NULL n'est pas égal à NULL

Comparaison avec NULL retourne NULL

Utiliser IS NULL / IS NOT NULL

```sql
SELECT * FROM Employees WHERE Phone IS NULL;
SELECT * FROM Employees WHERE COALESCE(Phone, '') != '';
```

### 29. Quelle est la différence entre UNION et UNION ALL ?

**Réponse :**

UNION : Supprime les doublons

UNION ALL : Garde les doublons (plus rapide)

```sql
SELECT Name FROM Employees
UNION
SELECT Name FROM Managers;

SELECT Name FROM Employees
UNION ALL
SELECT Name FROM Managers;
```

### 30. Quelle est la différence entre CHAR et VARCHAR ?

**Réponse :**

CHAR : Longueur fixe, complété par des espaces

VARCHAR : Longueur variable

```sql
CHAR(10)     -- 'Hello     ' (10 caractères)
VARCHAR(10)  -- 'Hello' (5 caractères stockés)
```

### 31. Quelle est la différence entre VARCHAR et NVARCHAR ?

**Réponse :**

VARCHAR : Caractères non-Unicode

NVARCHAR : Caractères Unicode (support multi-langues)

### 32. Qu'est-ce qu'un CTE (Common Table Expression) ?

**Réponse :**

Résultat temporaire nommé, utilisable dans une requête.

```sql
WITH EmployeeCTE AS (
    SELECT EmployeeID, Name, DepartmentID
    FROM Employees
    WHERE Salary > 50000
)
SELECT * FROM EmployeeCTE WHERE DepartmentID = 1;
```

### 33. Quelle est la différence entre une table temporaire et une variable de table ?

**Réponse :**

Table temporaire : CREATE TABLE #temp, stockée dans tempdb

Variable de table : DECLARE @table TABLE, en mémoire

### 34. Qu'est-ce qu'un curseur (Cursor) ?

**Réponse :**

Parcourt les résultats ligne par ligne.

```sql
DECLARE @EmployeeID INT
DECLARE EmployeeCursor CURSOR FOR
SELECT EmployeeID FROM Employees

OPEN EmployeeCursor
FETCH NEXT FROM EmployeeCursor INTO @EmployeeID
WHILE @@FETCH_STATUS = 0
BEGIN
    PRINT @EmployeeID
    FETCH NEXT FROM EmployeeCursor INTO @EmployeeID
END
CLOSE EmployeeCursor
DEALLOCATE EmployeeCursor
```

### 35. Quels sont les types de triggers ?

**Réponse :**

DML Triggers : AFTER, INSTEAD OF

DDL Triggers : CREATE, ALTER, DROP

Logon Triggers : Connexion

### 36. Qu'est-ce qu'une fonction stockée ?

**Réponse :**

Fonction SQL qui retourne une valeur.

```sql
CREATE FUNCTION GetEmployeeName (@EmployeeID INT)
RETURNS VARCHAR(50)
AS
BEGIN
    DECLARE @Name VARCHAR(50)
    SELECT @Name = Name FROM Employees WHERE EmployeeID = @EmployeeID
    RETURN @Name
END;

SELECT dbo.GetEmployeeName(1);
```

### 37. Quelle est la différence entre une fonction scalaire et une fonction table ?

**Réponse :**

Scalar Function : Retourne une seule valeur

Table-Valued Function : Retourne une table

```sql
-- Scalar
CREATE FUNCTION GetFullName(@ID INT)
RETURNS VARCHAR(100)
AS
BEGIN
    RETURN (SELECT FirstName + ' ' + LastName FROM Employees WHERE ID = @ID)
END;

-- Table-Valued
CREATE FUNCTION GetEmployeesByDept(@DeptID INT)
RETURNS TABLE
AS
RETURN (SELECT * FROM Employees WHERE DepartmentID = @DeptID);
```

### 38. Qu'est-ce qu'un synonyme ?

**Réponse :**

Alias pour un objet de base de données.

```sql
CREATE SYNONYM Emp FOR HR.dbo.Employees;
SELECT * FROM Emp;
```

### 39. Quelle est la différence entre SQL et MySQL ?

**Réponse :**

SQL : Langage standard de requête

MySQL : Système de gestion de base de données (SGBD) qui utilise SQL

### 40. Quels sont les SGBD courants ?

**Réponse :**

MySQL : Open source, populaire

PostgreSQL : Open source, avancé

Oracle : Commercial, entreprise

SQL Server : Microsoft

MongoDB : NoSQL, orienté documents

SQLite : Embarqué, léger

## 2. Requêtes et Jointures

### 41. Quels sont les différents types de JOIN ?

**Réponse :**

INNER JOIN : Lignes correspondantes dans les deux tables

LEFT JOIN : Toutes les lignes de gauche + correspondances

RIGHT JOIN : Toutes les lignes de droite + correspondances

FULL JOIN : Toutes les lignes des deux tables

CROSS JOIN : Produit cartésien

```sql
-- INNER JOIN
SELECT e.Name, d.DepartmentName
FROM Employees e
INNER JOIN Departments d ON e.DepartmentID = d.DepartmentID;

-- LEFT JOIN
SELECT e.Name, d.DepartmentName
FROM Employees e
LEFT JOIN Departments d ON e.DepartmentID = d.DepartmentID;
```

### 42. Qu'est-ce qu'un self-join ?

**Réponse :**

Jointure d'une table avec elle-même.

```sql
SELECT A.EmployeeID, A.Name AS Employee, B.Name AS Manager
FROM Employees A
INNER JOIN Employees B ON A.ManagerID = B.EmployeeID;
```

### 43. Qu'est-ce qu'un CROSS JOIN ?

**Réponse :**

Produit cartésien de deux tables (toutes les combinaisons).

```sql
SELECT A.Name, B.DepartmentName
FROM Employees A
CROSS JOIN Departments B;
-- Si 10 employés et 5 départements = 50 lignes
```

### 44. Qu'est-ce qu'un LEFT JOIN ?

**Réponse :**

Retourne toutes les lignes de la table de gauche et les correspondances de droite.

```sql
SELECT e.Name, d.DepartmentName
FROM Employees e
LEFT JOIN Departments d ON e.DepartmentID = d.DepartmentID;
-- Les employés sans département auront NULL pour DepartmentName
```

### 45. Qu'est-ce qu'un RIGHT JOIN ?

**Réponse :**

Retourne toutes les lignes de la table de droite et les correspondances de gauche.

```sql
SELECT e.Name, d.DepartmentName
FROM Employees e
RIGHT JOIN Departments d ON e.DepartmentID = d.DepartmentID;
-- Les départements sans employés auront NULL pour Name
```

### 46. Qu'est-ce qu'un FULL OUTER JOIN ?

**Réponse :**

Retourne toutes les lignes des deux tables.

```sql
SELECT e.Name, d.DepartmentName
FROM Employees e
FULL OUTER JOIN Departments d ON e.DepartmentID = d.DepartmentID;
```

### 47. Comment utiliser EXISTS ?

**Réponse :**

Vérifie si une sous-requête retourne des lignes.

```sql
SELECT * FROM Employees e
WHERE EXISTS (
    SELECT 1 FROM Departments d 
    WHERE d.DepartmentID = e.DepartmentID AND d.Active = 1
);
```

### 48. Comment utiliser NOT EXISTS ?

**Réponse :**

Vérifie qu'une sous-requête ne retourne aucune ligne.

```sql
SELECT * FROM Employees e
WHERE NOT EXISTS (
    SELECT 1 FROM Orders o WHERE o.EmployeeID = e.EmployeeID
);
-- Employés sans commandes
```

### 49. Quelle est la différence entre IN et EXISTS ?

**Réponse :**

IN : Compare avec une liste de valeurs

EXISTS : Vérifie l'existence de lignes

Performance :

IN : Mieux pour les petites listes

EXISTS : Mieux pour les grandes tables (s'arrête à la première correspondance)

### 50. Comment faire une jointure sur plusieurs tables ?

**Réponse :**

```sql
SELECT c.Name, o.OrderDate, p.ProductName, od.Quantity
FROM Customers c
INNER JOIN Orders o ON c.CustomerID = o.CustomerID
INNER JOIN OrderDetails od ON o.OrderID = od.OrderID
INNER JOIN Products p ON od.ProductID = p.ProductID
WHERE o.OrderDate >= '2024-01-01';
```

### 51. Comment utiliser GROUP BY ?

**Réponse :**

Groupe les lignes pour les fonctions d'agrégation.

```sql
SELECT DepartmentID, COUNT(*) as EmployeeCount, AVG(Salary) as AvgSalary
FROM Employees
GROUP BY DepartmentID;

SELECT DepartmentID, COUNT(*) as EmployeeCount
FROM Employees
GROUP BY DepartmentID
HAVING COUNT(*) > 5;
```

### 52. Quelles sont les fonctions d'agrégation ?

**Réponse :**

COUNT() : Nombre de lignes

SUM() : Somme

AVG() : Moyenne

MIN() : Minimum

MAX() : Maximum

```sql
SELECT 
    COUNT(*) as Total,
    SUM(Salary) as TotalSalary,
    AVG(Salary) as AvgSalary,
    MIN(Salary) as MinSalary,
    MAX(Salary) as MaxSalary
FROM Employees;
```

### 53. Comment utiliser DISTINCT ?

**Réponse :**

Élimine les doublons.

```sql
SELECT DISTINCT DepartmentID FROM Employees;
SELECT DISTINCT DepartmentID, JobTitle FROM Employees;
```

### 54. Comment utiliser ORDER BY ?

**Réponse :**

Trie les résultats.

```sql
SELECT * FROM Employees ORDER BY Salary DESC;

SELECT * FROM Employees ORDER BY DepartmentID ASC, Salary DESC;

SELECT * FROM Employees ORDER BY COALESCE(Salary, 0) DESC;
```

### 55. Comment utiliser LIMIT / TOP ?

**Réponse :**

Limite le nombre de résultats.

```sql
-- MySQL/PostgreSQL
SELECT * FROM Employees LIMIT 10;

-- SQL Server
SELECT TOP 10 * FROM Employees;

-- Oracle
SELECT * FROM Employees WHERE ROWNUM <= 10;

-- Avec OFFSET
SELECT * FROM Employees LIMIT 10 OFFSET 20;
```

### 56. Comment utiliser les window functions ?

**Réponse :**

Calculs sur un ensemble de lignes liées à la ligne courante.

```sql
SELECT Name, Salary,
    ROW_NUMBER() OVER (ORDER BY Salary DESC) as RowNum,
    RANK() OVER (ORDER BY Salary DESC) as Rank,
    DENSE_RANK() OVER (ORDER BY Salary DESC) as DenseRank,
    SUM(Salary) OVER (PARTITION BY DepartmentID) as DeptTotal
FROM Employees;
```

### 57. Expliquez ROW_NUMBER, RANK, DENSE_RANK, NTILE

**Réponse :**

ROW_NUMBER : Numéro unique séquentiel

RANK : Rang avec gaps pour ex-aequo

DENSE_RANK : Rang sans gaps

NTILE : Divise en N groupes

```
Données : 100, 100, 90, 80
ROW_NUMBER(): 1, 2, 3, 4
RANK():       1, 1, 3, 4
DENSE_RANK(): 1, 1, 2, 3
NTILE(2):     1, 1, 2, 2
```

### 58. Qu'est-ce qu'une requête récursive ?

**Réponse :**

Requête qui se réfère à elle-même (CTE récursif).

```sql
WITH EmployeeHierarchy AS (
    SELECT EmployeeID, ManagerID, Name, 1 as Level
    FROM Employees WHERE ManagerID IS NULL
    UNION ALL
    SELECT e.EmployeeID, e.ManagerID, e.Name, eh.Level + 1
    FROM Employees e
    INNER JOIN EmployeeHierarchy eh ON e.ManagerID = eh.EmployeeID
)
SELECT * FROM EmployeeHierarchy;
```

### 59. Quelle est la différence entre CROSS APPLY et OUTER APPLY ?

**Réponse :**

CROSS APPLY : Comme INNER JOIN, retourne les correspondances

OUTER APPLY : Comme LEFT JOIN, retourne tout de gauche

```sql
-- CROSS APPLY
SELECT e.Name, o.OrderDate
FROM Employees e
CROSS APPLY (
    SELECT TOP 3 * FROM Orders WHERE EmployeeID = e.EmployeeID
) o;

-- OUTER APPLY
SELECT e.Name, o.OrderDate
FROM Employees e
OUTER APPLY (
    SELECT TOP 3 * FROM Orders WHERE EmployeeID = e.EmployeeID
) o;
```

### 60. Comment implémenter la pagination ?

**Réponse :**

```sql
-- SQL Server
SELECT * FROM Employees
ORDER BY EmployeeID
OFFSET 10 ROWS FETCH NEXT 10 ROWS ONLY;

-- MySQL
SELECT * FROM Employees ORDER BY EmployeeID LIMIT 10 OFFSET 10;

-- Oracle
SELECT * FROM (
    SELECT e.*, ROWNUM rn FROM Employees e
) WHERE rn BETWEEN 11 AND 20;
```

### 61. Qu'est-ce que le SQL dynamique ?

**Réponse :**

SQL généré et exécuté à runtime.

```sql
DECLARE @sql NVARCHAR(MAX);
DECLARE @tableName NVARCHAR(50) = 'Employees';
SET @sql = 'SELECT * FROM ' + @tableName;
EXEC sp_executesql @sql;
Risques : Injection SQL, Complexité de debug, Performance overhead

```

### 62. Qu'est-ce que FOR XML PATH ?

**Réponse :**

Convertit les résultats en XML.

```sql
SELECT EmployeeID, Name
FROM Employees
FOR XML PATH('Employee');
```

### 63. Comment utiliser JSON dans SQL Server ?

**Réponse :**

```sql
-- Générer du JSON
SELECT Name, Age FROM Employees FOR JSON AUTO;

-- Parser du JSON
SELECT * FROM OPENJSON(@json)
WITH (
    Name NVARCHAR(50) '$.name',
    Age INT '$.age'
);

-- Modifier du JSON
SELECT JSON_MODIFY(@json, '$.name', 'NewName');
```

### 64. Comment faire un PIVOT ?

**Réponse :**

Transforme les lignes en colonnes.

```sql
SELECT *
FROM (
    SELECT Department, YEAR(HireDate) as Year, Salary
    FROM Employees
) AS SourceTable
PIVOT (
    SUM(Salary)
    FOR Year IN ([2020], [2021], [2022], [2023])
) AS PivotTable;
```

### 65. Comment faire un UNPIVOT ?

**Réponse :**

Transforme les colonnes en lignes.

```sql
SELECT Department, Year, Salary
FROM (
    SELECT Department, [2020], [2021], [2022] FROM EmployeePivot
) AS p
UNPIVOT (
    Salary FOR Year IN ([2020], [2021], [2022])
) AS unpvt;
```

## 3. Index et Optimisation

### 66. Qu'est-ce qu'un index clusterisé ?

**Réponse :**

Index qui détermine l'ordre physique des données.

```sql
CREATE CLUSTERED INDEX idx_employee_id ON Employees(EmployeeID);
Caractéristiques : Un seul par table, Les données sont stockées dans cet ordre

```

### 67. Qu'est-ce qu'un index non-clusterisé ?

**Réponse :**

Index séparé des données avec des pointeurs.

```sql
CREATE INDEX idx_employee_name ON Employees(Name);
Caractéristiques : Plusieurs par table, Structure séparée

```

### 68. Comment optimiser une requête ?

**Réponse :**

Index : Créer des index appropriés

SELECT : Éviter SELECT *

JOIN : Préférer les JOIN aux sous-requêtes

WHERE : Éviter les fonctions sur les colonnes indexées

LIMIT : Utiliser pour limiter les résultats

EXPLAIN : Analyser le plan d'exécution

### 69. Qu'est-ce qu'un plan d'exécution ?

**Réponse :**

Décomposition de la façon dont SQL Server exécute une requête.

```sql
EXPLAIN SELECT * FROM Employees WHERE Name = 'John';
-- Ou
SET SHOWPLAN_ALL ON;
```

### 70. Comment éviter les fonctions sur les colonnes indexées ?

**Réponse :**

```sql
-- Mauvais (empêche l'utilisation de l'index)
SELECT * FROM Employees WHERE YEAR(HireDate) = 2024;

-- Bon (utilise l'index)
SELECT * FROM Employees 
WHERE HireDate >= '2024-01-01' AND HireDate < '2025-01-01';
```

### 71. Qu'est-ce qu'un index couvrant (Covering Index) ?

**Réponse :**

Index qui contient toutes les colonnes nécessaires à une requête.

```sql
CREATE INDEX idx_covering ON Employees(DepartmentID) 
INCLUDE (Name, Salary);

SELECT Name, Salary FROM Employees WHERE DepartmentID = 1;
```

### 72. Qu'est-ce qu'un index filtré ?

**Réponse :**

Index avec une clause WHERE.

```sql
CREATE INDEX idx_active_employees ON Employees(Name)
WHERE Status = 'Active';
```

### 73. Qu'est-ce qu'un index full-text ?

**Réponse :**

Index pour la recherche textuelle.

```sql
CREATE FULLTEXT CATALOG ftCatalog AS DEFAULT;
CREATE FULLTEXT INDEX ON Articles(Content)
KEY INDEX PK_Articles;

SELECT * FROM Articles
WHERE CONTAINS(Content, 'database AND performance');
```

### 74. Qu'est-ce qu'un index composite ?

**Réponse :**

Index sur plusieurs colonnes.

```sql
CREATE INDEX idx_dept_name ON Employees(DepartmentID, Name);
Ordre important :

(DepartmentID, Name) : Utile pour WHERE DepartmentID = ?

(Name, DepartmentID) : Plus utile pour WHERE Name = ?

```

### 75. Comment détecter les index manquants ?

**Réponse :**

```sql
SELECT 
    migs.avg_total_user_cost * migs.avg_user_impact * (migs.user_seeks + migs.user_scans) AS improvement_measure,
    mid.statement AS table_name,
    mid.equality_columns,
    mid.inequality_columns,
    mid.included_columns
FROM sys.dm_db_missing_index_group_stats migs
INNER JOIN sys.dm_db_missing_index_groups mig ON migs.group_handle = mig.index_group_handle
INNER JOIN sys.dm_db_missing_index_details mid ON mig.index_handle = mid.index_handle
ORDER BY improvement_measure DESC;
```

### 76. Comment détecter les index inutilisés ?

**Réponse :**

```sql
SELECT 
    OBJECT_NAME(i.object_id) AS TableName,
    i.name AS IndexName,
    ISNULL(s.user_seeks, 0) AS user_seeks,
    ISNULL(s.user_scans, 0) AS user_scans,
    ISNULL(s.user_updates, 0) AS user_updates
FROM sys.indexes i
LEFT JOIN sys.dm_db_index_usage_stats s 
    ON i.object_id = s.object_id AND i.index_id = s.index_id
WHERE OBJECTPROPERTY(i.object_id, 'IsUserTable') = 1
ORDER BY user_seeks + user_scans ASC;
```

### 77. Qu'est-ce qu'une statistique ?

**Réponse :**

Informations sur la distribution des données utilisées par l'optimiseur.

```sql
UPDATE STATISTICS Employees;
CREATE STATISTICS stat_name ON Employees(Name);
DBCC SHOW_STATISTICS('Employees', 'stat_name');
```

### 78. Comment analyser les requêtes lentes ?

**Réponse :**

```sql
SELECT TOP 10 
    qs.total_elapsed_time / qs.execution_count AS avg_elapsed_time,
    qs.execution_count,
    SUBSTRING(st.text, (qs.statement_start_offset/2)+1,
        ((CASE qs.statement_end_offset
            WHEN -1 THEN DATALENGTH(st.text)
            ELSE qs.statement_end_offset
        END - qs.statement_start_offset)/2) + 1) AS query_text
FROM sys.dm_exec_query_stats qs
CROSS APPLY sys.dm_exec_sql_text(qs.sql_handle) st
ORDER BY avg_elapsed_time DESC;
```

### 79. Qu'est-ce qu'un deadlock et comment le résoudre ?

**Réponse :**

Deux transactions s'attendent mutuellement.

Détection :

```sql
DBCC TRACEON(1222, -1);
Résolution : Accéder aux ressources dans le même ordre, Utiliser des timeouts, Réduire la durée des transactions

```

### 80. Qu'est-ce que le lock escalation ?

**Réponse :**

SQL Server convertit les locks de ligne en locks de table quand trop de locks sont acquis.

```sql
ALTER TABLE Employees SET (LOCK_ESCALATION = DISABLE);
```

### 81. Quels sont les niveaux d'isolation ?

**Réponse :**

READ UNCOMMITTED : Dirty reads possibles

READ COMMITTED : Pas de dirty reads (défaut)

REPEATABLE READ : Pas de non-repeatable reads

SERIALIZABLE : Isolation complète

SNAPSHOT : Versioning des données

```sql
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
```

### 82. Qu'est-ce que l'injection SQL ?

**Réponse :**

Insertion de code SQL malveillant dans une requête.

Exemple vulnérable :

```sql
SELECT * FROM Users WHERE Name = '" + userInput + "';
-- Si userInput = "' OR '1'='1", retourne tous les utilisateurs
Prévention :

java
PreparedStatement ps = conn.prepareStatement(
    "SELECT * FROM Users WHERE Name = ?");
ps.setString(1, userInput);
```

### 83. Comment prévenir l'injection SQL ?

**Réponse :**

Requêtes paramétrées : PreparedStatement

Procédures stockées : Paramètres typés

Validation : Vérifier les entrées

ORM : Hibernate, JPA

Principe du moindre privilège : Limiter les permissions

### 84. Qu'est-ce qu'un plan d'exécution en cache ?

**Réponse :**

SQL Server cache les plans d'exécution pour réutilisation.

```sql
SELECT * FROM sys.dm_exec_cached_plans;
DBCC FREEPROCCACHE;
```

### 85. Qu'est-ce que la paramétrisation ?

**Réponse :**

Réutilisation des plans d'exécution avec des paramètres.

```sql
EXEC sp_executesql N'SELECT * FROM Employees WHERE ID = @ID', 
    N'@ID INT', @ID = 1;
```

## 4. Transactions et Concurrence

### 86. Qu'est-ce qu'une transaction imbriquée ?

**Réponse :**

Transaction dans une autre transaction.

```sql
BEGIN TRANSACTION;
    BEGIN TRANSACTION;  -- Transaction imbriquée
    COMMIT;
COMMIT;
```

### 87. Qu'est-ce qu'un SAVEPOINT ?

**Réponse :**

Point de sauvegarde dans une transaction.

```sql
BEGIN TRANSACTION;
    INSERT INTO Employees VALUES (1, 'John');
    SAVEPOINT sp1;
    INSERT INTO Employees VALUES (2, 'Jane');
    ROLLBACK TO sp1;  -- Annule seulement l'insertion de Jane
COMMIT;
```

### 88. Qu'est-ce que le contrôle de concurrence optimiste ?

**Réponse :**

Vérifie les conflits avant le commit.

```sql
UPDATE Employees 
SET Salary = 60000, Version = Version + 1
WHERE EmployeeID = 1 AND Version = 5;
-- Si 0 lignes affectées, conflit détecté
```

### 89. Qu'est-ce que le contrôle de concurrence pessimiste ?

**Réponse :**

Verrouille les ressources avant modification.

```sql
BEGIN TRANSACTION;
    SELECT * FROM Employees WITH (UPDLOCK) WHERE EmployeeID = 1;
    UPDATE Employees SET Salary = 60000 WHERE EmployeeID = 1;
COMMIT;
```

### 90. Qu'est-ce qu'un lock ?

**Réponse :**

Mécanisme de synchronisation pour l'accès concurrent.

Types :

Shared (S) : Lecture

Exclusive (X) : Écriture

Update (U) : Mise à jour

```sql
SELECT * FROM sys.dm_tran_locks;
```

### 91. Qu'est-ce qu'un blocage ?

**Réponse :**

Une transaction attend qu'une autre libère un lock.

```sql
SELECT 
    blocking.session_id AS blocking_session,
    blocked.session_id AS blocked_session,
    blocked.wait_type,
    blocked.wait_time
FROM sys.dm_exec_requests blocked
JOIN sys.dm_exec_requests blocking 
    ON blocked.blocking_session_id = blocking.session_id
WHERE blocked.blocking_session_id > 0;
```

### 92. Comment tuer une session bloquante ?

**Réponse :**

```sql
-- Voir les sessions
SELECT * FROM sys.dm_exec_sessions;

-- Tuer une session
KILL 52;  -- 52 = session_id

-- Tuer avec rollback
KILL 52 WITH ROLLBACK IMMEDIATE;
```

### 93. Qu'est-ce que le MVCC ?

**Réponse :**

Multi-Version Concurrency Control : versioning des données pour éviter les locks.

Implémentations :

SQL Server : SNAPSHOT isolation

PostgreSQL : MVCC natif

Oracle : Read consistency

### 94. Qu'est-ce que le snapshot isolation ?

**Réponse :**

Lecture des données versionnées sans locks.

```sql
ALTER DATABASE MyDB SET ALLOW_SNAPSHOT_ISOLATION ON;

SET TRANSACTION ISOLATION LEVEL SNAPSHOT;
BEGIN TRANSACTION;
    SELECT * FROM Employees;  -- Version au début de la transaction
COMMIT;
```

### 95. Qu'est-ce que la cohérence de lecture ?

**Réponse :**

Garantir que les lectures voient un état cohérent.

Types :

Read Committed : Voit les commits

Repeatable Read : Même résultat dans la transaction

Serializable : Isolation complète

## 5. Administration

### 96. Comment migrer une base de données ?

**Réponse :**

Backup/Restore : Sauvegarde et restauration

Detach/Attach : Détacher et attacher

SSIS : SQL Server Integration Services

Replication : Réplication

Scripts : Génération de scripts

### 97. Qu'est-ce que la réplication ?

**Réponse :**

Copie et distribution de données entre bases.

Types :

Snapshot : Copie complète périodique

Transactional : Changements en temps réel

Merge : Synchronisation bidirectionnelle

### 98. Qu'est-ce que le log shipping ?

**Réponse :**

Backup automatique des logs de transaction et restauration sur un serveur secondaire.

Composants : Serveur primaire, Serveur secondaire, Serveur de monitoring

### 99. Qu'est-ce que le database mirroring ?

**Réponse :**

Duplication de la base sur un serveur standby pour haute disponibilité.

Modes :

High Safety : Synchrone

High Performance : Asynchrone

### 100. Qu'est-ce que Always On Availability Groups ?

**Réponse :**

Solution de haute disponibilité et reprise après sinistre.

Avantages : Basculement automatique, Réplicas lisibles, Pas de stockage partagé

## 6. Questions Pratiques

### 101. Trouver le Nième salaire maximum

**Réponse :**

```sql
-- Avec sous-requête
SELECT DISTINCT Salary
FROM Employees
ORDER BY Salary DESC
LIMIT 1 OFFSET N-1;

-- Avec DENSE_RANK
SELECT Salary FROM (
    SELECT Salary, DENSE_RANK() OVER (ORDER BY Salary DESC) as rnk
    FROM Employees
) t WHERE rnk = N;

-- Pour le 2ème salaire max
SELECT MAX(Salary) FROM Employees
WHERE Salary < (SELECT MAX(Salary) FROM Employees);
```

### 102. Trouver les doublons

**Réponse :**

```sql
SELECT Email, COUNT(*) as Count
FROM Employees
GROUP BY Email
HAVING COUNT(*) > 1;

-- Supprimer les doublons
DELETE FROM Employees
WHERE EmployeeID NOT IN (
    SELECT MIN(EmployeeID) FROM Employees GROUP BY Email
);
```

### 103. Trouver les enregistrements sans correspondance

**Réponse :**

```sql
-- LEFT JOIN avec IS NULL
SELECT e.Name FROM Employees e
LEFT JOIN Departments d ON e.DepartmentID = d.DepartmentID
WHERE d.DepartmentID IS NULL;

-- NOT EXISTS
SELECT Name FROM Employees e
WHERE NOT EXISTS (
    SELECT 1 FROM Departments d WHERE d.DepartmentID = e.DepartmentID
);
```

### 104. Calculer un total cumulatif

**Réponse :**

```sql
SELECT OrderDate, Amount,
    SUM(Amount) OVER (ORDER BY OrderDate) as RunningTotal
FROM Orders;
```

### 105. Trouver les employés avec le même salaire que leur manager

**Réponse :**

```sql
SELECT e.Name as Employee, m.Name as Manager
FROM Employees e
JOIN Employees m ON e.ManagerID = m.EmployeeID
WHERE e.Salary = m.Salary;
```

### 106. Trouver le département avec le plus d'employés

**Réponse :**

```sql
SELECT DepartmentID, COUNT(*) as EmployeeCount
FROM Employees
GROUP BY DepartmentID
ORDER BY EmployeeCount DESC
LIMIT 1;
```

### 107. Trouver les employés embauchés cette année

**Réponse :**

```sql
SELECT * FROM Employees
WHERE YEAR(HireDate) = YEAR(CURDATE());

-- Ou plus performant
SELECT * FROM Employees
WHERE HireDate >= '2024-01-01' AND HireDate < '2025-01-01';
```

### 108. Mettre à jour avec JOIN

**Réponse :**

```sql
UPDATE e
SET e.DepartmentName = d.DepartmentName
FROM Employees e
JOIN Departments d ON e.DepartmentID = d.DepartmentID;
```

### 109. Insérer des données depuis une autre table

**Réponse :**

```sql
INSERT INTO EmployeesBackup (EmployeeID, Name, Salary)
SELECT EmployeeID, Name, Salary
FROM Employees
WHERE HireDate < '2020-01-01';
```

### 110. Créer une table à partir d'une requête

**Réponse :**

```sql
SELECT * INTO EmployeesBackup
FROM Employees
WHERE DepartmentID = 1;
```

### 111. Trouver les ventes par mois

**Réponse :**

```sql
SELECT 
    YEAR(OrderDate) as Year,
    MONTH(OrderDate) as Month,
    SUM(Amount) as TotalSales
FROM Orders
GROUP BY YEAR(OrderDate), MONTH(OrderDate)
ORDER BY Year, Month;
```

### 112. Calculer le pourcentage

**Réponse :**

```sql
SELECT 
    DepartmentID,
    COUNT(*) as EmpCount,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM Employees), 2) as Percentage
FROM Employees
GROUP BY DepartmentID;
```

### 113. Trouver les N premiers enregistrements par groupe

**Réponse :**

```sql
SELECT * FROM (
    SELECT *,
        ROW_NUMBER() OVER (PARTITION BY DepartmentID ORDER BY Salary DESC) as rn
    FROM Employees
) t WHERE rn <= 3;
```

### 114. Concaténer des valeurs

**Réponse :**

```sql
-- SQL Server
SELECT STRING_AGG(Name, ', ') FROM Employees;

-- MySQL
SELECT GROUP_CONCAT(Name SEPARATOR ', ') FROM Employees;
```

### 115. Trouver les jours entre deux dates

**Réponse :**

```sql
-- SQL Server
SELECT DATEDIFF(DAY, '2024-01-01', '2024-12-31');  -- 365

-- MySQL
SELECT DATEDIFF('2024-12-31', '2024-01-01');  -- 365
```

### 116. Formater une date

**Réponse :**

```sql
-- SQL Server
SELECT FORMAT(GETDATE(), 'yyyy-MM-dd HH:mm:ss');

-- MySQL
SELECT DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s');
```

### 117. Gérer les fuseaux horaires

**Réponse :**

```sql
-- SQL Server
SELECT GETUTCDATE();  -- UTC
SELECT SYSDATETIMEOFFSET();  -- Avec offset

-- MySQL
SELECT UTC_TIMESTAMP();  -- UTC
SELECT CONVERT_TZ(NOW(), '+00:00', '+05:30');  -- Conversion
```

### 118. Trouver les tables contenant une colonne spécifique

**Réponse :**

```sql
SELECT TABLE_NAME
FROM INFORMATION_SCHEMA.COLUMNS
WHERE COLUMN_NAME = 'Email';
```

### 119. Compter les lignes de toutes les tables

**Réponse :**

```sql
SELECT 
    t.name AS TableName,
    p.rows AS RowCount
FROM sys.tables t
JOIN sys.partitions p ON t.object_id = p.object_id
WHERE p.index_id IN (0, 1)
ORDER BY p.rows DESC;
```

### 120. Trouver les procédures stockées contenant un texte

**Réponse :**

```sql
SELECT OBJECT_NAME(object_id), definition
FROM sys.sql_modules
WHERE definition LIKE '%Employee%';
```

## 7. Bonnes Pratiques

### 121. Utiliser des index appropriés

**Réponse :**

Index sur les colonnes de WHERE, JOIN, ORDER BY

Éviter trop d'index (ralentit les écritures)

Index composites pour requêtes multi-colonnes

Surveiller l'utilisation des index

### 122. Éviter SELECT *

**Réponse :**

```sql
-- Mauvais
SELECT * FROM Employees;

-- Bon
SELECT EmployeeID, Name, Email FROM Employees;
```

### 123. Utiliser des paramètres au lieu de concaténation

**Réponse :**

```sql
-- Vulnérable
@sql = 'SELECT * FROM Users WHERE Name = ''\' + @name + ''\'';

-- Sûr
EXEC sp_executesql 
    N'SELECT * FROM Users WHERE Name = @Name',
    N'@Name NVARCHAR(50)',
    @Name = @name;
```

### 124. Limiter les résultats

**Réponse :**

```sql
SELECT TOP 100 * FROM LargeTable;
-- Ou
SELECT * FROM LargeTable LIMIT 100;
```

### 125. Utiliser EXISTS au lieu de IN

**Réponse :**

```sql
-- Moins performant
SELECT * FROM Employees
WHERE DepartmentID IN (SELECT DepartmentID FROM Departments WHERE Active = 1);

-- Plus performant
SELECT * FROM Employees e
WHERE EXISTS (
    SELECT 1 FROM Departments d 
    WHERE d.DepartmentID = e.DepartmentID AND d.Active = 1
);
```

### 126. Utiliser des transactions appropriées

**Réponse :**

```sql
BEGIN TRY
    BEGIN TRANSACTION;
        -- Opérations
    COMMIT;
END TRY
BEGIN CATCH
    ROLLBACK;
    THROW;
END CATCH;
```

### 127. Documenter le schéma

**Réponse :**

```sql
EXEC sp_addextendedproperty 
    @name = N'MS_Description',
    @value = N'Table des employés',
    @level0type = N'SCHEMA', @level0name = 'dbo',
    @level1type = N'TABLE', @level1name = 'Employees';

EXEC sp_addextendedproperty 
    @name = N'MS_Description',
    @value = N'Identifiant unique',
    @level0type = N'SCHEMA', @level0name = 'dbo',
    @level1type = N'TABLE', @level1name = 'Employees',
    @level2type = N'COLUMN', @level2name = 'EmployeeID';
```

### 128. Sauvegarder régulièrement

**Réponse :**

```sql
-- Backup complet
BACKUP DATABASE MyDB TO DISK = 'C:\\Backup\\MyDB.bak';

-- Backup différentiel
BACKUP DATABASE MyDB TO DISK = 'C:\\Backup\\MyDB_diff.bak' WITH DIFFERENTIAL;

-- Backup du log
BACKUP LOG MyDB TO DISK = 'C:\\Backup\\MyDB_log.trn';
```

### 129. Surveiller les performances

**Réponse :**

```sql
SELECT TOP 10 
    qs.total_elapsed_time / qs.execution_count AS avg_elapsed_time,
    qs.execution_count,
    SUBSTRING(st.text, (qs.statement_start_offset/2)+1,
        ((CASE qs.statement_end_offset
            WHEN -1 THEN DATALENGTH(st.text)
            ELSE qs.statement_end_offset
        END - qs.statement_start_offset)/2) + 1) AS query_text
FROM sys.dm_exec_query_stats qs
CROSS APPLY sys.dm_exec_sql_text(qs.sql_handle) st
ORDER BY avg_elapsed_time DESC;
```

### 130. Utiliser des vues pour simplifier

**Réponse :**

```sql
CREATE VIEW ActiveEmployees AS
SELECT EmployeeID, Name, Department
FROM Employees
WHERE Status = 'Active';

SELECT * FROM ActiveEmployees WHERE Department = 'IT';
```
'''

# ============================================================================
# GENERATION
# ============================================================================

def compter_questions(contenu):
    """Compte les questions au format ### N."""
    return len(re.findall(r'^### \d+\.', contenu, re.MULTILINE))


def ecrire_fichier(nom, contenu):
    """Ecrit le fichier Markdown avec encodage UTF-8."""
    with open(nom, 'w', encoding='utf-8') as f:
        f.write(contenu)
    taille_kb = len(contenu.encode('utf-8')) / 1024
    nb_q = compter_questions(contenu)
    print(f"  OK {nom:20s} | {nb_q:3d} questions | {taille_kb:6.1f} KB")


def main():
    print("=" * 65)
    print(" GENERATEUR D INTERVIEW JAVA FULL STACK")
    print("=" * 65)
    print()

    date_str = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    dossier = os.path.dirname(os.path.abspath(__file__))

    fichiers = [
        ("SpringBoot.md", SPRINGBOOT.format(date=date_str)),
        ("Java.md", JAVA.format(date=date_str)),
        ("SQL.md", SQL.format(date=date_str)),
    ]

    print("Generation des fichiers...")
    print()

    total_q = 0
    for nom, contenu in fichiers:
        chemin = os.path.join(dossier, nom)
        ecrire_fichier(chemin, contenu)
        total_q += compter_questions(contenu)

    print()
    print("=" * 65)
    print(f" TERMINE -- {total_q} questions generees dans 3 fichiers")
    print(f" Repertoire : {dossier}")
    print("=" * 65)


if __name__ == "__main__":
    main()
