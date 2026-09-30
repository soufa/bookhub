# Q/R Entretien — BookHub

Objectif : 250 questions/réponses au 31/10/2026.

Format :
- Q : question
- R courte : réponse en 30 s
- Exemple : code
- Piège : ce qui fait la différence
- Vécu : anecdote perso

---

## Java — Records

### Q 1. Qu'est-ce qu'un record Java ?

**R courte** : Type immuable introduit en Java 16. Génère automatiquement constructeur canonique, accesseurs, equals, hashCode, toString.

Exemple :
\\\java
public record Isbn(String value) {
    public Isbn {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("ISBN required");
    }
}
\\\

Piège : immuabilité shallow. Un record avec List doit avoir un compact constructor qui fait List.copyOf().

---

### Q 2. Différence entre record et classe classique ?

**R courte** : Record = immuable par défaut, pas de setters, equals/hashCode basés sur les composants, héritage interdit. Classe = libre, mutable.

Piège : un record ne peut pas étendre une classe.

---

### Q 3. Un record peut-il être générique ?

**R courte** : Oui.

Exemple :
\\\java
public record PageResult<T>(List<T> content, int page, int size, long total) {}
\\\

---

## Java — Optional

### Q 4. Quand utiliser Optional ?

**R courte** : Uniquement en retour de méthode quand l'absence est possible. Jamais en paramètre, jamais en champ JPA.

Piège : Optional.get() sans isPresent() est un anti-pattern.

---

### Q 5. Différence entre orElse et orElseGet ?

**R courte** : orElse(value) évalue toujours l'argument. orElseGet(supplier) évalue seulement si vide.


## Java — Records avancés & sealed



#### Q6. Qu'est-ce que le pattern matching instanceof ?
**R courte** : Introduit en Java 16. Permet de tester et caster en une seule expression, avec une variable de pattern scopée au bloc où le test est vrai.

Exemple :

java
if (obj instanceof String s) {
    System.out.println(s.length());
}
Piège : la variable s n'existe que dans la branche où le test est vrai (flow scoping).

Vécu : (à remplir)


#### Q7. Switch expression vs switch statement ?
**R courte** : Switch expression (Java 14) renvoie une valeur et vérifie l'exhaustivité. Pas de break, pas de fall-through avec ->.

Exemple :

java
String label = switch (day) {
    case MONDAY, FRIDAY -> "Travail";
    case SATURDAY, SUNDAY -> "Weekend";
    default -> "Autre";
};
Piège : default obligatoire sauf sur enum / sealed (exhaustivité vérifiée par le compilateur).

Vécu : (à remplir)


#### Q8. Qu'est-ce qu'une sealed interface ?
**R courte** : Hiérarchie fermée (Java 17). Seuls les types listés dans permits peuvent implémenter.

Exemple :

java
public sealed interface Shape permits Circle, Rectangle {}
Piège : le compilateur peut vérifier l'exhaustivité des switch, sans default.

Vécu : (à remplir)

#### Q9. Différence entre final et sealed ?
**R courte** : final interdit toute extension. sealed autorise uniquement les sous-types listés.

Piège : une classe sealed peut être étendue par un autre sealed, final ou non-sealed.

Vécu : (à remplir)

### Q10. Pourquoi utiliser un compact constructor dans un record ?

**R courte** : Pour valider et normaliser les composants avant l'affectation automatique des champs.

**Exemple** :

java
public Isbn {
    Objects.requireNonNull(value);
    value = value.replaceAll("-", "");
}
Piège : on ne peut pas réassigner un composant en dehors du compact constructor.

Vécu : (à remplir)


#### Q11. Un record peut-il être final ?

**R courte** : Un record est implicitement final. On ne peut pas le déclarer final explicitement.

Piège : c'est pour cette raison qu'un record ne peut pas être proxyfié par Hibernate.

Vécu : (à remplir)


#### Q12. Peut-on avoir un record avec un seul composant ?

**R courte** :  Oui, très utile pour les Value Objects.

Exemple :

java
public record Email(String value) {}
Piège : un record à un composant n'est pas un wrapper — c'est un type à part entière.

Vécu : (à remplir)


#### Q13. Peut-on utiliser un record comme entité JPA ?

**R courte** : Non. Hibernate a besoin d'un proxy (sous-classe), et un record est final.

Alternative : utiliser un record pour les DTO, une classe pour les entités.

Piège : depuis Hibernate 6.2, on peut utiliser des records dans les projections JPQL, mais pas comme entités.
}

## Java — Records (suite) & divers

### Q 14. Quand utiliser `Objects.requireNonNull` ?

**R courte** : En début de constructeur ou de méthode, pour valider qu'un paramètre n'est pas null.
 Lance `NullPointerException` immédiatement, plutôt qu'un NPE plus tard à un endroit imprévisible.

**Exemple** :
public Money {
    Objects.requireNonNull(amount, "Amount required");
    Objects.requireNonNull(currency, "Currency required");
}


### Q 15. Différence entre List.of() et Arrays.asList() ?
**R courte** :
List.of() est immuable (refuse null). Arrays.asList() est de taille fixe mais permet set(), et accepte null.

**Exemple**:

**java**
List<String> immutable = List.of("a", "b");     // set() → UnsupportedOperationException
List<String> fixed = Arrays.asList("a", "b");   // set() OK, add() → UnsupportedOperationException
**Piège** 
 List.of() lève NullPointerException si un élément est null. Arrays.asList() accepte les nulls.
 
 ### Q 16  Quand utiliser List.copyOf() ? ?
**R courte** :
R courte : Pour faire une copie défensive d'une collection, en garantissant l'immuabilité. Crée une nouvelle liste, refuse les nulls.
**Exemple**
public record Team(String name, List<String> members) {
    public Team {
        members = List.copyOf(members);   // copie défensive
    }
}
**Piège**
 sans List.copyOf(), l'appelant peut modifier la liste après le constructeur.
 
 
 ###  Q 17. Différence entre String.replace et String.replaceAll ?
**R courte** :
 replace(CharSequence, CharSequence) = remplacement littéral. replaceAll(String regex, String replacement) = regex.
 **Exemple**
 "a.b.c".replace(".", "-");      // "a-b-c"  (littéral)
"a.b.c".replaceAll(".", "-");   // "-----"  (regex : . = n'importe quel caractère)

### Q 18. BigDecimal vs double — pourquoi BigDecimal pour l'argent ?
**R courte** :
double a une précision binaire limitée (0.1 + 0.2 ≠ 0.3). BigDecimal a une précision arbitraire et un contrôle sur l'échelle (scale).
 **Exemple**
 System.out.println(0.1 + 0.2);                  // 0.30000000000000004
System.out.println(new BigDecimal("0.1").add(new BigDecimal("0.2")));  // 0.3
**Piège**
toujours construire un BigDecimal depuis une String, jamais depuis un double :
 **Exemple**
 new BigDecimal(0.1);      // ❌ 0.1000000000000000055...
new BigDecimal("0.1");    // ✅ 0.1

### Q 19. Currency.getInstance() — que se passe-t-il si le code est invalide ?
**R courte** :
Lance IllegalArgumentException si le code ISO 4217 n'existe pas.
**Exemple**Currency.getInstance("EUR");   // OK
Currency.getInstance("XXX");   // IllegalArgumentException
Currency.getInstance("eu");    // IllegalArgumentException (case-sensitive)
**Piege**
c'est un point d'entrée sensible. Valider les codes devise côté API (enum ou liste blanche).
### Q 20. Record avec BigDecimal — piège equals sur scale
**R courte** :
BigDecimal.equals() compare valeur ET scale. new BigDecimal("1.0").equals(new BigDecimal("1.00")) est false !
** Exemple **
new BigDecimal("1.0").equals(new BigDecimal("1.00"));        // false
new BigDecimal("1.0").compareTo(new BigDecimal("1.00")) == 0; // true
**Piege**
un record Money(amount, currency) avec BigDecimal peut avoir un equals surprenant.
 Solution : normaliser le scale dans le constructeur (amount.setScale(2, RoundingMode.HALF_UP)), ou utiliser compareTo
### Q 21. Pourquoi Objects.hash() plutôt que hashCode() manuel ?
**R courte** :
Objects.hash(a, b, c) combine plusieurs valeurs en un hash unique, sans écrire à la main les 31 * result + ....
**Exemple **
@Override
public int hashCode() {
    return Objects.hash(email, name);   // ✅ propre
}
**Piege**
depuis Java 16+, les record génèrent automatiquement equals/hashCode. Ne les redéfinir que si nécessaire

---

## Java — Enums & sealed

### Q 22. Qu'est-ce qu'un enum Java ?

**R courte** : Type spécial qui représente un ensemble fini et fixe de constantes. Ce sont des classes à part entière : elles peuvent avoir des champs, des constructeurs, des méthodes.

**Exemple** :
***java***
public enum BookStatus {
    AVAILABLE("Available"),
    BORROWED("Currently borrowed");

    private final String description;
    BookStatus(String description) { this.description = description; }
    public String description() { return description; }
}
***PIEGE***
 le constructeur d'un enum est implicitement privé. On ne peut pas instancier un enum avec new.

###Q 23. Différence entre enum et constantes static final ?
**R courte** : Enum = type à part entière, sûr à la compilation, itérable, avec méthodes et champs. static final String = juste une valeur, aucune garantie de validité.

**Piege** : un enum peut être utilisé dans un switch (exhaustivité vérifiée). Les constantes String ne le permettent pas.

### Q 24. Qu'est-ce qu'une sealed interface ?
**R courte** : Interface qui restreint les types qui peuvent l'implémenter. Introduite en Java 17 (final). Hiérarchie fermée, exhaustivité au compilateur.

**Exemple** :

java
public sealed interface Shape permits Circle, Rectangle {}

**Piège** : les sous-types doivent être dans le même module ou le même package (si module-less).

### Q 25. Différence entre sealed, final, non-sealed ?
**R courte** :

final : aucune extension possible

sealed : extension limitée à une liste explicite (permits)

non-sealed : extension libre (désactive le sceau pour un sous-type)

**Piège** : un sous-type d'une classe sealed doit être déclaré final, sealed ou non-sealed

### Q 26. Pourquoi utiliser un record comme DTO ?
**R courte** : Immuable, concis, equals/hashCode automatiques, idéal pour transporter des données entre couches (contrôleur ↔ service ↔ client).

**Exemple** :

java
public record BookDto(Long id, String title) {}

**Piège** : un record n'est pas adapté aux entités JPA (Hibernate a besoin de proxyfié, et un record est final).

###Q 27. Où valider : DTO ou entité ?

**R courte** : Les deux, mais différemment. Le DTO valide la forme (champs requis, format). L'entité valide les invariants métier (unicité, cohérence).

**Piège** : ne pas dupliquer la même validation aux deux endroits. Le DTO assure que l'input est propre, l'entité assure l'intégrité.


### Q 28. Qu'est-ce que le pattern matching instanceof ?

**R courte** : Java 16+. Permet de tester et caster en une seule expression, avec une variable scopée au bloc où le test est vrai.

**Exemple** :

java
if (notification instanceof EmailNotification email) {
    System.out.println(email.email());
}
**Piège** : la variable email n'existe que dans la branche où le test est vrai (flow scoping).

###Q 29. Switch expression sur enum ?

**R courte** : Depuis Java 14, un switch peut être une expression qui renvoie une valeur. Sur un enum, le compilateur vérifie l'exhaustivité (pas besoin de default).

**Exemple** :

java
String label = switch (status) {
    case AVAILABLE -> "Dispo";
    case BORROWED -> "Emprunté";
    case RESERVED -> "Réservé";
    case LOST, MAINTENANCE -> "Indisponible";
};
**Piège** : -> (arrow) remplace : et break. Pas de fall-through possible.


---

## Java — Collections

### Q30. Différence entre `ArrayList` et `LinkedList` ?

**R courte** : `ArrayList` = tableau dynamique, accès index O(1), insertion fin O(1) amorti. `LinkedList` = liste doublement chaînée, insertion milieu O(1) si nœud connu, accès O(n).

**Piège** : dans 95 % des cas, `ArrayList` est plus rapide (cache-friendly). Ne pas choisir `LinkedList` sur la seule théorie.

**Vécu** : (à remplir)

---

### Q31. Différence entre `HashMap` et `TreeMap` ?

**R courte** : `HashMap` = O(1) amorti, pas d'ordre. `TreeMap` = O(log n), clés triées, navigation (`floorKey`, `ceilingKey`).

**Piège** : `TreeMap` exige que les clés soient `Comparable` ou un `Comparator` fourni. Un `compareTo` incohérent avec `equals` produit des comportements étranges.

**Vécu** : (à remplir)

---

### Q32. Pourquoi `EnumMap` plutôt que `HashMap<MyEnum, V>` ?

**R courte** : `EnumMap` utilise un tableau indexé par `ordinal`. Plus compact, plus rapide, ordre de déclaration naturel.

**Piège** : toujours préférer `EnumMap` quand les clés sont une enum. Pour les sets, utiliser `EnumSet`.

**Vécu** : (à remplir)

---

### Q33. Qu'est-ce que `ConcurrentModificationException` ?

**R courte** : Levée par les itérateurs fail-fast (`ArrayList`, `HashMap`) quand la collection est modifiée pendant l'itération.

**Exemple** :
java
for (String s : list) if (s.isEmpty()) list.remove(s); // ❌ CME
list.removeIf(String::isEmpty);                          // ✅


### Q34. Différence entre fail-fast et fail-safe ?
**R courte** : fail-fast (ArrayList) = détecte la modification, lève CME. fail-safe (CopyOnWriteArrayList) = snapshot, pas d'exception, modifications invisibles.

**Piège** : CopyOnWriteArrayList copie tout à chaque écriture → inadapté aux écritures fréquentes.


### Q35. Qu'est-ce que PriorityQueue ?
**R courte** : Tas binaire. offer() et poll() en O(log n). L'itérateur ne garantit pas l'ordre du tas.

**Exemple** :
PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
Piège : peek() renvoie le plus petit (ou plus grand selon le comparator), mais forEach() renvoie dans un ordre arbitraire.

### Q36. Différence entre Stack et ArrayDeque ?
**R courte** : Stack étend Vector (synchronisé, legacy). ArrayDeque est plus rapide, non synchronisé, API Deque claire (push/pop/offer/poll aux deux bouts).

**Piège** : Stack est un anti-pattern. Toujours préférer ArrayDeque.


### Q37. Comment rendre une collection immuable ?
**R courte** : List.copyOf(), Set.copyOf(), Map.copyOf() (Java 10+) → copie défensive + immuable. Refuse null.

**Piège** : Collections.unmodifiableList() est une vue — si la liste source change, la vue change aussi. Pas une copie.


## Java — Streams & Lambda

### Q38. Différence entre `map` et `flatMap` ?

**R courte** : `map` transforme 1 → 1. `flatMap` transforme 1 → N (aplatit un `Stream<Stream<T>>` en `Stream<T>`).

**Exemple** :
// map → Stream<Stream<Item>>
orders.stream().map(o -> o.items().stream());

// flatMap → Stream<Item>
orders.stream().flatMap(o -> o.items().stream());
Piège : si vous écrivez map et obtenez un Stream<Stream<...>>, c'est flatMap qu'il fallait utiliser.


### Q39. reduce vs collect — quand utiliser l'un ou l'autre ?
**R courte** : reduce pour des valeurs immuables (somme, min, max). collect pour accumuler dans une structure mutable (List, Map, StringBuilder).

**Piège** : reduce avec un accumulateur mutable partagé = race condition en parallèle. Utiliser collect avec un supplier frais par partition.

### Q40. findFirst vs findAny ?
**R courte** : findFirst = déterministe, respecte l'ordre d'encounter. findAny = autorise une optimisation en parallèle, résultat non déterministe.

**Piège** : sur un stream parallèle ordonné, findFirst coûte cher. Préférer findAny quand l'ordre n'importe pas.

### Q41. Pourquoi les Streams sont-ils lazy ?
** R courte** : Les opérations intermédiaires ne s'exécutent pas tant qu'aucune opération terminale n'est appelée. Permet les optimisations (short-circuit, fusion d'opérations).

**Exemple** :

java
Stream.of(1, 2, 3).filter(x -> { System.out.println(x); return true; });
// Rien ne s'affiche : pas d'opération terminale
**Piège** : oublier l'opération terminale = pipeline jamais exécuté, aucune erreur.


### Q42. Collectors.toMap — pièges ?
**R courte** : Lève IllegalStateException si clé dupliquée (sans merge function). Lève NullPointerException si valeur null.

**Exemple** :

java
.collect(Collectors.toMap(
    Book::isbn,
    Book::title,
    (a, b) -> a    // merge : garder le premier
));
**Piège** : toujours fournir une merge function si les clés peuvent être dupliquées. toMap refuse null en valeur.

###Q43. groupingBy avec downstream — à quoi ça sert ?
**R courte** : Le 2ᵉ argument est un collector appliqué à chaque groupe. Permet de compter, moyenner, mapper à l'intérieur de chaque groupe.

**Exemple** :

java
Map<String, Long> countByAuthor = books.stream()
    .collect(Collectors.groupingBy(
        Book::author,
        Collectors.counting()
    ));
**Piège** : par défaut, groupingBy retourne List<T>. Utiliser un downstream (counting(), mapping(), averagingInt()) pour transformer.

### Q44. Pourquoi éviter les effets de bord dans map/filter ?
**R courte** : Les opérations intermédiaires doivent être pures. Sinon → résultats non déterministes en parallèle, incompatibilité avec les optimisations du framework.

**Exemple** :

java
// ❌ Anti-pattern
stream.map(x -> { counter.incrementAndGet(); return x * 2; });

// ✅ Utiliser un collector ou compter en sortie
long count = stream.count();
**Piège** : peek est prévu pour le debug, pas pour la logique métier

### Q45. Quand utiliser parallelStream ?
**R courte** : CPU-bound, gros volumes (milliers+), pas d'effet de bord, opérations associatives. Sinon, l'overhead dépasse le gain.

**Piège** : parallelStream sur I/O-bound ou petits volumes = plus lent que séquentiel. Toujours mesurer avant.**

---

## Spring — Core (IoC & DI)

### Q46. Qu'est-ce que l'Inversion de Contrôle (IoC) ?

**R courte** : Principe où le conteneur (Spring) crée et gère les objets, au lieu que ce soit le code qui instancie (`new`). Le contrôle est inversé : au lieu d'appeler `new Service()`, on demande au conteneur.

**Exemple** :
// Sans Spring
NotificationService service = new NotificationService(new EmailSender());

// Avec Spring
@Autowired
NotificationService service;   // Spring l'a créé et injecté

###Q47. Différence entre @Component, @Service, @Repository, @Controller ?
**R courte** : Tous sont des stéréotypes Spring (spécialisations de @Component). Ils ont la même fonction technique mais une intention sémantique différente :

@Component : générique

@Service : logique métier

@Repository : accès aux données (+ traduction des exceptions)

@Controller / @RestController : couche web

**Piège** : @Repository active la traduction des exceptions JPA (DataAccessException). Pas les autres.

###Q48. Injection par constructeur vs par champ vs par setter ?
**R courte** :

Constructeur : recommandé (immuabilité, testabilité, détection des dépendances manquantes au démarrage)

Setter : pour les dépendances optionnelles

Champ : à éviter (impossible à tester sans Spring, masque les dépendances)

**Exemple** :

java
// ✅ Constructeur
public BookService(BookRepository repo) { this.repo = repo; }

// ❌ Champ
@Autowired private BookRepository repo;
**Piège** : l'injection par champ ne permet pas l'utilisation du mot-clé final sur la dépendance.

###Q49. @Configuration + @Bean vs @Component ?
**R courte** :

@Component : sur une classe, Spring la détecte par scan

@Configuration + @Bean : sur une méthode, Spring appelle la méthode pour créer le bean

Quand utiliser @Bean : quand vous ne pouvez pas annoter la classe (ex: Clock, String, librairie tierce).

**Piège** : une méthode @Bean dans une classe @Configuration est proxifiée — Spring garantit qu'elle renvoie toujours le même singleton.

###Q50. @Value — comment l'utiliser ?
**R courte** : Injecte une valeur depuis application.yml / application.properties ou une variable d'environnement.

**Exemple* :

java
@Value("${bookhub.greeting.message:Hello}")
private String message;
**Piège** : syntaxe ${clé:valeur_par_défaut}. Sans valeur par défaut, si la clé est absente → IllegalArgumentException au démarrage.


###Q51. Qu'est-ce que @PostConstruct / @PreDestroy ?
**R courte** :

@PostConstruct : appelé après l'injection des dépendances, avant que le bean soit utilisé

@PreDestroy : appelé avant la destruction du contexte Spring

Utile pour : ouvrir/fermer des ressources, initialiser des caches, logger le démarrage.

**Piège** : en Java 17, ces annotations viennent de jakarta.annotation (avant : javax.annotation).

### Q52. Qu'est-ce qu'un bean singleton par défaut ?
**R courte** : Un bean Spring est singleton par défaut : une seule instance partagée par toute l'application.

**Autres scopes** : prototype (nouvelle instance à chaque demande), request, session (web).

**Piège** : un singleton ne doit jamais avoir d'état mutable partagé. Sinon → problèmes de concurrence.

###Q53. Qu'est-ce que ApplicationContext ?
**R courte** : Le conteneur IoC de Spring. Il gère le cycle de vie des beans, l'injection des dépendances, la résolution des propriétés.

**Exemple** :

java
ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
GreetingService service = ctx.getBean(GreetingService.class);
**Piège** : en Spring Boot, l'ApplicationContext est créé automatiquement par SpringApplication.run(). On n'a pas besoin de l'instancier à la main.