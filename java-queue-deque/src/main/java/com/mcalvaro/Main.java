package com.mcalvaro;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        System.out.println("Queue Interface");

        Queue<String> queue = new LinkedList<>();

        queue.add("Corne Keyboard");
        queue.add("Lulu Keyboard");
        queue.add("Lily58 Keyboard");

        System.out.println("Queue: " + queue);

        // Remove
        String first = queue.remove();

        System.out.println("Removed Element: " + first);

        System.out.println("Queue after removal: " + queue);

        queue.add("Microdox Keyboard");

        // // Peek
        String peeked = queue.peek();
        System.out.println("Peeked Element: " + peeked);

        System.out.println("Queue after peek: " + queue);

        // Los métodos de la colección
        // como size y contains
        // pueden ser usados con esta
        // implementación.
        System.out.println("Size of queue: "
        + queue.size());

        // ----------------------------------------
        // Deque
        Deque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(4);
        deque.addLast(1);
        System.out.println("Deque: " + deque);
        int first_elment = deque.removeFirst();
        int last_element = deque.removeLast();
        System.out.println("First: " + first_elment + ", Last: " + last_element);

        // // Iterar Ambas Direcciones
        Deque<String> dq = new ArrayDeque<String>();
        dq.add("Mouse");
        dq.add("Keyboard");
        dq.add("Air Book");

        for (Iterator itr = dq.iterator(); itr.hasNext();) {
        System.out.println(itr.next() + " ");
        }

        System.out.println("-----------------------");

        for (Iterator itr = dq.descendingIterator(); itr.hasNext();) {
        System.out.println(itr.next() + " ");
        }

        // -----------------------------------------------------

        // PriorityQueue
        Queue<String> pq = new PriorityQueue<>();

        pq.add("Mouse");
        pq.add("Keyboard");
        pq.add("Air Book");

        // // Iterar

        Iterator iterator = pq.iterator();
        while (iterator.hasNext()) {
            System.out.println("- " + iterator.next());
        }

        pq.remove("Mouse");

        System.out.println("After remove: " + pq);

    }
}
