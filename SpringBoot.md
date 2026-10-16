# Spring Boot - Questions d'Interview

> **Nombre de questions :** 50
> **Généré le :** 2026-10-16 16:29:29

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
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
Étapes :

Utiliser Spring Initializr (start.spring.io)

Ou ajouter les dépendances dans pom.xml/build.gradle

Créer la classe principale avec @SpringBootApplication

4. Qu'est-ce qu'un Spring Boot Starter ?
Réponse :
Un Starter est un ensemble de descripteurs de dépendances qui simplifie la configuration Maven/Gradle. Il regroupe toutes les dépendances nécessaires pour une fonctionnalité spécifique.

Exemples :

spring-boot-starter-web : Pour les applications web (Spring MVC, Tomcat, Jackson)

spring-boot-starter-data-jpa : Pour JPA avec Hibernate

spring-boot-starter-security : Pour Spring Security

spring-boot-starter-test : Pour les tests (JUnit, Mockito, AssertJ)

5. Expliquez le concept d'auto-configuration dans Spring Boot
Réponse :
L'auto-configuration configure automatiquement l'application Spring en fonction des dépendances présentes dans le classpath.

Fonctionnement :

Spring Boot examine les JARs présents

Il applique des configurations conditionnelles

Les classes @ConditionalOnClass, @ConditionalOnMissingBean contrôlent l'activation

Exemple : Si H2 est dans le classpath, Spring Boot configure automatiquement une base de données en mémoire.

6. Comment définir des propriétés dans une application Spring Boot ?
Réponse :
Dans application.properties ou application.yml :

properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
7. Qu'est-ce qu'un profil dans Spring Boot et comment l'utiliser ?
Réponse :
Un profil permet de séparer les configurations selon l'environnement (dev, test, prod).

Utilisation :

Annotation @Profile("dev") sur les composants

Fichiers application-dev.properties, application-prod.properties

Activation : --spring.profiles.active=dev

java
@Component
@Profile("dev")
public class DevDataSourceConfig {
    // Configuration spécifique au développement
}
8. Comment implémenter la gestion des exceptions dans Spring Boot ?
Réponse :
Utiliser @ControllerAdvice et @ExceptionHandler :

java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
9. Qu'est-ce que l'annotation @SpringBootApplication ?
Réponse :
@SpringBootApplication combine trois annotations :

@Configuration : Indique une classe de configuration

@EnableAutoConfiguration : Active l'auto-configuration

@ComponentScan : Scanne les composants dans le package courant

10. Quelle est la différence entre application.properties et bootstrap.properties ?
Réponse :

bootstrap.properties : Chargé en premier, utilisé pour Spring Cloud Config (connexion au serveur de configuration)

application.properties : Configuration principale de l'application

2. Configuration et Dépendances
11. Comment configurer un DataSource dans Spring Boot ?
Réponse :

properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
12. Comment activer HTTPS dans Spring Boot ?
Réponse :

properties
server.port=8443
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=changeit
server.ssl.keyStoreType=PKCS12
server.ssl.keyAlias=tomcat
13. Comment configurer le logging dans Spring Boot ?
Réponse :

properties
logging.level.org.springframework=DEBUG
logging.level.com.example=INFO
logging.file.name=app.log
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
14. Qu'est-ce que @EnableAutoConfiguration ?
Réponse :
@EnableAutoConfiguration indique à Spring Boot de configurer automatiquement les beans en fonction des dépendances du classpath et des propriétés définies.

15. Comment externaliser la configuration dans Spring Boot ?
Réponse :
Ordre de priorité (du plus élevé au plus bas) :

Arguments de ligne de commande

Variables d'environnement

application-{profile}.properties

application.properties

@PropertySource

16. Qu'est-ce que CommandLineRunner ?
Réponse :
Interface pour exécuter du code après le démarrage de l'application.

java
@Component
public class MyRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Application started!");
    }
}
17. Comment configurer plusieurs DataSources ?
Réponse :

java
@Configuration
public class DataSourceConfig {
    @Bean(name = "primaryDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }
    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }
}
18. Qu'est-ce que @ConfigurationProperties ?
Réponse :
Permet de binder des propriétés à un objet Java de façon type-safe.

java
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
    private String version;
    // getters et setters
}
properties
app.name=MyApp
app.version=1.0.0
19. Quelle est la différence entre @Value et @ConfigurationProperties ?
Réponse :

@Value : Injection de valeurs individuelles, pas de validation

@ConfigurationProperties : Binding de groupes de propriétés, support de la validation, type-safe

20. Comment utiliser @ConditionalOnProperty ?
Réponse :

java
@Configuration
@ConditionalOnProperty(
    name = "feature.cache.enabled",
    havingValue = "true",
    matchIfMissing = false
)
public class CacheConfig {
    // Configuration activée uniquement si la propriété est true
}
3. Dependency Injection et Beans
21. Qu'est-ce que la Dependency Injection dans Spring Boot ?
Réponse :
Design pattern où les dépendances d'un objet sont fournies par le conteneur Spring plutôt que créées par l'objet lui-même.

Types :

Constructor Injection (recommandé)

Setter Injection

Field Injection (déconseillé)

java
@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
22. Qu'est-ce qu'un Spring Bean ?
Réponse :
Un Bean est un objet géré par le conteneur Spring IoC. Il est instancié, assemblé et géré par Spring.

Définition :

@Component : Composant générique

@Service : Couche service

@Repository : Couche DAO

@Controller : Contrôleur web

@Bean : Dans une classe @Configuration

23. Quelle est la différence entre @Component, @Service, @Repository et @Controller ?
Réponse :

@Component : Stéréotype générique pour tout composant Spring

@Service : Spécialisation pour la couche métier

@Repository : Spécialisation pour la couche d'accès aux données (traduction des exceptions)

@Controller : Spécialisation pour les contrôleurs MVC

24. Expliquez la différence entre @Primary et @Qualifier
Réponse :

@Primary : Définit le bean par défaut quand plusieurs candidats existent

@Qualifier : Spécifie explicitement quel bean injecter

java
@Bean
@Primary
public MessageService emailService() {
    return new EmailService();
}
@Bean
public MessageService smsService() {
    return new SMSService();
}
@Autowired
@Qualifier("smsService")
private MessageService messageService;
25. Comment définir un Bean avec @Bean ?
Réponse :

java
@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
26. Quels sont les scopes de Bean dans Spring ?
Réponse :

singleton : Une instance par conteneur (défaut)

prototype : Une instance à chaque demande

request : Une instance par requête HTTP

session : Une instance par session HTTP

application : Une instance par ServletContext

27. Comment créer une annotation personnalisée ?
Réponse :

java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {
    String value() default "";
}
28. Qu'est-ce que le cycle de vie d'un Bean Spring ?
Réponse :

Instanciation

Population des propriétés

BeanNameAware.setBeanName()

ApplicationContextAware.setApplicationContext()

@PostConstruct / afterPropertiesSet()

Utilisation

@PreDestroy / destroy()

29. Comment utiliser @Lookup ?
Réponse :

java
@Component
public abstract class CommandManager {
    @Lookup
    protected abstract Command createCommand();
}
30. Quelle est la différence entre @Resource, @Inject et @Autowired ?
Réponse :

@Autowired : Spécifique Spring, injection par type puis par nom

@Resource : JSR-250, injection par nom d'abord

@Inject : JSR-330, standard Java, similaire à @Autowired

4. REST et Web Development
31. Comment créer un service RESTful dans Spring Boot ?
Réponse :

java
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.save(user));
    }
}
32. Quelle est la différence entre @RequestBody et @ResponseBody ?
Réponse :

@RequestBody : Lie le corps de la requête HTTP à un paramètre de méthode

@ResponseBody : Lie la valeur de retour de la méthode au corps de la réponse HTTP

Avec @RestController, @ResponseBody est implicite.

33. Comment valider une requête dans Spring Boot ?
Réponse :

java
@PostMapping("/users")
public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
    return ResponseEntity.ok(userService.save(user));
}

public class User {
    @NotNull
    @Size(min = 2, max = 50)
    private String name;
    @Email
    private String email;
    @Min(18)
    private int age;
}
34. Quelle est la différence entre @PathVariable et @RequestParam ?
Réponse :

@PathVariable : Extrait les valeurs du chemin URI

@RequestParam : Extrait les paramètres de requête

java
@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) { }

@GetMapping("/search")
public List<User> search(@RequestParam String name) { }
35. Comment implémenter la pagination dans Spring Boot ?
Réponse :

java
@GetMapping("/users")
public Page<User> getUsers(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(defaultValue = "id") String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    return userRepository.findAll(pageable);
}
36. Comment gérer CORS dans Spring Boot ?
Réponse :

java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://example.com")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
37. Quelle est la différence entre POST et PUT ?
Réponse :

POST : Crée une nouvelle ressource, non idempotent

PUT : Met à jour ou crée une ressource, idempotent

38. Quels sont les codes de statut HTTP courants ?
Réponse :

200 OK : Succès

201 Created : Ressource créée

204 No Content : Succès sans contenu

400 Bad Request : Requête invalide

401 Unauthorized : Non authentifié

403 Forbidden : Non autorisé

404 Not Found : Ressource non trouvée

500 Internal Server Error : Erreur serveur

39. Comment implémenter HATEOAS ?
Réponse :

java
@GetMapping("/users/{id}")
public EntityModel<User> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    EntityModel<User> resource = EntityModel.of(user);
    resource.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
    return resource;
}
40. Comment créer un filtre personnalisé ?
Réponse :

java
@Component
public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        chain.doFilter(request, response);
        long duration = System.currentTimeMillis() - start;
        System.out.println("Request took: " + duration + "ms");
    }
}
5. Spring Data et JPA
41. Qu'est-ce que Spring Data JPA ?
Réponse :
Spring Data JPA simplifie l'implémentation de la couche d'accès aux données en fournissant des repositories basés sur JPA.

java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByAgeGreaterThan(int age);
    @Query("SELECT u FROM User u WHERE u.department = :dept")
    List<User> findByDepartment(@Param("dept") String department);
}
42. Quelle est la différence entre CrudRepository, JpaRepository et PagingAndSortingRepository ?
Réponse :

CrudRepository : Opérations CRUD de base

PagingAndSortingRepository : Étend CrudRepository avec pagination et tri

JpaRepository : Étend PagingAndSortingRepository avec des méthodes JPA spécifiques

43. Comment définir une requête personnalisée ?
Réponse :

java
// JPQL
@Query("SELECT u FROM User u WHERE u.email = ?1")
User findByEmail(String email);

// SQL natif
@Query(value = "SELECT * FROM users WHERE email = ?1", nativeQuery = true)
User findByEmailNative(String email);

// Query By Method Name
List<User> findByLastNameAndFirstName(String lastName, String firstName);
44. Comment gérer les transactions dans Spring Boot ?
Réponse :

java
@Service
public class UserService {
    @Transactional
    public void transferMoney(Long fromId, Long toId, BigDecimal amount) {
        // Opérations dans une transaction
    }
    @Transactional(readOnly = true)
    public User findUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
45. Qu'est-ce que le problème N+1 et comment le résoudre ?
Réponse :
Le problème N+1 se produit quand une requête charge une entité, puis N requêtes supplémentaires chargent les entités liées.

Solutions :

java
// JOIN FETCH
@Query("SELECT u FROM User u JOIN FETCH u.orders WHERE u.id = :id")
User findUserWithOrders(@Param("id") Long id);

// EntityGraph
@EntityGraph(attributePaths = {"orders"})
User findWithOrdersById(Long id);
46. Qu'est-ce que @Entity et @Table ?
Réponse :

java
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_name", nullable = false, length = 100)
    private String name;
    @Transient
    private String temporaryField;
}
47. Quels sont les types de relations JPA ?
Réponse :

@OneToOne

@OneToMany

@ManyToOne

@ManyToMany

java
@Entity
public class Order {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
}
48. Qu'est-ce que FetchType.LAZY vs EAGER ?
Réponse :

LAZY : Charge l'entité liée uniquement quand elle est accédée

EAGER : Charge l'entité liée immédiatement

java
@ManyToOne(fetch = FetchType.LAZY)  // Recommandé
private Customer customer;
49. Comment utiliser @Modifying et @Query ?
Réponse :

java
@Modifying
@Transactional
@Query("UPDATE User u SET u.active = false WHERE u.lastLogin < :date")
int deactivateInactiveUsers(@Param("date") LocalDate date);
50. Qu'est-ce qu'un Projection dans Spring Data JPA ?
Réponse :
Interface pour récupérer un sous-ensemble de propriétés.

java
public interface UserSummary {
    String getName();
    String getEmail();
}

@Query("SELECT u.name as name, u.email as email FROM User u")
List<UserSummary> findAllSummaries();
Fin du fichier SpringBoot.md
