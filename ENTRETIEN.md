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

R courte : Type immuable introduit en Java 16. Génère automatiquement constructeur canonique, accesseurs, equals, hashCode, toString.

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

R courte : Record = immuable par défaut, pas de setters, equals/hashCode basés sur les composants, héritage interdit. Classe = libre, mutable.

Piège : un record ne peut pas étendre une classe.

---

### Q 3. Un record peut-il être générique ?

R courte : Oui.

Exemple :
\\\java
public record PageResult<T>(List<T> content, int page, int size, long total) {}
\\\

---

## Java — Optional

### Q 4. Quand utiliser Optional ?

R courte : Uniquement en retour de méthode quand l'absence est possible. Jamais en paramètre, jamais en champ JPA.

Piège : Optional.get() sans isPresent() est un anti-pattern.

---

### Q 5. Différence entre orElse et orElseGet ?

R courte : orElse(value) évalue toujours l'argument. orElseGet(supplier) évalue seulement si vide.


## Java — Records avancés & sealed



#### Q6. Qu'est-ce que le pattern matching instanceof ?
R courte : Introduit en Java 16. Permet de tester et caster en une seule expression, avec une variable de pattern scopée au bloc où le test est vrai.

Exemple :

java
if (obj instanceof String s) {
    System.out.println(s.length());
}
Piège : la variable s n'existe que dans la branche où le test est vrai (flow scoping).

Vécu : (à remplir)


#### Q7. Switch expression vs switch statement ?
R courte : Switch expression (Java 14) renvoie une valeur et vérifie l'exhaustivité. Pas de break, pas de fall-through avec ->.

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
R courte : Hiérarchie fermée (Java 17). Seuls les types listés dans permits peuvent implémenter.

Exemple :

java
public sealed interface Shape permits Circle, Rectangle {}
Piège : le compilateur peut vérifier l'exhaustivité des switch, sans default.

Vécu : (à remplir)

#### Q9. Différence entre final et sealed ?
R courte : final interdit toute extension. sealed autorise uniquement les sous-types listés.

Piège : une classe sealed peut être étendue par un autre sealed, final ou non-sealed.

Vécu : (à remplir)

Q10. Pourquoi utiliser un compact constructor dans un record ?
R courte : Pour valider et normaliser les composants avant l'affectation automatique des champs.

Exemple :

java
public Isbn {
    Objects.requireNonNull(value);
    value = value.replaceAll("-", "");
}
Piège : on ne peut pas réassigner un composant en dehors du compact constructor.

Vécu : (à remplir)


#### Q11. Un record peut-il être final ?
R courte : Un record est implicitement final. On ne peut pas le déclarer final explicitement.

Piège : c'est pour cette raison qu'un record ne peut pas être proxyfié par Hibernate.

Vécu : (à remplir)


#### Q12. Peut-on avoir un record avec un seul composant ?
R courte : Oui, très utile pour les Value Objects.

Exemple :

java
public record Email(String value) {}
Piège : un record à un composant n'est pas un wrapper — c'est un type à part entière.

Vécu : (à remplir)


#### Q13. Peut-on utiliser un record comme entité JPA ?
R courte : Non. Hibernate a besoin d'un proxy (sous-classe), et un record est final.

Alternative : utiliser un record pour les DTO, une classe pour les entités.

Piège : depuis Hibernate 6.2, on peut utiliser des records dans les projections JPQL, mais pas comme entités.
}
