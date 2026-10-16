# Java - Questions d'Interview

> **Nombre de questions :** 60
> **Généré le :** 2026-10-16 14:38:36

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
public final class Constants {
    public static final double PI = 3.14159;
    public final void display() {
        // Ne peut pas être overridée
    }
}
```

### 7. Peut-on surcharger ou overrider les méthodes static ?

**Réponse :**

Surcharge (Overloading) : OUI, possible

Override : NON, impossible (les méthodes static sont liées à la classe, pas à l'instance)

```java
class Parent {
    static void display() { System.out.println("Parent"); }
}
class Child extends Parent {
    static void display() { System.out.println("Child"); } // Hiding, pas overriding
}
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
public class BankAccount {
    private double balance;  // Champ privé
    public double getBalance() {
        return balance;
    }
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }
}
```

### 11. Quelle est la différence entre surcharge et override ?

**Réponse :**

Surcharge (Overloading) : Même nom, paramètres différents, même classe

Override : Même signature, sous-classe redéfinit la méthode parente

```java
// Overloading
public void print(int x) { }
public void print(String s) { }

// Overriding
@Override
public String toString() { return "custom"; }
```

### 12. Qu'est-ce qu'un constructeur ?

**Réponse :**

Méthode spéciale pour initialiser un objet.

```java
public class User {
    private String name;
    private int age;
    
    // Constructeur par défaut
    public User() { }
    
    // Constructeur paramétré
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

### 13. Qu'est-ce que le chaînage de constructeurs ?

**Réponse :**

Appeler un constructeur depuis un autre constructeur avec this().

```java
public class User {
    private String name;
    private int age;
    
    public User() {
        this("Unknown", 0);
    }
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

### 14. Quelle est la différence entre this et super ?

**Réponse :**

this : Référence à l'instance courante

super : Référence à la classe parente

```java
public class Child extends Parent {
    public Child() {
        super();  // Appelle le constructeur parent
        this.name = "Child";  // Référence à l'instance courante
    }
}
```

### 15. Qu'est-ce que la covariance des types de retour ?

**Réponse :**

Une méthode overridée peut retourner un sous-type du type de retour original.

```java
class Parent {
    Object getValue() { return new Object(); }
}
class Child extends Parent {
    @Override
    String getValue() { return "Hello"; }  // String est un sous-type d'Object
}
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
Function<Integer, Integer> square = n -> {
    int result = n * n;
    return result;
};
```

### 18. Qu'est-ce qu'une interface fonctionnelle ?

**Réponse :**

Une interface avec exactement une méthode abstraite. Elle peut avoir plusieurs méthodes default/static.

```java
@FunctionalInterface
public interface Calculator {
    int calculate(int a, int b);
    default void display() {
        System.out.println("Calculator");
    }
}

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
public interface Vehicle {
    void start();
    default void honk() {
        System.out.println("Beep beep!");
    }
}
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
    {
        "name": "John",
        "age": 30
    }
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
for (String s : cowList) {
    cowList.add("new");  // Pas d'exception
}
```

### 38. Quelle est la différence entre Comparable et Comparator ?

**Réponse :**

Comparable : Ordre naturel, implémenté par la classe

Comparator : Ordre personnalisé, classe externe

```java
public class Employee implements Comparable<Employee> {
    @Override
    public int compareTo(Employee other) {
        return this.name.compareTo(other.name);
    }
}

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
enum Day { MONDAY, TUESDAY, WEDNESDAY }
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
class MyThread extends Thread {
    public void run() {
        System.out.println("Running");
    }
}
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
synchronized(resourceA) {
    synchronized(resourceB) { }  // Attend resourceB
}
// Thread 2
synchronized(resourceB) {
    synchronized(resourceA) { }  // Attend resourceA
}
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
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    return "Hello";
});
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
public class SharedResource {
    private volatile boolean flag = false;
    public void setFlag() {
        flag = true;  // Visible immédiatement par tous les threads
    }
}
Note : volatile ne garantit pas l'atomicité.

```

### 54. Qu'est-ce que synchronized ?

**Réponse :**

synchronized assure qu'un seul thread accède à une section critique à la fois.

```java
// Méthode synchronisée
public synchronized void increment() {
    count++;
}

// Bloc synchronisé
public void increment() {
    synchronized(this) {
        count++;
    }
}
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
for (int i = 0; i < 3; i++) {
    new Thread(() -> {
        latch.countDown();
    }).start();
}
latch.await();  // Attend que le compteur atteigne 0
```

### 57. Qu'est-ce qu'un CyclicBarrier ?

**Réponse :**

Synchronise les threads à un point de rendez-vous.

```java
CyclicBarrier barrier = new CyclicBarrier(3, () -> {
    System.out.println("All threads arrived");
});
for (int i = 0; i < 3; i++) {
    new Thread(() -> {
        barrier.await();
    }).start();
}
```

### 58. Qu'est-ce qu'un Semaphore ?

**Réponse :**

Contrôle l'accès à une ressource partagée avec un nombre de permits.

```java
Semaphore semaphore = new Semaphore(3);  // 3 permits
semaphore.acquire();  // Prend un permit
try {
    // Accéder à la ressource
} finally {
    semaphore.release();  // Libère le permit
}
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
class SumTask extends RecursiveTask<Long> {
    protected Long compute() {
        if (array.length <= 1000) {
            // Calcul direct
        }
        // Diviser et conquérir
    }
}
long result = pool.invoke(new SumTask(array));
```
