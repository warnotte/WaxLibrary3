package io.github.warnotte.waxlib3.core.PropertyChangeManager.experimental;

import java.util.LinkedList;

public class LimitedList<T> {
    private LinkedList<T> list;
    private int maxSize;

    public LimitedList(int maxSize) {
        this.maxSize = maxSize;
        this.list = new LinkedList<>();
    }

    // Méthode pour ajouter un élément (push)
    public void push(T item) {
        if (list.size() >= maxSize) {
            list.removeFirst(); // Supprime l'élément le plus ancien
        }
        list.add(item);
    }

    // Méthode pour retirer le dernier élément (pop)
    public T pop() {
        if (!list.isEmpty()) {
            return list.removeLast();
        } else {
            System.out.println("Liste vide. Impossible de retirer un élément.");
            return null;
        }
    }

    // Méthode pour vérifier si la liste est vide
    public boolean isEmpty() {
        return list.isEmpty();
    }

    // Méthode pour obtenir la taille actuelle de la liste
    public int size() {
        return list.size();
    }

    // Méthode pour obtenir l'élément au sommet sans le retirer
    public T peek() {
        if (!list.isEmpty()) {
            return list.getLast();
        } else {
            System.out.println("Liste vide. Aucun élément au sommet.");
            return null;
        }
    }
}
