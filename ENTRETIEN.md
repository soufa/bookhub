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
