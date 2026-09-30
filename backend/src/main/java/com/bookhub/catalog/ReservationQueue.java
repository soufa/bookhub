package com.bookhub.catalog;

import com.bookhub.shared.domain.Isbn;

import java.time.Instant;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

public class ReservationQueue {
    /***
     * Pourquoi ArrayDeque plutôt que Stack ?
     * Deque est une structure a double extrimite ,
     * ca veut dire quand peut ajouter des elements au debut ou a la fin de la structure
     * elle peut travailler comme une pile LIFO ou comme une queue FIFO
     * stack une pile LIFO last in first out
     * @param user
     * @param isbn
     * @param requestedAt
     */
    public record Reservation(String user, Isbn isbn, Instant requestedAt) {
        public Reservation {
            Objects.requireNonNull(user, "User required");
            Objects.requireNonNull(isbn, "Isbn required");
            Objects.requireNonNull(requestedAt, "Date required");
        }
    }

    /***
     * Comparator c'est une interface java qui permet de definir comment comparer et trier des objets
     */
    private final PriorityQueue<Reservation> queue =
            /*** pour chaque reservation appel la methode requestedAt
             * pour retinir la date demande pour la comparerr**/
            new PriorityQueue<>(Comparator.comparing(Reservation::requestedAt));

    public void enqueue(Reservation reservation) {
        Objects.requireNonNull(reservation, "Reservation required");
        queue.offer(reservation);
    }

    /***
     * poll retourne lelement de plus haut priorite
     * mais literatrtion dans une boucle ne garnatie pas la prioirté
     * @return
     */
    public Reservation next() {
        /*** supprime le premeir element de la queue**/
        return queue.poll();
    }

    public Reservation peek() {
        /**regaredre lelement en tete sans le supprimer**/
        return queue.peek();
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}